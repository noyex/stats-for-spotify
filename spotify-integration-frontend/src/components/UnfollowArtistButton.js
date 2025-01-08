import React, { useState } from 'react';
import { unfollowArtist } from '../services/spotifyService';
import '../styles/RemoveButton.css';

const UnfollowArtistButton = ({ userId, artistId }) => {
  const [unfollowing, setUnfollowing] = useState(false);
  const [unfollowed, setUnfollowed] = useState(false);
  const [error, setError] = useState(null);

  const handleUnfollowArtist = async () => {
    setUnfollowing(true);
    setError(null);

    try {
      await unfollowArtist(userId, artistId);
      setUnfollowed(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setUnfollowing(false);
    }
  };

  return (
    <div>
      <button onClick={handleUnfollowArtist} disabled={unfollowing || unfollowed} className="remove-button">
        {unfollowed ? 'Unfollowed' : unfollowing ? 'Unfollowing...' : <strong>Unfollow</strong>}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default UnfollowArtistButton;