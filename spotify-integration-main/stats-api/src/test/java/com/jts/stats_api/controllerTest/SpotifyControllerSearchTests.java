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
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyControllerSearchTests {

    @InjectMocks
    private SpotifyController spotifyController;

    @Mock
    private ControllerService controllerService;

    @Mock
    private SpotifyApi spotifyApi;

    @Mock
    private SearchAlbumsRequest.Builder searchAlbumsBuilder;

    @Mock
    private SearchArtistsRequest.Builder searchArtistsBuilder;

    @Mock
    private SearchTracksRequest.Builder searchTracksBuilder;

    @Mock
    private SearchAlbumsRequest searchAlbumsRequest;

    @Mock
    private SearchArtistsRequest searchArtistsRequest;

    @Mock
    private SearchTracksRequest searchTracksRequest;

    @Mock
    private Paging<AlbumSimplified> albumPaging;

    @Mock
    private Paging<Artist> artistPaging;

    @Mock
    private Paging<Track> trackPaging;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchAlbumsSuccess() throws Exception {
        String userId = "testUser";
        String query = "testAlbum";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchAlbums(query)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.limit(10)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.offset(0)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.market(any())).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.build()).thenReturn(searchAlbumsRequest);
        when(searchAlbumsRequest.execute()).thenReturn(albumPaging);
        when(albumPaging.getItems()).thenReturn(new AlbumSimplified[0]);

        AlbumSimplified[] result = spotifyController.searchAlbums(userId, query);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testSearchAlbumsException() throws Exception {
        String userId = "testUser";
        String query = "testAlbum";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchAlbums(query)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.limit(10)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.offset(0)).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.market(any())).thenReturn(searchAlbumsBuilder);
        when(searchAlbumsBuilder.build()).thenReturn(searchAlbumsRequest);
        when(searchAlbumsRequest.execute()).thenThrow(new RuntimeException("Test exception during search albums"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spotifyController.searchAlbums(userId, query);
        });

        assertEquals("Error searching albums", exception.getMessage());
    }

    @Test
    void testSearchArtistsSuccess() throws Exception {
        String userId = "testUser";
        String query = "testArtist";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchArtists(query)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.limit(10)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.offset(0)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.market(any())).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.build()).thenReturn(searchArtistsRequest);
        when(searchArtistsRequest.execute()).thenReturn(artistPaging);
        when(artistPaging.getItems()).thenReturn(new Artist[0]);

        Artist[] result = spotifyController.searchArtists(userId, query);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
    @Test
    void testSearchArtistsException() throws Exception {
        String userId = "testUser";
        String query = "testArtist";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchArtists(query)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.limit(10)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.offset(0)).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.market(any())).thenReturn(searchArtistsBuilder);
        when(searchArtistsBuilder.build()).thenReturn(searchArtistsRequest);
        when(searchArtistsRequest.execute()).thenThrow(new RuntimeException("Test exception during search artists"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spotifyController.searchArtists(userId, query);
        });

        assertEquals("Error searching artists", exception.getMessage());
    }

    @Test
    void testSearchTracksSuccess() throws Exception {
        String userId = "testUser";
        String query = "testTrack";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchTracks(query)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.limit(10)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.offset(0)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.market(any())).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.build()).thenReturn(searchTracksRequest);
        when(searchTracksRequest.execute()).thenReturn(trackPaging);
        when(trackPaging.getItems()).thenReturn(new Track[0]);

        Track[] result = spotifyController.searchTracks(userId, query);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    void testSearchTracksException() throws Exception {
        String userId = "testUser";
        String query = "testTrack";
        UserDetails userDetails = new UserDetails();
        userDetails.setRefId(userId);

        when(controllerService.getUserDetails(userId)).thenReturn(userDetails);
        when(controllerService.getSpotifyApiForUser(userId)).thenReturn(spotifyApi);
        when(spotifyApi.searchTracks(query)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.limit(10)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.offset(0)).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.market(any())).thenReturn(searchTracksBuilder);
        when(searchTracksBuilder.build()).thenReturn(searchTracksRequest);
        when(searchTracksRequest.execute()).thenThrow(new RuntimeException("Test exception during search tracks"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spotifyController.searchTracks(userId, query);
        });

        assertEquals("Error searching tracks", exception.getMessage());
    }
}