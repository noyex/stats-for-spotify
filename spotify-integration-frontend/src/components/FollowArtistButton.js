import React, { useState } from 'react';
import { followArtist } from '../services/spotifyService';

const FollowArtistButton = ({ userId, artistId }) => {
  const [following, setFollowing] = useState(false);
  const [followed, setFollowed] = useState(false);
  const [error, setError] = useState(null);

  const handleFollowArtist = async () => {
    setFollowing(true);
    setError(null);

    try {
      await followArtist(userId, artistId);
      setFollowed(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setFollowing(false);
    }
  };

  return (
    <div>
      <button onClick={handleFollowArtist} disabled={following || followed}>
        {followed ? 'Followed' : following ? 'Following...' : 'Follow Artist'}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default FollowArtistButton;