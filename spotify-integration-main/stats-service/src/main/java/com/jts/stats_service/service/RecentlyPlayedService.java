package com.jts.stats_service.service;


import com.jts.stats_data.entity.PlaybackHistory;
import com.jts.stats_data.repositories.PlaybackHistoryRepository;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.PlayHistory;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
public class RecentlyPlayedService {

    @Autowired
    private PlaybackHistoryRepository playbackHistoryRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<PlaybackHistory> mapRecentlyPlayedToPlaybackHistory(PlayHistory[] recentlyPlayedTracks) {
        return Arrays.stream(recentlyPlayedTracks)
                .map(playHistory ->{
                    PlaybackHistory history = new PlaybackHistory();
                    history.setTrackName(playHistory.getTrack().getName());
                    history.setArtistName(playHistory.getTrack().getArtists()[0].getName());
                    history.setPlayedAt(playHistory.getPlayedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    return history;
                })
                .toList();
    }

    public List<PlaybackHistory> updateAndFetchRecentlyPlayed(UserDetails user, List<PlaybackHistory> newHistory) {
        UserDetails userDetails = userDetailsRepository.findByRefId(user.getRefId());

        if(userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        Integer userId = userDetails.getId();

        List<PlaybackHistory> existingHistory = playbackHistoryRepository.findByUserDetailsId(userId);

        List<PlaybackHistory> toRemove = existingHistory.stream()
                .filter(existing -> newHistory.stream()
                        .noneMatch(newItem -> newItem.getTrackName().equals(existing.getTrackName())
                                && newItem.getPlayedAt().equals(existing.getPlayedAt())))
                .toList();
        playbackHistoryRepository.deleteAll(toRemove);

        for(PlaybackHistory newItem : newHistory) {
            boolean exists = existingHistory.stream()
                    .anyMatch(existing -> existing.getTrackName().equals(newItem.getTrackName())
                            && existing.getPlayedAt().equals(newItem.getPlayedAt()));
            if (!exists) {
                newItem.setUserDetails(userDetails);
                playbackHistoryRepository.save(newItem);
            }
        }
        return playbackHistoryRepository.findByUserDetailsId(userId);
    }
}
