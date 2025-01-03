package com.jts.stats_data.repositories;

import com.jts.stats_data.entity.Tracks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TracksRepository extends JpaRepository<Tracks, Long> {
    List<Tracks> findByUserDetailsId(Integer id);
}
