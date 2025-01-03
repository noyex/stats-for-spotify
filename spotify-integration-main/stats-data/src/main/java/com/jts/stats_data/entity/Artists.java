package com.jts.stats_data.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Artists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetails userDetails;
}
