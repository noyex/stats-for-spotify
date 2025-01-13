package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.Playlists;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.PlaylistsRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.PlaylistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PlaylistsServiceTests {

    @Mock
    private PlaylistsRepository playlistsRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private PlaylistsService playlistsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaylistMapper() {
        PlaylistSimplified playlistSimplified = mock(PlaylistSimplified.class);
        PlaylistTracksInformation tracksInformation = mock(PlaylistTracksInformation.class);

        when(playlistSimplified.getName()).thenReturn("Test Playlist");
        when(playlistSimplified.getTracks()).thenReturn(tracksInformation);
        when(tracksInformation.getTotal()).thenReturn(20);

        List<Playlists> playlists = playlistsService.playlistMapper(new PlaylistSimplified[]{playlistSimplified});

        assertEquals(1, playlists.size());
        assertEquals("Test Playlist", playlists.get(0).getName());
        assertEquals(20, playlists.get(0).getNumberOfTracks());
    }

    @Test
    void testUpdateAndFetchPlaylists() {
        UserDetails user = new UserDetails();
        user.setRefId("testRefId");

        UserDetails userDetails = new UserDetails();
        userDetails.setId(1);
        userDetails.setRefId("testRefId");

        Playlists existingPlaylist = new Playlists();
        existingPlaylist.setName("Existing Playlist");
        existingPlaylist.setNumberOfTracks(10);

        Playlists newPlaylist = new Playlists();
        newPlaylist.setName("New Playlist");
        newPlaylist.setNumberOfTracks(15);

        when(userDetailsRepository.findByRefId("testRefId")).thenReturn(userDetails);
        when(playlistsRepository.findByUserDetailsId(1))
                .thenReturn(List.of(existingPlaylist), List.of(newPlaylist));

        List<Playlists> updatedPlaylists = playlistsService.updateAndFetchPlaylists(user, List.of(newPlaylist));

        assertEquals(1, updatedPlaylists.size());
        assertEquals("New Playlist", updatedPlaylists.get(0).getName());
        assertEquals(15, updatedPlaylists.get(0).getNumberOfTracks());

        verify(playlistsRepository, times(1)).deleteAll(any());
        verify(playlistsRepository, times(1)).save(any(Playlists.class));
        verify(playlistsRepository, times(2)).findByUserDetailsId(1); // Przed i po zapisaniu
    }
}