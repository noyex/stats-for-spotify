import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import SpotifyProfileButton from '../components/SpotifyProfileButton';
import { Link } from 'react-router-dom';
import { getCurrentUserProfile, getCurrentUserPlaylists, getCurrentUserFollowedArtists, getUserSavedAlbums, getCurrentUserSavedTracks } from '../services/spotifyService';
import '../styles/Global.css';
import '../styles/ProfilePage.css';

const ProfilePage = () => {
    const [userId, setUserId] = useState(null);
    const [profile, setProfile] = useState(null);
    const [playlists, setPlaylists] = useState([]);
    const [artists, setArtists] = useState([]);
    const [albums, setAlbums] = useState([]);
    const [tracks, setTracks] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        if (id) {
            setUserId(id);
            fetchUserProfile(id);
            fetchUserPlaylists(id);
            fetchUserFollowedArtists(id);
            fetchUserSavedAlbums(id);
            fetchUserSavedTracks(id);
        }
    }, []);

    const fetchUserProfile = async (id) => {
        try {
            const data = await getCurrentUserProfile(id); 
            setProfile(data);
        } catch (err) {
            setError("Failed to load user profile.");
            console.error(err);
        }
    };

    const fetchUserPlaylists = async (id) => {
        try {
            const data = await getCurrentUserPlaylists(id); 
            setPlaylists(data);
        } catch (err) {
            setError("Failed to load playlists.");
            console.error(err);
        }
    };

    const fetchUserFollowedArtists = async (id) => {
        try {
            const data = await getCurrentUserFollowedArtists(id);
            setArtists(data);
        } catch (err) {
            setError("Failed to load artists.");
            console.error(err);
        }
    };

    const fetchUserSavedAlbums = async (id) => {
        try {
            const data = await getUserSavedAlbums(id);
            setAlbums(data);
        } catch (err) {
            setError("Failed to load saved albums.");
            console.error(err);
        }
    };

    const fetchUserSavedTracks = async (id) => {
        try {
            const data = await getCurrentUserSavedTracks(id);
            setTracks(data);
        } catch (err) {
            setError("Failed to load saved tracks.");
            console.error(err);
        }
    }

    return (
        <div className="profile-page">
            <nav className="nav-bar">
                {userId && (
                    <>
                        <Link to={`/home?id=${userId}`}>Home</Link>
                        <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
                        <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link>
                        <Link to={`/profile?id=${userId}`}>Profile</Link>
                    </>
                )}
            </nav>
            <div className="profile-layout">
                <div className="profile-left">
                    <h2 className="playlists-header">Your Playlists</h2>
                    {playlists.length > 0 ? (
                        <ul className="playlist-list">
                            {playlists.map((playlist) => (
                                <li key={playlist.id} className="playlist-item">
                                    <img
                                        src={playlist.images?.[0]?.url || '/placeholder.png'}
                                        alt={playlist.name}
                                        className="playlist-picture"
                                    />
                                    <div className="playlist-details">
                                        <h3 className="playlist-name">{playlist.name}</h3>
                                        <p className="playlist-tracks">Tracks: {playlist.tracks.total}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="no-playlists-message">No playlists available.</p>
                    )}
                </div>

                <div className="profile-center">
                    <h1>Your Profile</h1>
                    {error && <p className="error-message">{error}</p>}
                    {profile ? (
                        <div className="profile-container">
                            <img
                                src={profile.images?.[0]?.url || '/placeholder.png'}
                                alt="Profile"
                                className="profile-picture"
                            />
                            <h2>{profile.display_name || `${userId}`}</h2>
                            <SpotifyProfileButton userId={userId} />
                            <p><strong>Country:</strong> {profile.country || 'N/A'}</p>
                            <p><strong>Subscription:</strong> {profile.product || 'N/A'}</p>
                            <p><strong>Email:</strong> {profile.email || 'N/A'}</p>
                            <p><strong>Followers:</strong> {profile.followers.total || 'N/A'}</p>
                            <LogoutButton userId={userId} />
                        </div>
                    ) : (
                        <p>Loading profile...</p>
                    )}
                </div>

                <div className="profile-right">
                    <h2 className="artists-header">Followed Artists</h2>
                    {artists.length > 0 ? (
                        <ul className="artists-list">
                            {artists.map((artist) => (
                                <li key={artist.id} className="artists-item">
                                    <img
                                        src={artist.images?.[0]?.url || '/placeholder.png'}
                                        alt={artist.name}
                                        className="artists-picture"
                                    />
                                    <div className="artists-details">
                                        <h3 className="artists-name">{artist.name}</h3>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    ) : (
                        <p className="no-artists-message">No artists available.</p>
                    )}
                </div>
            </div>

            <div className="bottom-layout">
                <div className="saved-albums-left">
                    <h2>Your Saved Albums</h2>
                    {albums.length > 0 ? (
                        <ul className="albums-list">
                            {albums.map((item, index) => {
                                const album = item.album;
                                const artistNames = album.artists.map((artist) => artist.name).join(", ");
                                return (
                                    <li key={index} className="album-item">
                                        <img
                                            src={album.images?.[0]?.url || '/placeholder.png'}
                                            alt={album.name}
                                            className="album-image"
                                        />
                                        <div className="album-details">
                                            <p className="album-name">{album.name}</p>
                                            <p className="album-tracks">Tracks: {album.tracks.total}</p>
                                            <p className="album-artist">Artists: {artistNames}</p>
                                        </div>
                                    </li>
                                );
                            })}
                        </ul>
                    ) : (
                        <p className="no-albums-message">No saved albums available.</p>
                    )}
                </div>
                <div className="saved-tracks-right">
                    <h2>Saved Tracks</h2>
                    {tracks.length > 0 ? (
                        <ul className="track-list-profile">
                            {tracks.map((item, index) => {
                                const track = item.track;
                                const artistNames = track.artists.map((artist) => artist.name).join(", ");
                                return (
                                    <li key={index} className="track-item-profile">
                                        <img
                                            src={track.album?.images?.[0]?.url || '/placeholder.png'}
                                            alt={track.name}
                                            className="track-image-profile"
                                        />
                                        <div className="track-details-profile">
                                            <p className="album-name">{track.name}</p>
                                            <p className="track-artist-profile">Artists: {artistNames}</p>
                                            <p className="track-album-profile">Album: {track.album.name}</p>
                                        </div>
                                    </li>
                                );
                            })}
                        </ul>
                    ) : (
                        <p className="no-tracks-message">No saved tracks available.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;