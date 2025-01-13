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
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.requests.data.follow.FollowArtistsOrUsersRequest;
import se.michaelthelin.spotify.requests.data.follow.UnfollowArtistsOrUsersRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SpotifyControllerArtistFollowTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private FollowArtistsOrUsersRequest followRequest;

    @Mock
    private FollowArtistsOrUsersRequest.Builder followBuilder;

    @Mock
    private UnfollowArtistsOrUsersRequest unfollowRequest;

    @Mock
    private UnfollowArtistsOrUsersRequest.Builder unfollowBuilder;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFollowArtistForCurrentUserSuccess() throws Exception {
        String userId = "testUserId";
        String artistId = "testArtistId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.followArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId})).thenReturn(followBuilder);
        when(followBuilder.build()).thenReturn(followRequest);

        doAnswer(invocation -> null).when(followRequest).execute();

        spotifyController.followArtistForCurrentUser(userId, artistId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).followArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId});
        verify(followRequest, times(1)).execute();
    }

    @Test
    void testFollowArtistForCurrentUserException() throws Exception {
        String userId = "testUserId";
        String artistId = "testArtistId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.followArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId})).thenReturn(followBuilder);
        when(followBuilder.build()).thenReturn(followRequest);

        doThrow(new RuntimeException("Test Exception")).when(followRequest).execute();

        assertThrows(RuntimeException.class, () -> spotifyController.followArtistForCurrentUser(userId, artistId));

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).followArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId});
        verify(followRequest, times(1)).execute();
    }

    @Test
    void testUnfollowArtistForCurrentUserSuccess() throws Exception {
        String userId = "testUserId";
        String artistId = "testArtistId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.unfollowArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId})).thenReturn(unfollowBuilder);
        when(unfollowBuilder.build()).thenReturn(unfollowRequest);

        doAnswer(invocation -> null).when(unfollowRequest).execute();

        spotifyController.unfollowArtistForCurrentUser(userId, artistId);

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).unfollowArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId});
        verify(unfollowRequest, times(1)).execute();
    }

    @Test
    void testUnfollowArtistForCurrentUserException() throws Exception {
        String userId = "testUserId";
        String artistId = "testArtistId";

        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.unfollowArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId})).thenReturn(unfollowBuilder);
        when(unfollowBuilder.build()).thenReturn(unfollowRequest);

        doThrow(new RuntimeException("Test Exception")).when(unfollowRequest).execute();

        assertThrows(RuntimeException.class, () -> spotifyController.unfollowArtistForCurrentUser(userId, artistId));

        verify(controllerService, times(1)).getUserDetails(userId);
        verify(controllerService, times(1)).getSpotifyApiForUser(userId);
        verify(spotifyApi, times(1)).unfollowArtistsOrUsers(ModelObjectType.ARTIST, new String[]{artistId});
        verify(unfollowRequest, times(1)).execute();
    }
}