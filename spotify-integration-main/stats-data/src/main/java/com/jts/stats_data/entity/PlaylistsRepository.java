package com.jts.stats_data.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

import java.util.List;

@Repository
public interface PlaylistsRepository extends JpaRepository<Playlists, Long> {
    List<Playlists> findByUserDetailsId(Integer userId);
}
