package com.jts.stats_service.service;

import com.jts.stats_data.entity.Tracks;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.TracksRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.SavedAlbum;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;

import java.util.Arrays;
import java.util.List;

@Service
public class TracksService {

    @Autowired
    private TracksRepository tracksRepository;

    @Autowired
    private UserDetailsRepository UserDetailsRepository;
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<Tracks> trackMapper(SavedTrack[] savedTracks) {
        return Arrays.stream(savedTracks)
                .map(existingTracks -> {
                    Tracks tracks = new Tracks();
                    tracks.setName(existingTracks.getTrack().getName());
                    tracks.setArtistName(existingTracks.getTrack().getArtists()[0].getName());
                    tracks.setAlbumName(existingTracks.getTrack().getAlbum().getName());
                    return tracks;
                })
                .toList();
    }

    public List<Tracks> updateAndFetchTracks(UserDetails user, List<Tracks> newTracks) {
        UserDetails userDetails = userDetailsRepository.findByRefId(user.getRefId());

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        Integer userId = userDetails.getId();

        List<Tracks> existingTracks = tracksRepository.findByUserDetailsId(userId);

        List<Tracks> toRemove = existingTracks.stream()
                .filter(existing -> newTracks.stream()
                        .noneMatch(newItem -> newItem.getName().equals(existing.getName())
                        && newItem.getAlbumName().equals(existing.getAlbumName())
                        && newItem.getArtistName().equals(existing.getArtistName())))
                .toList();
        tracksRepository.deleteAll(existingTracks);

        for (Tracks newItem : newTracks) {
            boolean exists = existingTracks.stream()
                    .anyMatch(existing -> existing.getName().equals(newItem.getName())
                    && existing.getAlbumName().equals(newItem.getAlbumName())
                    && existing.getArtistName().equals(newItem.getArtistName()));
            if(!exists) {
                newItem.setUserDetails(userDetails);
                tracksRepository.save(newItem);
            }
        }
        return tracksRepository.findByUserDetailsId(userId);
    }
}
