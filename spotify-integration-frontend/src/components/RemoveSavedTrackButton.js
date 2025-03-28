import React, { useState } from 'react';
import { removeSavedTrack } from '../services/spotifyService';
import '../styles/RemoveButton.css';

const RemoveSavedTrackButton = ({ userId, trackId }) => {
  const [removing, setRemoving] = useState(false);
  const [removed, setRemoved] = useState(false);
  const [error, setError] = useState(null);

  const handleRemoveSavedTrack = async () => {
    setRemoving(true);
    setError(null);

    try {
      await removeSavedTrack(userId, trackId);
      setRemoved(true);
      window.location.reload();
    } catch (err) {
      setError(err.message);
    } finally {
      setRemoving(false);
    }
  };

  return (
    <div>
      <button onClick={handleRemoveSavedTrack} disabled={removing || removed} className="remove-button">
        {removed ? 'Removed' : removing ? 'Removing...' : <strong>Remove</strong>}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default RemoveSavedTrackButton;