import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import { Link } from 'react-router-dom';

const ProfilePage = () => {
    const [userId, setUserId] = useState(null);

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        if (id) {
            setUserId(id);
        }
    }, []);

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
            <h1>Profile Page</h1>
            {userId ? (
                <LogoutButton userId={userId} />
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
};

export default ProfilePage;