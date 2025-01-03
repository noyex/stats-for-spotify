package com.jts.stats_service.service;

import com.jts.stats_data.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PlaylistsService {

    @Autowired
    private PlaylistsRepository playlistsRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<Playlists> playlistMapper(PlaylistSimplified[] playlist) {
        return Arrays.stream(playlist)
                .map(currentPlaylists -> {
                    Playlists playlists = new Playlists();
                    playlists.setName(currentPlaylists.getName());
                    playlists.setNumberOfTracks(currentPlaylists.getTracks().getTotal());
                    return playlists;
                })
                .toList();
    }

    public List<Playlists> updateAndFetchPlaylists(UserDetails user, List<Playlists> newPlaylists) {
        UserDetails userDetails = userDetailsRepository.findByRefId(user.getRefId());

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        Integer userId = userDetails.getId();

        List<Playlists> existingPlaylists = playlistsRepository.findByUserDetailsId(userId);

        List<Playlists> toRemove = existingPlaylists.stream()
                .filter(existing -> newPlaylists.stream()
                        .noneMatch(newItem -> newItem.getName().equals(existing.getName())
                        && newItem.getNumberOfTracks().equals(existing.getNumberOfTracks())))
                .toList();
        playlistsRepository.deleteAll(toRemove);

        for(Playlists newItem : newPlaylists) {
            boolean exists = existingPlaylists.stream()
                    .anyMatch(existing -> existing.getName().equals(newItem.getName())
                    && existing.getNumberOfTracks().equals(newItem.getNumberOfTracks()));
            if (!exists){
                newItem.setUserDetails(userDetails);
                playlistsRepository.save(newItem);
            }
        }
        return playlistsRepository.findByUserDetailsId(userId);
    }
}
