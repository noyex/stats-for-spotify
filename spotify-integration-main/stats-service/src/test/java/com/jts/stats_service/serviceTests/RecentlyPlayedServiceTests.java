package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.PlaybackHistory;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.PlaybackHistoryRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.RecentlyPlayedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecentlyPlayedServiceTests {

    @Mock
    private PlaybackHistoryRepository playbackHistoryRepository;

    @Mock
    private UserDetailsRepository userDetailsRepository;

    @InjectMocks
    private RecentlyPlayedService recentlyPlayedService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapRecentlyPlayedToPlaybackHistory() {
        PlayHistory playHistory = mock(PlayHistory.class);
        TrackSimplified track = mock(TrackSimplified.class);
        ArtistSimplified artist = mock(ArtistSimplified.class);

        when(playHistory.getTrack()).thenReturn(track);
        when(track.getName()).thenReturn("Test Track");
        when(track.getArtists()).thenReturn(new ArtistSimplified[]{artist});
        when(artist.getName()).thenReturn("Test Artist");
        when(playHistory.getPlayedAt()).thenReturn(java.util.Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));

        List<PlaybackHistory> result = recentlyPlayedService.mapRecentlyPlayedToPlaybackHistory(new PlayHistory[]{playHistory});

        assertEquals(1, result.size());
        assertEquals("Test Track", result.get(0).getTrackName());
        assertEquals("Test Artist", result.get(0).getArtistName());
    }

    @Test
    void testUpdateAndFetchRecentlyPlayed() {
        UserDetails user = new UserDetails();
        user.setRefId("testUserId");
        user.setId(1);

        PlaybackHistory existingHistory = new PlaybackHistory();
        existingHistory.setTrackName("Old Track");
        existingHistory.setArtistName("Old Artist");
        existingHistory.setPlayedAt(LocalDateTime.now().minusDays(1));

        PlaybackHistory newHistory = new PlaybackHistory();
        newHistory.setTrackName("Test Track");
        newHistory.setArtistName("Test Artist");
        newHistory.setPlayedAt(LocalDateTime.now());

        when(userDetailsRepository.findByRefId("testUserId")).thenReturn(user);
        when(playbackHistoryRepository.findByUserDetailsId(1)).thenReturn(List.of(existingHistory), List.of(newHistory));
        when(playbackHistoryRepository.save(any(PlaybackHistory.class))).thenReturn(newHistory);

        List<PlaybackHistory> updatedHistory = recentlyPlayedService.updateAndFetchRecentlyPlayed(user, List.of(newHistory));

        assertEquals(1, updatedHistory.size());
        assertEquals("Test Track", updatedHistory.get(0).getTrackName());
        assertEquals("Test Artist", updatedHistory.get(0).getArtistName());

        verify(playbackHistoryRepository, times(1)).deleteAll(any());
        verify(playbackHistoryRepository, times(1)).save(newHistory);
        verify(playbackHistoryRepository, times(2)).findByUserDetailsId(1); // before and after
    }
}