package com.jts.stats_data.repositories;

import com.jts.stats_data.entity.PlaybackHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaybackHistoryRepository extends JpaRepository<PlaybackHistory, Long> {
    List<PlaybackHistory> findByUserDetailsId(Integer userId);
}
