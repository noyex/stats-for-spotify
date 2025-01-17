package com.jts.stats_client.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

@Service
public class SpotifyConfiguration {
	
	@Value("${redirect.server.ip}")
    protected String customIp;

	@Value("${client.id}")
    protected String clientId;

	@Value("${client.sercet.id}")
    protected String clientSecret;
	
	public SpotifyApi getSpotifyObject() {
		 URI redirectedURL = SpotifyHttpManager.makeUri(customIp + "/api/get-user-code");
		 
		 return new SpotifyApi
				 .Builder()
				 .setClientId(clientId)
				 .setClientSecret(clientSecret)
				 .setRedirectUri(redirectedURL)
				 .build();
	}
}
