import React from 'react';

const SpotifyProfileButton = ({ userId }) => {
    const redirectToSpotifyProfile = async () => {
        try {
            const response = await fetch(`/api/get-spotify-profile-url?userId=${userId}`);
            const data = await response.json();
            
            if (data.url) {
                window.open(data.url, '_blank');
            }
        } catch (error) {
            console.error('Error redirecting to Spotify profile:', error);
        }
    };

    return (
        <button onClick={redirectToSpotifyProfile} className="spotify-button">
            <span className="button-icon">🌐</span>
            Przejdź do profilu Spotify
        </button>
    );
};

export default SpotifyProfileButton;