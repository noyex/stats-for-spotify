package com.jts.stats_data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tracks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String albumName;
    private String artistName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails userDetails;
}
