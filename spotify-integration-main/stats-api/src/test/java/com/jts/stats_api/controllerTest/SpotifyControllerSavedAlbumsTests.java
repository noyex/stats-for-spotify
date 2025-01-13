package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_data.entity.Albums;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_service.service.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.SavedAlbum;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerSavedAlbumsTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private AlbumService albumService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetCurrentUsersSavedAlbumsRequest getCurrentUsersSavedAlbumsRequest;

    @Mock
    private Paging<SavedAlbum> pagingMock;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCurrentUserSavedAlbumSuccess() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId("testUserId");

        SavedAlbum[] savedAlbums = new SavedAlbum[1];
        savedAlbums[0] = mock(SavedAlbum.class);

        List<Albums> mappedAlbums = Collections.singletonList(new Albums());

        GetCurrentUsersSavedAlbumsRequest.Builder builderMock = mock(GetCurrentUsersSavedAlbumsRequest.Builder.class);
        when(controllerService.getUserDetails("testUserId")).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser("testUserId")).thenReturn(spotifyApi);
        when(spotifyApi.getCurrentUsersSavedAlbums()).thenReturn(builderMock);
        when(builderMock.limit(50)).thenReturn(builderMock);
        when(builderMock.offset(0)).thenReturn(builderMock);
        when(builderMock.build()).thenReturn(getCurrentUsersSavedAlbumsRequest);

        when(getCurrentUsersSavedAlbumsRequest.execute()).thenReturn(pagingMock);
        when(pagingMock.getItems()).thenReturn(savedAlbums);
        when(albumService.albumMapper(savedAlbums)).thenReturn(mappedAlbums);

        SavedAlbum[] result = spotifyController.getCurrentUserSavedAlbum("testUserId");

        assertNotNull(result);
        assertEquals(1, result.length);
        verify(albumService, times(1)).updateAndFetchAlbums(userDetails, mappedAlbums);
    }

    @Test
    void testGetCurrentUserSavedAlbumException() throws Exception {
        when(controllerService.getUserDetails("testUserId")).thenThrow(new RuntimeException("User not found"));

        SavedAlbum[] result = spotifyController.getCurrentUserSavedAlbum("testUserId");

        assertNotNull(result);
        assertEquals(0, result.length);
        verify(albumService, never()).updateAndFetchAlbums(any(), any());
    }
}
