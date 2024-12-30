package com.jts.stats_api.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import com.jts.stats_client.config.SpotifyConfiguration;
import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.entity.UserDetailsRepository;
import com.jts.stats_service.service.UserProfileService;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@RestController
@RequestMapping("/api")
public class SpotifyController {

	@Value("${custom.server.ip}")
	private String customIp;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private SpotifyConfiguration spotifyConfiguration;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	@GetMapping("/login")
	public String spotifyLogin() {
		try {
			SpotifyApi object = spotifyConfiguration.getSpotifyObject();

			AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
					.scope("user-library-read user-read-email user-top-read user-read-recently-played user-read-currently-playing")
					.show_dialog(true)
					.build();

			final URI uri = authorizationCodeUriRequest.execute();
			return uri.toString();
		} catch (Exception e) {
			System.err.println("Error generating Spotify login URI: " + e.getMessage());
			return "Error generating login link.";
		}
	}

	@GetMapping(value = "get-user-code")
	public void getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();

		AuthorizationCodeRequest authorizationCodeRequest = object.authorizationCode(userCode).build();
		User user = null;

		try {
			final AuthorizationCodeCredentials authorizationCode = authorizationCodeRequest.execute();

			object.setAccessToken(authorizationCode.getAccessToken());
			object.setRefreshToken(authorizationCode.getRefreshToken());

			final GetCurrentUsersProfileRequest getCurrentUsersProfile = object.getCurrentUsersProfile().build();
			user = getCurrentUsersProfile.execute();

			userProfileService.insertOrUpdateUserDetails(user,
					authorizationCode.getAccessToken(),
					authorizationCode.getRefreshToken());
		} catch (Exception e) {
			System.out.println("Exception occurred while getting user code: " + e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing user code");
			return;
		}

		response.sendRedirect(customIp + "/home?id=" + user.getId());
	}
	
	@GetMapping(value = "home")
	public String home(@RequestParam String userId) {
		try {

			return userId;
		} catch (Exception e) {
			System.out.println("Exception occured while landing to home page: " + e);
		}

		return null;
	}
	
	@GetMapping(value = "user-saved-album")
	public SavedAlbum[] getCurrentUserSavedAlbum(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);

		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());
		
		final GetCurrentUsersSavedAlbumsRequest getUsersTopArtistsRequest = object.getCurrentUsersSavedAlbums()
				.limit(50)
				.offset(0)
				.build();

		try {
			final Paging<SavedAlbum> artistPaging = getUsersTopArtistsRequest.execute();

			return artistPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occured while fetching user saved album: " + e);
		}
		
		return new SavedAlbum[0];
	}

	@GetMapping(value = "user-top-songs-medium")
	public Track[] getUserTopTracksMedium(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);
		
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());
		
		final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
				.time_range("medium_term")
				.limit(50)
				.offset(0)
				.build();

		try {
			final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

			return trackPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occured while fetching top songs: " + e);
		}
		
		return new Track[0];
	}

	@GetMapping(value = "user-top-songs-short")
	public Track[] getUserTopTracksShort(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);

		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());

		final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
				.time_range("short_term")
				.limit(50)
				.offset(0)
				.build();

		try {
			final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

			return trackPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occured while fetching top songs: " + e);
		}

		return new Track[0];
	}

	@GetMapping(value = "user-top-songs-Long")
	public Track[] getUserTopTracksLong(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);

		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());

		final GetUsersTopTracksRequest getUsersTopTracksRequest = object.getUsersTopTracks()
				.time_range("long_term")
				.limit(50)
				.offset(0)
				.build();

		try {
			final Paging<Track> trackPaging = getUsersTopTracksRequest.execute();

			return trackPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occured while fetching top songs: " + e);
		}

		return new Track[0];
	}

	@GetMapping(value = "user-recently-played")
	public PlayHistory[] getUserPlaybackHistory(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);

		SpotifyApi object = spotifyConfiguration.getSpotifyObject();

		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());

		final GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = object.getCurrentUsersRecentlyPlayedTracks()
				.limit(50)
				.before(new Date(System.currentTimeMillis()))
				.build();

		try {
			final PagingCursorbased<PlayHistory> trackPaging = getCurrentUsersRecentlyPlayedTracksRequest.execute();
			return trackPaging.getItems();
		} catch (Exception e){
			System.out.println("Exceprion occured while fetching songs: " + e);
		}
		return new PlayHistory[0];
	}

	@GetMapping(value = "user-currently-playing-track")
	public CurrentlyPlaying getUserCurrentlyPlayingTrack(@RequestParam String userId) {
		UserDetails userDetails = userDetailsRepository.findByRefId(userId);
		SpotifyApi object = spotifyConfiguration.getSpotifyObject();
		object.setAccessToken(userDetails.getAccessToken());
		object.setRefreshToken(userDetails.getRefreshToken());

		final GetUsersCurrentlyPlayingTrackRequest currentlyPlayingTrackRequest = object.getUsersCurrentlyPlayingTrack()
				.market(CountryCode.PL)
				.additionalTypes("track")
				.build();

		try {
			final CurrentlyPlaying currentlyPlayingTrack = currentlyPlayingTrackRequest.execute();

			if (currentlyPlayingTrack != null && currentlyPlayingTrack.getIs_playing()) {
				System.out.println("Currently playing track: " + currentlyPlayingTrack.getItem().getName());
				return currentlyPlayingTrack;
			} else {
				System.out.println("No track is currently playing.");
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching currently playing track: " + e);
		}

		return null;
	}
}
