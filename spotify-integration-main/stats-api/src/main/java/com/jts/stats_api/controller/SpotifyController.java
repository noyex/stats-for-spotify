package com.jts.stats_api.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import com.jts.stats_api.service.ControllerService;
import com.jts.stats_client.config.SpotifyConfiguration;
import com.jts.stats_data.entity.*;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.*;
import com.neovisionaries.i18n.CountryCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import jakarta.servlet.http.HttpServletResponse;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.follow.FollowArtistsOrUsersRequest;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.follow.UnfollowArtistsOrUsersRequest;
import se.michaelthelin.spotify.requests.data.library.*;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

@RestController
@RequestMapping("/api")
public class SpotifyController {

	@Value("${custom.server.ip}")
	private String customIp;
	
	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private RecentlyPlayedService recentlyPlayedService;

	@Autowired
	private SpotifyConfiguration spotifyConfiguration;
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;

    @Autowired
    private PlaylistsService playlistsService;

	@Autowired
	private ControllerService controllerService;
    @Autowired
    private ArtistsService artistsService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private TracksService tracksService;

	@GetMapping("/login")
	public String spotifyLogin() {
		try {
			SpotifyApi object = spotifyConfiguration.getSpotifyObject();

			AuthorizationCodeUriRequest authorizationCodeUriRequest = object.authorizationCodeUri()
					.scope("user-library-read user-read-email user-read-private user-top-read user-read-recently-played user-read-currently-playing playlist-read-private user-follow-read user-follow-modify user-library-modify")
					.show_dialog(true)
					.build();

			final URI uri = authorizationCodeUriRequest.execute();
			return uri.toString();
		} catch (Exception e) {
			System.err.println("Error generating Spotify login URI: " + e.getMessage());
			return "Error generating login link.";
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> spotifyLogout(@RequestParam String userId) {
		try {
			UserDetails userDetails = userDetailsRepository.findByRefId(userId);
			if (userDetails != null) {
				userDetails.setAccessToken(null);
				userDetails.setRefreshToken(null);
				userDetailsRepository.save(userDetails);
			}
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			System.out.println("Error logging out: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final GetCurrentUsersSavedAlbumsRequest request = spotifyApi.getCurrentUsersSavedAlbums()
					.limit(50)
					.offset(0)
					.build();

			final Paging<SavedAlbum> albumPaging = request.execute();
			SavedAlbum[] savedAlbums = albumPaging.getItems();

			List<Albums> newAlbums = albumService.albumMapper(savedAlbums);
			albumService.updateAndFetchAlbums(userDetails, newAlbums);
			return savedAlbums;
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching user saved album: " + e.getMessage());
			return new SavedAlbum[0];
		}
	}

	@GetMapping(value = "user-top-songs-medium")
	@Cacheable("topSongsMedium")
	public Track[] getUserTopTracksMedium(@RequestParam String userId) {
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
		
		final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
				.time_range("medium_term")
				.limit(48)
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
	@Cacheable("topSongsShort")
	public Track[] getUserTopTracksShort(@RequestParam String userId) {
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

		final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
				.time_range("short_term")
				.limit(48)
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

	@GetMapping(value = "user-top-songs-long")
	@Cacheable("topSongsLong")
	public Track[] getUserTopTracksLong(@RequestParam String userId) {
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

		final GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
				.time_range("long_term")
				.limit(48)
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
	@Cacheable("recentlyPlayed")
	public PlayHistory[] getUserPlaybackHistory(@RequestParam String userId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final GetCurrentUsersRecentlyPlayedTracksRequest request = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
					.limit(48)
					.before(new Date(System.currentTimeMillis()))
					.build();

			final PagingCursorbased<PlayHistory> trackPaging = request.execute();
			PlayHistory[] recentlyPlayedTracks = trackPaging.getItems();

			List<PlaybackHistory> newHistory = recentlyPlayedService.mapRecentlyPlayedToPlaybackHistory(recentlyPlayedTracks);
			recentlyPlayedService.updateAndFetchRecentlyPlayed(userDetails, newHistory);

			return recentlyPlayedTracks;

		} catch (Exception e) {
			System.out.println("Exception occurred while fetching recently played tracks: " + e.getMessage());
			throw new RuntimeException("Failed to fetch recently played tracks.", e);
		}
	}

	@GetMapping(value = "user-currently-playing-track")
	public CurrentlyPlaying getUserCurrentlyPlayingTrack(@RequestParam String userId) {
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

		final GetUsersCurrentlyPlayingTrackRequest currentlyPlayingTrackRequest = spotifyApi.getUsersCurrentlyPlayingTrack()
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

	@GetMapping(value = "current-user-profile")
	public User getCurrentUserProfile(@RequestParam String userId) {
		try {
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile()
					.build();

			final User user = getCurrentUsersProfileRequest.execute();
			System.out.println("Fetched User: " + user);
			return user;
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching user profile: " + e);
			return null;
		}
	}

	@GetMapping(value = "current-user-playlists")
	@Cacheable("playlists")
	public PlaylistSimplified[] getCurrentUserPlaylists(@RequestParam String userId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
			final GetListOfCurrentUsersPlaylistsRequest request = spotifyApi.getListOfCurrentUsersPlaylists()
					.limit(50)
					.offset(0)
					.build();

			final Paging<PlaylistSimplified> playlistSimplifiedPaging = request.execute();
			PlaylistSimplified[] playlists = playlistSimplifiedPaging.getItems();

			List<Playlists> newPlaylists = playlistsService.playlistMapper(playlists);
			playlistsService.updateAndFetchPlaylists(userDetails, newPlaylists);
			return playlists;
		} catch (Exception e){
			System.out.println("Exception occurred while fetching playlists: " + e);
			return new PlaylistSimplified[0];
		}
	}

	@GetMapping(value = "current-user-followed-artists")
	public Artist[] getCurrentUserFollowedArtists(@RequestParam String userId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
			final ModelObjectType type = ModelObjectType.ARTIST;

			final GetUsersFollowedArtistsRequest request = spotifyApi.getUsersFollowedArtists(type)
					.limit(50)
					.build();
			final PagingCursorbased<Artist> artistPagingCursorbased = request.execute();
			Artist[] artists = artistPagingCursorbased.getItems();

			List<Artists> newArtists = artistsService.artistMapper(artists);
			artistsService.updateAndFetchArtists(userDetails, newArtists);
			return artists;
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching artists: " + e);
			return null;
		}

	}

	@GetMapping(value = "current-user-saved-tracks")
	public SavedTrack[] getCurrentUserSavedTracks(@RequestParam String userId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final GetUsersSavedTracksRequest request = spotifyApi.getUsersSavedTracks()
					.limit(50)
					.market(CountryCode.PL)
					.build();

			final Paging<SavedTrack> trackPaging = request.execute();
			SavedTrack[] savedTracks = trackPaging.getItems();

			List<Tracks> newTracks = tracksService.trackMapper(savedTracks);
			tracksService.updateAndFetchTracks(userDetails, newTracks);
			return savedTracks;
		} catch (Exception e){
			System.out.println("Exception occurred while fetching tracks: " + e);
			return new SavedTrack[0];
		}
	}

	@GetMapping(value = "search-albums")
	public AlbumSimplified[] searchAlbums(@RequestParam String userId, @RequestParam String query) {
		UserDetails userDetails = controllerService.getUserDetails(userId);
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
		final SearchAlbumsRequest request = spotifyApi.searchAlbums(query)
				.limit(10)
				.offset(0)
				.market(CountryCode.PL)
				.build();

		try {
			final Paging<AlbumSimplified> albumPaging = request.execute();
			return albumPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occurred while searching albums: " + e.getMessage());
			throw new RuntimeException("Error searching albums", e);
		}
	}

	@GetMapping(value = "search-artists")
	public Artist[] searchArtists(@RequestParam String userId, @RequestParam String query) {
		try {
		UserDetails userDetails = controllerService.getUserDetails(userId);
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
		final SearchArtistsRequest request = spotifyApi.searchArtists(query)
				.limit(10)
				.offset(0)
				.market(CountryCode.PL)
				.build();

			final Paging<Artist> artistPaging = request.execute();
			return artistPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occurred while searching artists: " + e.getMessage());
			throw new RuntimeException("Error searching artists", e);
		}
	}

	@GetMapping(value = "search-tracks")
	public Track[] searchTracks(@RequestParam String userId, @RequestParam String query) {
		UserDetails userDetails = controllerService.getUserDetails(userId);
		SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
		final SearchTracksRequest request = spotifyApi.searchTracks(query)
				.limit(10)
				.offset(0)
				.market(CountryCode.PL)
				.build();

		try {
			final Paging<Track> trackPaging = request.execute();
			return trackPaging.getItems();
		} catch (Exception e) {
			System.out.println("Exception occurred while searching tracks: " + e.getMessage());
			throw new RuntimeException("Error searching tracks", e);
		}
	}

	@PutMapping(value = "save-track-for-current-user")
	public void saveTrackForCurrentUser(@RequestParam String userId, @RequestParam String trackId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final SaveTracksForUserRequest request = spotifyApi.saveTracksForUser(trackId)
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while saving track for current user: " + e.getMessage());
			throw new RuntimeException("Error saving track for current user", e);
		}
	}

	@PutMapping(value = "save-album-for-current-user")
	public void saveAlbumForCurrentUser(@RequestParam String userId, @RequestParam String albumId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final SaveAlbumsForCurrentUserRequest request = spotifyApi.saveAlbumsForCurrentUser(albumId)
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while saving album for current user: " + e.getMessage());
			throw new RuntimeException("Error saving album for current user", e);
		}
	}

	@PutMapping(value = "follow-artist-for-current-user")
	public void followArtistForCurrentUser(@RequestParam String userId, @RequestParam String artistId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
			ModelObjectType type = ModelObjectType.ARTIST;

			final FollowArtistsOrUsersRequest request = spotifyApi.followArtistsOrUsers(type, new String[]{artistId})
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while following artist for current user: " + e.getMessage());
			throw new RuntimeException("Error following artist for current user", e);
		}
	}

	@DeleteMapping(value = "unfollow-artist-for-current-user")
	public void unfollowArtistForCurrentUser(@RequestParam String userId, @RequestParam String artistId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);
			ModelObjectType type = ModelObjectType.ARTIST;

			final UnfollowArtistsOrUsersRequest request = spotifyApi.unfollowArtistsOrUsers(type, new String[]{artistId})
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while unfollowing artist for current user: " + e.getMessage());
			throw new RuntimeException("Error unfollowing artist for current user", e);
		}
	}

	@DeleteMapping(value = "remove-saved-track-for-current-user")
	public void removeSavedTrackForCurrentUser(@RequestParam String userId, @RequestParam String trackId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final RemoveUsersSavedTracksRequest request = spotifyApi.removeUsersSavedTracks(trackId)
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while unsaving track for current user: " + e.getMessage());
			throw new RuntimeException("Error unsaving track for current user", e);
		}
	}

	@DeleteMapping(value = "remove-saved-album-for-current-user")
	public void removeSavedAlbumForCurrentUser(@RequestParam String userId, @RequestParam String albumId) {
		try {
			UserDetails userDetails = controllerService.getUserDetails(userId);
			SpotifyApi spotifyApi = controllerService.getSpotifyApiForUser(userId);

			final RemoveAlbumsForCurrentUserRequest request = spotifyApi.removeAlbumsForCurrentUser(albumId)
					.build();

			request.execute();
		} catch (Exception e) {
			System.out.println("Exception occurred while unsaving album for current user: " + e.getMessage());
			throw new RuntimeException("Error unsaving album for current user", e);
		}
	}




}
