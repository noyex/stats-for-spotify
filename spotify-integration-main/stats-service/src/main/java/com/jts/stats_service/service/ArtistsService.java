package com.jts.stats_service.service;

import com.jts.stats_data.entity.Artists;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.ArtistsRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Arrays;
import java.util.List;

@Service
public class ArtistsService {

    @Autowired
    private ArtistsRepository artistsRepository;

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    public List<Artists> artistMapper(Artist[] artist) {
        return Arrays.stream(artist)
                .map(followedArtists -> {
                    Artists artists = new Artists();
                    artists.setName(followedArtists.getName());
                    return artists;
                })
                .toList();
    }

    public List<Artists> updateAndFetchArtists(UserDetails user, List<Artists> newArtists) {
        UserDetails userDetails = userDetailsRepository.findByRefId(user.getRefId());

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        Integer userId = userDetails.getId();

        List<Artists> followedArtists = artistsRepository.findByUserDetailsId(userId);

        List<Artists> toRemove = followedArtists.stream()
                .filter(followed -> newArtists.stream()
                        .noneMatch(newItem -> newItem.getName().equals(followed.getName())))
                .toList();
        artistsRepository.deleteAll(toRemove);

        for(Artists newItem : newArtists) {
            boolean exists = followedArtists.stream()
                    .anyMatch(followed -> followed.getName().equals(newItem.getName()));
            if(!exists){
                newItem.setUserDetails(userDetails);
                artistsRepository.save(newItem);
            }
        }
        return artistsRepository.findByUserDetailsId(userId);
    }
}
