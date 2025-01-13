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
import se.michaelthelin.spotify.requests.data.library.RemoveAlbumsForCurrentUserRequest;
import se.michaelthelin.spotify.requests.data.library.RemoveUsersSavedTracksRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SpotifyControllerRemoveTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private RemoveUsersSavedTracksRequest.Builder removeTracksBuilder;

    @Mock
    private RemoveAlbumsForCurrentUserRequest.Builder removeAlbumsBuilder;

    @Mock
    private RemoveUsersSavedTracksRequest removeTracksRequest;

    @Mock
    private RemoveAlbumsForCurrentUserRequest removeAlbumsRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRemoveSavedTrackForCurrentUserSuccess() throws Exception {
        String userId = "testUserId";
        String trackId = "testTrackId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.removeUsersSavedTracks(trackId)).thenReturn(removeTracksBuilder);
        when(removeTracksBuilder.build()).thenReturn(removeTracksRequest);

        doAnswer(invocation -> null).when(removeTracksRequest).execute();

        spotifyController.removeSavedTrackForCurrentUser(userId, trackId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).removeUsersSavedTracks(trackId);
        verify(removeTracksRequest, times(1)).execute();
    }

    @Test
    void testRemoveSavedTrackForCurrentUserException() throws Exception {
        String userId = "testUserId";
        String trackId = "testTrackId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.removeUsersSavedTracks(trackId)).thenReturn(removeTracksBuilder);
        when(removeTracksBuilder.build()).thenReturn(removeTracksRequest);

        doThrow(new RuntimeException("Test Exception")).when(removeTracksRequest).execute();

        assertThrows(RuntimeException.class, () -> spotifyController.removeSavedTrackForCurrentUser(userId, trackId));

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).removeUsersSavedTracks(trackId);
        verify(removeTracksRequest, times(1)).execute();
    }

    @Test
    void testRemoveSavedAlbumForCurrentUserSuccess() throws Exception {
        String userId = "testUserId";
        String albumId = "testAlbumId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.removeAlbumsForCurrentUser(albumId)).thenReturn(removeAlbumsBuilder);
        when(removeAlbumsBuilder.build()).thenReturn(removeAlbumsRequest);

        doAnswer(invocation -> null).when(removeAlbumsRequest).execute();

        spotifyController.removeSavedAlbumForCurrentUser(userId, albumId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).removeAlbumsForCurrentUser(albumId);
        verify(removeAlbumsRequest, times(1)).execute();
    }

    @Test
    void testRemoveSavedAlbumForCurrentUserException() throws Exception {
        String userId = "testUserId";
        String albumId = "testAlbumId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.removeAlbumsForCurrentUser(albumId)).thenReturn(removeAlbumsBuilder);
        when(removeAlbumsBuilder.build()).thenReturn(removeAlbumsRequest);

        doThrow(new RuntimeException("Test Exception")).when(removeAlbumsRequest).execute();

        assertThrows(RuntimeException.class, () -> spotifyController.removeSavedAlbumForCurrentUser(userId, albumId));

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).removeAlbumsForCurrentUser(albumId);
        verify(removeAlbumsRequest, times(1)).execute();
    }
}