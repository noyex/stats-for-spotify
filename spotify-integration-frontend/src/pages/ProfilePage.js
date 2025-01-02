import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import SpotifyProfileButton from '../components/SpotifyProfileButton';
import { Link } from 'react-router-dom';
import { getCurrentUserProfile } from '../services/spotifyService';
import '../styles/Global.css';
import '../styles/ProfilePage.css';

const ProfilePage = () => {
    const [userId, setUserId] = useState(null);
    const [profile, setProfile] = useState(null); // Przechowywanie danych profilu
    const [error, setError] = useState(null);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        if (id) {
            setUserId(id);
            fetchUserProfile(id);
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
            <h1>Your Profile</h1>
            {error && <p className="error-message">{error}</p>}
            {profile ? (
                <div className="profile-container">
                    <img
                        src={profile.images?.[0]?.url || '/placeholder.png'}
                        alt="Profile"
                        className="profile-picture"
                    />
                    <h2>{profile.display_name || 'N/A'}</h2>
                    <SpotifyProfileButton userId={userId} />
                    <p><strong>Country:</strong> {profile.country || 'N/A'}</p>
                    <p><strong>Subscription:</strong> {profile.product || 'N/A'}</p>
                    <p><strong>Email:</strong> {profile.email || 'N/A'}</p>
                    <LogoutButton userId={userId} />
                </div>
            ) : (
                <p>Loading profile...</p>
            )}
        </div>
    );
};

export default ProfilePage;