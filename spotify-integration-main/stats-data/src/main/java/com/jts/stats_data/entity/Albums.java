package com.jts.stats_data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Albums {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String artistName;
    @Column(name = "number_of_tracks")
    private Integer numberOfTracks;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails userDetails;
}
