package com.jts.stats_data.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Table(name = "users")
@Data
@Entity
public class UserDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="ID")
	private Integer id;

	@Column(name ="ref_id")
	private String refId;

	@Column(name ="username")
	private String username;

	@Column(name ="email")
	private String email;

	@Column(name ="access_token", columnDefinition = "TEXT")
	private String accessToken;

	@Column(name ="refresh_token")
	private String refreshToken;
}
