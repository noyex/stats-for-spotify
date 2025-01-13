package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_service.service.RecentlyPlayedService;
import com.jts.stats_data.entity.PlaybackHistory;
import com.jts.stats_data.entity.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerRecentlyPlayedTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private RecentlyPlayedService recentlyPlayedService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetCurrentUsersRecentlyPlayedTracksRequest.Builder recentlyPlayedBuilder;

    @Mock
    private GetCurrentUsersRecentlyPlayedTracksRequest recentlyPlayedRequest;

    @Mock
    private PagingCursorbased<PlayHistory> trackPaging;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserPlaybackHistorySuccess() throws Exception {
        String userId = "testUser";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        PlayHistory[] playHistories = new PlayHistory[1];
        List<PlaybackHistory> playbackHistories = Collections.emptyList();

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersRecentlyPlayedTracks()).thenReturn(recentlyPlayedBuilder);
        when(recentlyPlayedBuilder.limit(48)).thenReturn(recentlyPlayedBuilder);
        when(recentlyPlayedBuilder.before(any(Date.class))).thenReturn(recentlyPlayedBuilder);
        when(recentlyPlayedBuilder.build()).thenReturn(recentlyPlayedRequest);
        when(recentlyPlayedRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(playHistories);
        when(recentlyPlayedService.mapRecentlyPlayedToPlaybackHistory(playHistories)).thenReturn(playbackHistories);

        PlayHistory[] result = spotifyController.getUserPlaybackHistory(userId);

        assertNotNull(result);
        assertArrayEquals(playHistories, result);
        verify(recentlyPlayedService, times(1)).updateAndFetchRecentlyPlayed(userDetails, playbackHistories);
    }

    @Test
    void testGetUserPlaybackHistoryException() throws Exception {
        String userId = "testUser";

        when(controllerService.getUserDetails(userId)).thenThrow(new RuntimeException("User not found"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spotifyController.getUserPlaybackHistory(userId);
        });

        assertEquals("Failed to fetch recently played tracks.", exception.getMessage());
        verify(recentlyPlayedService, never()).updateAndFetchRecentlyPlayed(any(), any());
    }
}