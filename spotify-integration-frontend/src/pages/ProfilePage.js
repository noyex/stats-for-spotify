import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import SpotifyProfileButton from '../components/SpotifyProfileButton';
import { Link } from 'react-router-dom';
import { getCurrentUserProfile, getCurrentUserPlaylists } from '../services/spotifyService';
import '../styles/Global.css';
import '../styles/ProfilePage.css';

const ProfilePage = () => {
    const [userId, setUserId] = useState(null);
    const [profile, setProfile] = useState(null);
    const [playlists, setPlaylists] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        if (id) {
            setUserId(id);
            fetchUserProfile(id);
            fetchUserPlaylists(id);
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

    return (
        <div className="profile-page">
            <nav className="nav-bar">
                {userId && (
                    <>
                        <Link to={`/home?id=${userId}`}>Home</Link>
                        <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
                        <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link>
                        <Link to={`/user-recently-played?id=${userId}`}>Playback History</Link>
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
                    <h2>Right Section</h2>
                    <p>This space is empty for now.</p>
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;