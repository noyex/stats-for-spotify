import React, { useState } from 'react';
import { removeSavedAlbum } from '../services/spotifyService';
import '../styles/RemoveButton.css';

const RemoveSavedAlbumButton = ({ userId, albumId }) => {
  const [removing, setRemoving] = useState(false);
  const [removed, setRemoved] = useState(false);
  const [error, setError] = useState(null);

  const handleRemoveSavedAlbum = async () => {
    setRemoving(true);
    setError(null);

    try {
      await removeSavedAlbum(userId, albumId);
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
      <button onClick={handleRemoveSavedAlbum} disabled={removing || removed} className="remove-button">
        {removed ? 'Removed' : removing ? 'Removing...' : <strong>Remove</strong>}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default RemoveSavedAlbumButton;