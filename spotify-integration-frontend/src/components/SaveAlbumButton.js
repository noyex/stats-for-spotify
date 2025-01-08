import React, { useState } from 'react';
import { saveAlbum } from '../services/spotifyService';

const SaveAlbumButton = ({ userId, albumId }) => {
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState(null);

  const handleSaveAlbum = async () => {
    setSaving(true);
    setError(null);

    try {
      await saveAlbum(userId, albumId);
      setSaved(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <button onClick={handleSaveAlbum} disabled={saving || saved}>
        {saved ? 'Added' : saving ? 'Saving...' : 'Save Album'}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default SaveAlbumButton;