package com.jts.stats_api.service;

import com.jts.stats_api.controller.SpotifyController;
import com.jts.stats_client.config.SpotifyConfiguration;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;

@Service
public class ControllerService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private SpotifyConfiguration spotifyConfiguration;

    @Cacheable("spotifyApi")
    public SpotifyApi getSpotifyApiForUser(String userId) {
        UserDetails userDetails = userDetailsRepository.findByRefId(userId);

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found.");
        }

        SpotifyApi spotifyApi = spotifyConfiguration.getSpotifyObject();
        spotifyApi.setAccessToken(userDetails.getAccessToken());
        spotifyApi.setRefreshToken(userDetails.getRefreshToken());

        return spotifyApi;
    }

    @Cacheable("userDetails")
    public UserDetails getUserDetails(String userId) {
        UserDetails userDetails = userDetailsRepository.findByRefId(userId);

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found.");
        }
        return userDetails;
    }
}
