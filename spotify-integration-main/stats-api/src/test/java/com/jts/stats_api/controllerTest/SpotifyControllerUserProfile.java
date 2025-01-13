package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.User;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerUserProfileTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetCurrentUsersProfileRequest.Builder getCurrentUsersProfileBuilder;

    @Mock
    private GetCurrentUsersProfileRequest getCurrentUsersProfileRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUserProfileSuccess() throws Exception {
        String userId = "testUser";
        User mockUser = mock(User.class);

        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(getCurrentUsersProfileBuilder);
        when(getCurrentUsersProfileBuilder.build()).thenReturn(getCurrentUsersProfileRequest);
        when(getCurrentUsersProfileRequest.execute()).thenReturn(mockUser);

        User result = spotifyController.getCurrentUserProfile(userId);

        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(spotifyApi, times(1)).getCurrentUsersProfile();
        verify(getCurrentUsersProfileRequest, times(1)).execute();
    }

    @Test
    void testGetCurrentUserProfileException() throws Exception {
        String userId = "testUser";

        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersProfile()).thenReturn(getCurrentUsersProfileBuilder);
        when(getCurrentUsersProfileBuilder.build()).thenReturn(getCurrentUsersProfileRequest);
        when(getCurrentUsersProfileRequest.execute()).thenThrow(new RuntimeException("Test Exception"));

        User result = spotifyController.getCurrentUserProfile(userId);

        assertNull(result);
        verify(spotifyApi, times(1)).getCurrentUsersProfile();
        verify(getCurrentUsersProfileRequest, times(1)).execute();
    }
}