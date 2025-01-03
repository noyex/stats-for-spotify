package com.jts.stats_data.repositories;

import com.jts.stats_data.entity.Playlists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistsRepository extends JpaRepository<Playlists, Long> {
    List<Playlists> findByUserDetailsId(Integer userId);
}
