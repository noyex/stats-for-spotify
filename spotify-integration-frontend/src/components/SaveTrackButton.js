import React, { useState } from 'react';
import { saveTrack } from '../services/spotifyService';

const SaveTrackButton = ({ userId, trackId }) => {
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState(null);

  const handleSaveTrack = async () => {
    setSaving(true);
    setError(null);

    try {
      await saveTrack(userId, trackId);
      setSaved(true);
    } catch (err) {
      setError(err.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      <button onClick={handleSaveTrack} disabled={saving || saved}>
        {saved ? 'Added' : saving ? 'Saving...' : 'Save Track'}
      </button>
      {error && <p className="error-message">Error: {error}</p>}
    </div>
  );
};

export default SaveTrackButton;