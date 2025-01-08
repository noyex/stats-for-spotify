import React, { useState } from 'react';
import { searchArtists } from '../services/spotifyService';
import { useLocation } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchArtistsPage.css';
import '../styles/Global.css';
import NavbarSearch from '../components/NavbarSearch';

const SearchArtistsPage = () => {
  const [query, setQuery] = useState('');
  const [artists, setArtists] = useState([]);
  const [error, setError] = useState(null);
  
  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  const handleSearch = async () => {
    try {
      const data = await searchArtists(userId, query);
      setArtists(data);
    } catch (err) {
      setError(err.message);
      console.error(err);
    }
  };

  return (
    <div className="search-artists-page">
      {userId && <Navbar userId={userId} />}
      <h1>Search for Artists</h1>
      {userId && <NavbarSearch userId={userId} />}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search for an artist..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search-input"
        />
        <button onClick={handleSearch} className="search-button">Search</button>
      </div>

      {error && <p className="error-message">Error: {error}</p>}

      <ul className="artists-list-search">
        {artists.map((artist) => (
          <li key={artist.id} className="artist-item-search">
            {artist.images?.[0]?.url && (
              <img
                src={artist.images[0].url}
                alt={artist.name}
                className="artist-image-search"
              />
            )}
            <div className="artist-details-search">
              <p className="artist-name-search">{artist.name}</p>
              <p className="artist-genres-search">{artist.genres.join(", ")}</p>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SearchArtistsPage;