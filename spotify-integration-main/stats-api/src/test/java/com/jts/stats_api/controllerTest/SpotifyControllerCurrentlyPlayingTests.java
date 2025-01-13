package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerCurrentlyPlayingTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetUsersCurrentlyPlayingTrackRequest.Builder currentlyPlayingTrackBuilder;

    @Mock
    private GetUsersCurrentlyPlayingTrackRequest currentlyPlayingTrackRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserCurrentlyPlayingTrackSuccess() throws Exception {
        String userId = "testUser";
        CurrentlyPlaying mockCurrentlyPlaying = mock(CurrentlyPlaying.class);

        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.market(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.additionalTypes(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.build()).thenReturn(currentlyPlayingTrackRequest);
        when(currentlyPlayingTrackRequest.execute()).thenReturn(mockCurrentlyPlaying);
        when(mockCurrentlyPlaying.getIs_playing()).thenReturn(true);

        CurrentlyPlaying result = spotifyController.getUserCurrentlyPlayingTrack(userId);

        assertNotNull(result);
        assertEquals(mockCurrentlyPlaying, result);
        verify(spotifyApi, times(1)).getUsersCurrentlyPlayingTrack();
        verify(currentlyPlayingTrackRequest, times(1)).execute();
    }

    @Test
    void testGetUserCurrentlyPlayingTrackNoTrackPlaying() throws Exception {
        String userId = "testUser";
        CurrentlyPlaying mockCurrentlyPlaying = mock(CurrentlyPlaying.class);

        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.market(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.additionalTypes(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.build()).thenReturn(currentlyPlayingTrackRequest);
        when(currentlyPlayingTrackRequest.execute()).thenReturn(mockCurrentlyPlaying);
        when(mockCurrentlyPlaying.getIs_playing()).thenReturn(false);

        CurrentlyPlaying result = spotifyController.getUserCurrentlyPlayingTrack(userId);

        assertNull(result);
        verify(spotifyApi, times(1)).getUsersCurrentlyPlayingTrack();
        verify(currentlyPlayingTrackRequest, times(1)).execute();
    }

    @Test
    void testGetUserCurrentlyPlayingTrackException() throws Exception {
        String userId = "testUser";

        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getUsersCurrentlyPlayingTrack()).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.market(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.additionalTypes(any())).thenReturn(currentlyPlayingTrackBuilder);
        when(currentlyPlayingTrackBuilder.build()).thenReturn(currentlyPlayingTrackRequest);
        when(currentlyPlayingTrackRequest.execute()).thenThrow(new RuntimeException("Test Exception"));

        CurrentlyPlaying result = spotifyController.getUserCurrentlyPlayingTrack(userId);

        assertNull(result);
        verify(spotifyApi, times(1)).getUsersCurrentlyPlayingTrack();
        verify(currentlyPlayingTrackRequest, times(1)).execute();
    }
}