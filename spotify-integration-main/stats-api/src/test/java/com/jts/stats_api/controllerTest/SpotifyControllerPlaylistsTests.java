package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_service.service.PlaylistsService;
import com.jts.stats_data.entity.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest.Builder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class SpotifyControllerPlaylistsTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private PlaylistsService playlistsService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUserPlaylistsSuccess() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId("testUserId");
        when(controllerService.getUserDetails("testUserId")).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser("testUserId")).thenReturn(spotifyApi);

        Builder mockBuilder = mock(Builder.class);
        when(spotifyApi.getListOfCurrentUsersPlaylists()).thenReturn(mockBuilder);
        when(mockBuilder.limit(50)).thenReturn(mockBuilder);
        when(mockBuilder.offset(0)).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(getListOfCurrentUsersPlaylistsRequest);

        PlaylistSimplified[] mockPlaylistsArray = new PlaylistSimplified[2];
        Paging<PlaylistSimplified> mockPaging = mock(Paging.class);
        when(mockPaging.getItems()).thenReturn(mockPlaylistsArray);
        when(getListOfCurrentUsersPlaylistsRequest.execute()).thenReturn(mockPaging);

        when(playlistsService.playlistMapper(mockPlaylistsArray)).thenReturn(List.of());

        PlaylistSimplified[] result = spotifyController.getCurrentUserPlaylists("testUserId");

        assertNotNull(result);
        assertEquals(2, result.length);
        verify(playlistsService, times(1)).updateAndFetchPlaylists(userDetails, List.of());
    }

    @Test
    void testGetCurrentUserPlaylistsException() {
        when(controllerService.getUserDetails("testUserId")).thenThrow(new RuntimeException("User not found"));

        PlaylistSimplified[] result = spotifyController.getCurrentUserPlaylists("testUserId");

        assertNotNull(result);
        assertEquals(0, result.length);
        verify(playlistsService, never()).updateAndFetchPlaylists(any(), any());
    }
}