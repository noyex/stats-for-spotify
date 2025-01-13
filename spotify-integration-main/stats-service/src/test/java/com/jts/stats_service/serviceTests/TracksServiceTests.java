package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.Tracks;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.TracksRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.TracksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TracksServiceTests {

    @Mock
    private TracksRepository tracksRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private TracksService tracksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTrackMapper() {
        SavedTrack savedTrack = mock(SavedTrack.class);
        Track track = mock(Track.class);
        ArtistSimplified artist = mock(ArtistSimplified.class);
        AlbumSimplified album = mock(AlbumSimplified.class);

        when(savedTrack.getTrack()).thenReturn(track);
        when(track.getName()).thenReturn("Test Track");
        when(track.getArtists()).thenReturn(new ArtistSimplified[]{artist});
        when(artist.getName()).thenReturn("Test Artist");
        when(track.getAlbum()).thenReturn(album);
        when(album.getName()).thenReturn("Test Album");

        List<Tracks> result = tracksService.trackMapper(new SavedTrack[]{savedTrack});

        assertEquals(1, result.size());
        assertEquals("Test Track", result.get(0).getName());
        assertEquals("Test Artist", result.get(0).getArtistName());
        assertEquals("Test Album", result.get(0).getAlbumName());
    }

    @Test
    void testUpdateAndFetchTracksSuccess() {
        UserDetails user = new UserDetails();
        user.setRefId("testRefId");

        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setRefId("testRefId");

        Tracks newTrack = new Tracks();
        newTrack.setName("New Track");
        newTrack.setArtistName("New Artist");
        newTrack.setAlbumName("New Album");

        when(userDetailsRepository.findByRefId("testRefId")).thenReturn(userDetails);
        when(tracksRepository.findByUserDetailsId(1)).thenReturn(Collections.emptyList(), List.of(newTrack));

        List<Tracks> updatedTracks = tracksService.updateAndFetchTracks(user, List.of(newTrack));

        assertEquals(1, updatedTracks.size());
        assertEquals("New Track", updatedTracks.get(0).getName());
        assertEquals("New Artist", updatedTracks.get(0).getArtistName());
        assertEquals("New Album", updatedTracks.get(0).getAlbumName());

        verify(tracksRepository, times(1)).deleteAll(any());
        verify(tracksRepository, times(1)).save(any(Tracks.class));
        verify(tracksRepository, times(2)).findByUserDetailsId(1); // Two calls: before and after saving
    }

    @Test
    void testUpdateAndFetchTracksUserNotFound() {
        UserDetails user = new UserDetails();
        user.setRefId("testRefId");

        when(userDetailsRepository.findByRefId("testRefId")).thenReturn(null);

        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> tracksService.updateAndFetchTracks(user, List.of(new Tracks())));

        assertEquals("User not found", exception.getMessage());
        verify(tracksRepository, never()).save(any());
    }
}