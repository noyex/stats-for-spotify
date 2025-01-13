package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_data.entity.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.data.library.SaveAlbumsForCurrentUserRequest;
import se.michaelthelin.spotify.requests.data.library.SaveTracksForUserRequest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class SpotifyControllerSaveTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private SaveTracksForUserRequest.Builder saveTracksBuilder;

    @Mock
    private SaveAlbumsForCurrentUserRequest.Builder saveAlbumsBuilder;

    @Mock
    private SaveTracksForUserRequest saveTracksRequest;

    @Mock
    private SaveAlbumsForCurrentUserRequest saveAlbumsRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTrackForCurrentUserSuccess() throws Exception {
        String userId = "testUser";
        String trackId = "testTrack";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.saveTracksForUser(trackId)).thenReturn(saveTracksBuilder);
        when(saveTracksBuilder.build()).thenReturn(saveTracksRequest);

        doAnswer(invocation -> null).when(saveTracksRequest).execute();

        spotifyController.saveTrackForCurrentUser(userId, trackId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).saveTracksForUser(trackId);
        verify(saveTracksRequest, times(1)).execute();
    }

    @Test
    void testSaveTrackForCurrentUserException() throws Exception {
        String userId = "testUser";
        String trackId = "testTrack";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.saveTracksForUser(trackId)).thenReturn(saveTracksBuilder);
        when(saveTracksBuilder.build()).thenReturn(saveTracksRequest);

        doThrow(new RuntimeException("Test Exception")).when(saveTracksRequest).execute();

        try {
            spotifyController.saveTrackForCurrentUser(userId, trackId);
        } catch (RuntimeException e) {
            assertEquals("Error saving track for current user", e.getMessage());
        }

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).saveTracksForUser(trackId);
        verify(saveTracksRequest, times(1)).execute();
    }

    @Test
    void testSaveAlbumForCurrentUserSuccess() throws Exception {
        String userId = "testUser";
        String albumId = "testAlbum";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.saveAlbumsForCurrentUser(albumId)).thenReturn(saveAlbumsBuilder);
        when(saveAlbumsBuilder.build()).thenReturn(saveAlbumsRequest);

        doAnswer(invocation -> null).when(saveAlbumsRequest).execute();

        spotifyController.saveAlbumForCurrentUser(userId, albumId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).saveAlbumsForCurrentUser(albumId);
        verify(saveAlbumsRequest, times(1)).execute();
    }

    @Test
    void testSaveAlbumForCurrentUserException() throws Exception {
        String userId = "testUser";
        String albumId = "testAlbum";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.saveAlbumsForCurrentUser(albumId)).thenReturn(saveAlbumsBuilder);
        when(saveAlbumsBuilder.build()).thenReturn(saveAlbumsRequest);

        doThrow(new RuntimeException("Test Exception")).when(saveAlbumsRequest).execute();

        try {
            spotifyController.saveAlbumForCurrentUser(userId, albumId);
        } catch (RuntimeException e) {
            assertEquals("Error saving album for current user", e.getMessage());
        }

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).saveAlbumsForCurrentUser(albumId);
        verify(saveAlbumsRequest, times(1)).execute();
    }
}