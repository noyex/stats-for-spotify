package com.jts.stats_client.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

class SpotifyConfigurationTests {

    private SpotifyConfiguration spotifyConfiguration;

    @BeforeEach
    void setUp() {
        spotifyConfiguration = new SpotifyConfiguration();
        spotifyConfiguration.customIp = "http://localhost";
        spotifyConfiguration.clientId = "testClientId";
        spotifyConfiguration.clientSecret = "testClientSecret";
    }

    @Test
    void testGetSpotifyObject() {
        URI expectedUri = SpotifyHttpManager.makeUri("http://localhost/api/get-user-code");

        SpotifyApi spotifyApi = spotifyConfiguration.getSpotifyObject();

        assertEquals("testClientId", spotifyApi.getClientId());
        assertEquals("testClientSecret", spotifyApi.getClientSecret());
        assertEquals(expectedUri, spotifyApi.getRedirectURI());
    }
}