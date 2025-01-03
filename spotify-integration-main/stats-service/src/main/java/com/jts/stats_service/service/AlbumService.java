package com.jts.stats_service.service;

import com.jts.stats_data.entity.Albums;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.AlbumsRepository;
import com.jts.stats_data.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.SavedAlbum;

import java.util.Arrays;
import java.util.List;

@Service
public class AlbumService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private AlbumsRepository albumsRepository;

    public List<Albums> albumMapper(SavedAlbum[] savedAlbums) {
        return Arrays.stream(savedAlbums)
                .map(existingAlbums -> {
                    Albums albums = new Albums();
                    albums.setName(existingAlbums.getAlbum().getName());
                    albums.setArtistName(existingAlbums.getAlbum().getArtists()[0].getName());
                    albums.setNumberOfTracks(existingAlbums.getAlbum().getTracks().getTotal());
                    return albums;
                })
                .toList();
    }

    public List<Albums> updateAndFetchAlbums(UserDetails user, List<Albums> newAlbums) {
        UserDetails userDetails = userDetailsRepository.findByRefId(user.getRefId());

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        Integer userId = userDetails.getId();

        List<Albums> existingAlbums = albumsRepository.findByUserDetailsId(userId);

        List<Albums> toRemove = existingAlbums.stream()
                .filter(existing -> newAlbums.stream()
                        .noneMatch(newItem -> newItem.getName().equals(existing.getName())
                                && newItem.getArtistName().equals(existing.getArtistName())))
                .toList();

        albumsRepository.deleteAll(toRemove);

        for (Albums newItem : newAlbums) {
            boolean exists = existingAlbums.stream()
                    .anyMatch(existing -> existing.getName().equals(newItem.getName())
                            && existing.getArtistName().equals(newItem.getArtistName()));
            if (!exists) {
                newItem.setUserDetails(userDetails);
                albumsRepository.save(newItem);
            }
        }

        return albumsRepository.findByUserDetailsId(userId);
    }
}