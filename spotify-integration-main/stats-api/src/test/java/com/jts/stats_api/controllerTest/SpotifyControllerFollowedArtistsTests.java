package com.jts.stats_api.controllerTest;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_api.service.ControllerService;
import com.jts.stats_data.entity.Artists;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_service.service.ArtistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerFollowedArtistsTests {

    @Mock
    private ControllerService controllerService;

    @Mock
    private ArtistsService artistsService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private GetUsersFollowedArtistsRequest getUsersFollowedArtistsRequest;

    @InjectMocks
    private SpotifyController spotifyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrentUserFollowedArtistsSuccess() throws Exception {
        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setRefId("testUserId");

        when(controllerService.getUserDetails("testUserId")).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser("testUserId")).thenReturn(spotifyApi);

        GetUsersFollowedArtistsRequest.Builder requestBuilder = mock(GetUsersFollowedArtistsRequest.Builder.class);
        when(spotifyApi.getUsersFollowedArtists(ModelObjectType.ARTIST)).thenReturn(requestBuilder);
        when(requestBuilder.limit(50)).thenReturn(requestBuilder);
        when(requestBuilder.build()).thenReturn(getUsersFollowedArtistsRequest);

        PagingCursorbased<Artist> mockPaging = mock(PagingCursorbased.class);
        when(getUsersFollowedArtistsRequest.execute()).thenReturn(mockPaging);

        Artist artist = mock(Artist.class);
        when(mockPaging.getItems()).thenReturn(new Artist[]{artist});

        when(artistsService.artistMapper(any())).thenReturn(List.of(new Artists()));
        when(artistsService.updateAndFetchArtists(any(), any())).thenReturn(List.of(new Artists()));

        Artist[] result = spotifyController.getCurrentUserFollowedArtists("testUserId");

        assertNotNull(result);
        assertEquals(1, result.length);
        verify(artistsService).artistMapper(any());
        verify(artistsService).updateAndFetchArtists(any(), any());
    }

    @Test
    void testGetCurrentUserFollowedArtistsException() throws Exception {
        when(controllerService.getUserDetails("testUserId")).thenThrow(new RuntimeException("User not found"));

        Artist[] result = spotifyController.getCurrentUserFollowedArtists("testUserId");

        assertNull(result);
        verify(artistsService, never()).artistMapper(any());
        verify(artistsService, never()).updateAndFetchArtists(any(), any());
    }
}