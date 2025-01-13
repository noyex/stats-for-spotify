package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.Artists;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.ArtistsRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.ArtistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArtistsServiceTests {

    @Mock
    private ArtistsRepository artistsRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private ArtistsService artistsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testArtistMapper() {
        Artist spotifyArtist = mock(Artist.class);
        when(spotifyArtist.getName()).thenReturn("Test Artist");

        List<Artists> result = artistsService.artistMapper(new Artist[]{spotifyArtist});

        assertEquals(1, result.size());
        assertEquals("Test Artist", result.get(0).getName());
    }

    @Test
    void testUpdateAndFetchArtists() {
        UserDetails user = new UserDetails();
        user.setRefId("testRefId");

        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setRefId("testRefId");

        Artists existingArtist = new Artists();
        existingArtist.setName("Existing Artist");
        existingArtist.setUserDetails(userDetails);

        Artists newArtist = new Artists();
        newArtist.setName("New Artist");

        when(userDetailsRepository.findByRefId("testRefId")).thenReturn(userDetails);
        when(artistsRepository.findByUserDetailsId(1))
                .thenReturn(List.of(existingArtist), List.of(newArtist));

        List<Artists> result = artistsService.updateAndFetchArtists(user, List.of(newArtist));

        assertEquals(1, result.size());
        assertEquals("New Artist", result.get(0).getName());

        verify(artistsRepository, times(1)).deleteAll(any());
        verify(artistsRepository, times(1)).save(any(Artists.class));
        verify(artistsRepository, times(2)).findByUserDetailsId(1);
    }
}