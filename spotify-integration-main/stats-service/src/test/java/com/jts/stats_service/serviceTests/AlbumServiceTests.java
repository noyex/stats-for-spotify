package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.Albums;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.AlbumsRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AlbumServiceTests {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private AlbumsRepository albumsRepository;

    @InjectMocks
    private AlbumService albumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAlbumMapper() {
        SavedAlbum savedAlbum = mock(SavedAlbum.class);
        Album album = mock(Album.class);
        ArtistSimplified artist = mock(ArtistSimplified.class);
        Paging<TrackSimplified> tracks = mock(Paging.class);

        when(savedAlbum.getAlbum()).thenReturn(album);
        when(album.getName()).thenReturn("Test Album");
        when(album.getArtists()).thenReturn(new ArtistSimplified[]{artist});
        when(artist.getName()).thenReturn("Test Artist");
        when(album.getTracks()).thenReturn(tracks);
        when(tracks.getTotal()).thenReturn(10);

        List<Albums> albumsList = albumService.albumMapper(new SavedAlbum[]{savedAlbum});

        assertEquals(1, albumsList.size());
        assertEquals("Test Album", albumsList.get(0).getName());
        assertEquals("Test Artist", albumsList.get(0).getArtistName());
        assertEquals(10, albumsList.get(0).getNumberOfTracks());
    }

    @Test
    void testUpdateAndFetchAlbums() {
        UserDetails user = new UserDetails();
        user.setRefId("testRefId");

        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setRefId("testRefId");

        Albums newAlbum = new Albums();
        newAlbum.setName("New Album");
        newAlbum.setArtistName("New Artist");

        when(userDetailsRepository.findByRefId("testRefId")).thenReturn(userDetails);
        when(albumsRepository.findByUserDetailsId(1)).thenReturn(List.of());

        List<Albums> updatedAlbums = albumService.updateAndFetchAlbums(user, List.of(newAlbum));

        assertEquals(1, updatedAlbums.size());
        assertEquals("New Album", updatedAlbums.get(0).getName());
        assertEquals("New Artist", updatedAlbums.get(0).getArtistName());

        verify(albumsRepository, times(1)).deleteAll(any());
        verify(albumsRepository, times(1)).save(any(Albums.class));
        verify(albumsRepository, times(1)).findByUserDetailsId(1);
    }
}