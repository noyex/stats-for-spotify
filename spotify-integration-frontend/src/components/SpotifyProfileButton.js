import React from "react";

const SpotifyProfileButton = ({userId}) => {
    const handleSpotifyProfile = async () => {
        if(!userId){
            console.error('User ID is missing');
            return;
        }
            window.location.href = `https://open.spotify.com/user/${userId}`;
    };
    return <button onClick={handleSpotifyProfile} className="spotify-profile-button">Spotify Profile</button>;
};
export default SpotifyProfileButton;