import React, { useState } from 'react';
import { searchAlbums } from '../services/spotifyService';
import { useLocation } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchAlbumsPage.css';
import '../styles/Global.css';
import NavbarSearch from '../components/NavbarSearch';

const SearchAlbumsPage = () => {
  const [query, setQuery] = useState('');
  const [albums, setAlbums] = useState([]);
  const [error, setError] = useState(null);
  
  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  const handleSearch = async () => {
    try {
      const data = await searchAlbums(userId, query);
      setAlbums(data);
    } catch (err) {
      setError(err.message);
      console.error(err);
    }
  };

  return (
    <div className="search-albums-page">
      {userId && <Navbar userId={userId} />}
      <h1>Search for Albums</h1>
      {userId && <NavbarSearch userId={userId} />}
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search for an album..."
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="search-input"
        />
        <button onClick={handleSearch} className="search-button">Search</button>
      </div>

      {error && <p className="error-message">Error: {error}</p>}

      <ul className="albums-list-search">
        {albums.map((album) => (
          <li key={album.id} className="album-item-search">
            {album.images?.[0]?.url && (
              <img
                src={album.images[0].url}
                alt={album.name}
                className="album-image-search"
              />
            )}
            <div className="album-details-search">
              <p className="album-name-search">{album.name}</p>
              <p className="album-artists-search">by {album.artists.map((artist) => artist.name).join(", ")}</p>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SearchAlbumsPage;