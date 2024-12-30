package com.jts.stats_data.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "playback_history")
public class PlaybackHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackName;
    private String artistName;
    private String albumImageUrl;

    @Column(nullable = false)
    private LocalDateTime playedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails userDetails;
}
