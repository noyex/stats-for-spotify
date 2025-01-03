package com.jts.stats_data.repositories;

import com.jts.stats_data.entity.Artists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistsRepository extends JpaRepository<Artists, Long> {
    List<Artists> findByUserDetailsId(Integer id);
}
