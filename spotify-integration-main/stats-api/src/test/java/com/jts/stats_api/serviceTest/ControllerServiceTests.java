package com.jts.stats_api.serviceTest;

import com.jts.stats_api.service.ControllerService;
import com.jts.stats_client.config.SpotifyConfiguration;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerServiceTests {

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @Mock
    private SpotifyConfiguration spotifyConfiguration;

    @InjectMocks
    private ControllerService controllerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetSpotifyApiForUser_UserExists() {
        String userId = "testUser";
        UserDetails userDetails = new UserDetails();
        userDetails.setAccessToken("testAccessToken");
        userDetails.setRefreshToken("testRefreshToken");

        when(userDetailsRepository.findByRefId(userId)).thenReturn(userDetails);
        SpotifyApi spotifyApiMock = mock(SpotifyApi.class);
        when(spotifyConfiguration.getSpotifyObject()).thenReturn(spotifyApiMock);

        SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

        assertNotNull(spotifyApi);
        verify(spotifyApiMock).setAccessToken("testAccessToken");
        verify(spotifyApiMock).setRefreshToken("testRefreshToken");
    }

    @Test
    public void testGetSpotifyApiForUser_UserNotFound() {
        String userId = "testUser";

        when(userDetailsRepository.findByRefId(userId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            controllerService.getSpotifyApiForUser(userId);
        });
    }

    @Test
    public void testGetUserDetails_UserExists() {
        String userId = "testUser";
        UserDetails userDetails = new UserDetails();

        when(userDetailsRepository.findByRefId(userId)).thenReturn(userDetails);

        UserDetails result = controllerService.getUserDetails(userId);

        assertNotNull(result);
        assertEquals(userDetails, result);
    }

    @Test
    public void testGetUserDetails_UserNotFound() {
        String userId = "testUser";

        when(userDetailsRepository.findByRefId(userId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            controllerService.getUserDetails(userId);
        });
    }
}