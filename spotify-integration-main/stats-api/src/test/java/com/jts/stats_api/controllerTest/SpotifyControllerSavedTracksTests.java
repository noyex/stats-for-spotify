package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_service.service.TracksService;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.entity.Tracks;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerSavedTracksTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private TracksService tracksService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetUsersSavedTracksRequest getUsersSavedTracksRequest;

    @Mock
    private Paging<SavedTrack> trackPaging;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUserSavedTracksSuccess() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId("testUserId");
        when(controllerService.getUserDetails("testUserId")).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser("testUserId")).thenReturn(spotifyApi);

        GetUsersSavedTracksRequest.Builder mockBuilder = mock(GetUsersSavedTracksRequest.Builder.class);
        when(spotifyApi.getUsersSavedTracks()).thenReturn(mockBuilder);
        when(mockBuilder.limit(50)).thenReturn(mockBuilder);
        when(mockBuilder.market(CountryCode.PL)).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(getUsersSavedTracksRequest);

        SavedTrack[] savedTracks = new SavedTrack[2];
        when(getUsersSavedTracksRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(savedTracks);

        List<Tracks> mappedTracks = List.of(new Tracks(), new Tracks());
        when(tracksService.trackMapper(savedTracks)).thenReturn(mappedTracks);

        SavedTrack[] result = spotifyController.getCurrentUserSavedTracks("testUserId");

        assertNotNull(result);
        assertEquals(2, result.length);
        verify(tracksService, times(1)).updateAndFetchTracks(userDetails, mappedTracks);
    }

    @Test
    void testGetCurrentUserSavedTracksException() {
        when(controllerService.getUserDetails("testUserId")).thenThrow(new RuntimeException("User not found"));

        SavedTrack[] result = spotifyController.getCurrentUserSavedTracks("testUserId");

        assertNotNull(result);
        assertEquals(0, result.length);
        verify(tracksService, never()).trackMapper(any());
        verify(tracksService, never()).updateAndFetchTracks(any(), any());
    }
}