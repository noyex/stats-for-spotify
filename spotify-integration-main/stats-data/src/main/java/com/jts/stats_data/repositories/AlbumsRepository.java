package com.jts.stats_data.repositories;

import com.jts.stats_data.entity.Albums;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface AlbumsRepository extends JpaRepository<Albums, Long> {
    List<Albums> findByUserDetailsId(Integer id);
}
