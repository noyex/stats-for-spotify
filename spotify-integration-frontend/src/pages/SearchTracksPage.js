import React, { useState } from 'react';
import { searchTracks } from '../services/spotifyService';
import { useLocation } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchTracksPage.css';
import '../styles/Global.css';
import NavbarSearch from '../components/NavbarSearch';
import SaveTrackButton from '../components/SaveTrackButton';

const SearchTracksPage = () => {
  const [query, setQuery] = useState('');
  const [tracks, setTracks] = useState([]);
  const [error, setError] = useState(null);
  
  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  const handleSearch = async () => {
    try {
      const data = await searchTracks(userId, query);
      setTracks(data);
    } catch (err) {
      setError(err.message);
      console.error(err);
    }
  };

  return (
    <div className="search-tracks-page">
      {userId && <Navbar userId={userId} />}
      <h1>Search for Tracks</h1>
      {userId && <NavbarSearch userId={userId} />}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search for a track..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search-input"
        />
        <button onClick={handleSearch} className="search-button">Search</button>
      </div>

      {error && <p className="error-message">Error: {error}</p>}

      <ul className="tracks-list-search">
        {tracks.map((track) => (
            <li key={track.id} className="track-item-search">
            {track.album.images?.[0]?.url && (
                <img
                src={track.album.images[0].url}
                alt={track.name}
                className="track-image-search"
                />
            )}
            <div className="track-details-search">
                <p className="track-name-search">{track.name}</p>
                <p className="track-artists-search">by {track.artists.map((artist) => artist.name).join(", ")}</p>
                <SaveTrackButton userId={userId} trackId={track.id} />
            </div>
            </li>
        ))}
        </ul>
    </div>
  );
};

export default SearchTracksPage;