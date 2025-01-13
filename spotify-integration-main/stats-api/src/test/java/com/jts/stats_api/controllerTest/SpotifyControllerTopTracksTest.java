package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SpotifyControllerTopTracksTest {

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetUsersTopTracksRequest getUsersTopTracksRequest;

    @Mock
    private Paging<Track> trackPaging;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // top tracks tests

    @Test
    public void testGetUserTopTracksMediumSuccess() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("medium_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(new Track[]{});

        Track[] result = spotifyController.getUserTopTracksMedium("userId");

        assertArrayEquals(new Track[]{}, result);
    }

    @Test
    public void testGetUserTopTracksMediumException() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("medium_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenThrow(new RuntimeException("Test Exception"));

        Track[] result = spotifyController.getUserTopTracksMedium("userId");

        assertEquals(0, result.length);
    }

    @Test
    public void testGetUserTopTracksShortSuccess() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("short_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(new Track[]{});

        Track[] result = spotifyController.getUserTopTracksShort("userId");

        assertArrayEquals(new Track[]{}, result);
    }

    @Test
    public void testGetUserTopTracksShortException() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("short_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenThrow(new RuntimeException("Test Exception"));

        Track[] result = spotifyController.getUserTopTracksShort("userId");

        assertEquals(0, result.length);
    }

    @Test
    public void testGetUserTopTracksLongSuccess() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("long_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(new Track[]{});

        Track[] result = spotifyController.getUserTopTracksLong("userId");

        assertArrayEquals(new Track[]{}, result);
    }

    @Test
    public void testGetUserTopTracksLongException() throws Exception {
        when(controllerService.getSpotifyApiForUser(anyString())).thenReturn(spotifyApi);

        GetUsersTopTracksRequest.Builder builderMock = mock(GetUsersTopTracksRequest.Builder.class);
        when(spotifyApi.getUsersTopTracks()).thenReturn(builderMock);
        when(builderMock.time_range("long_term")).thenReturn(builderMock);
        when(builderMock.limit(48)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getUsersTopTracksRequest);

        when(getUsersTopTracksRequest.execute()).thenThrow(new RuntimeException("Test Exception"));

        Track[] result = spotifyController.getUserTopTracksLong("userId");

        assertEquals(0, result.length);
    }
}