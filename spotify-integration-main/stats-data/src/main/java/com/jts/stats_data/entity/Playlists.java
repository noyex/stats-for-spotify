package com.jts.stats_data.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Playlists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "number_of_tracks")
    private Integer numberOfTracks;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails userDetails;

}
