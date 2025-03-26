import React, { useState } from 'react';
import { searchAlbums } from '../services/spotifyService';
import { useLocation, Link } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchAlbumsPage.css';
import '../styles/Global.css';
import { FiSearch, FiMusic, FiUser, FiDisc, FiHeart, FiExternalLink, FiCalendar } from 'react-icons/fi';
import { toast } from 'react-toastify';
import SaveAlbumButton from '../components/SaveAlbumButton';

const SearchAlbumsPage = () => {
  const [query, setQuery] = useState('');
  const [albums, setAlbums] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  
  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  const handleSearch = async () => {
    if (!query.trim()) {
      toast.info('Wprowadź wyszukiwaną frazę');
      return;
    }
    
    setIsLoading(true);
    setError(null);
    
    try {
      const data = await searchAlbums(userId, query);
      setAlbums(data);
    } catch (err) {
      setError(err.message);
      toast.error('Wystąpił błąd podczas wyszukiwania');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="search-page">
      {userId && <Navbar userId={userId} />}
      
      <div className="search-header">
        <h1 className="search-title">Wyszukiwarka Spotify</h1>
        <p className="search-description">
          Wyszukaj ulubione utwory, artystów lub albumy i zapisz je do swoich playlist
        </p>
        
        <div className="search-nav">
          <Link to={`/search-tracks?id=${userId}`} className="search-nav-link">
            <FiMusic /> Utwory
          </Link>
          <Link to={`/search-artists?id=${userId}`} className="search-nav-link">
            <FiUser /> Artyści
          </Link>
          <Link to={`/search-albums?id=${userId}`} className="search-nav-link active">
            <FiDisc /> Albumy
          </Link>
        </div>
      </div>
      
      <div className="search-container">
        <div className="search-form">
          <FiSearch className="search-icon" />
          <input
            type="text"
            placeholder="Wyszukaj album..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={handleKeyDown}
            className="search-input"
          />
          <button 
            onClick={handleSearch} 
            className="search-button"
            disabled={isLoading}
          >
            {isLoading ? 'Szukam...' : 'Szukaj'}
          </button>
        </div>

        <div className="search-results-container">
          {error && (
            <div className="search-no-results">
              <FiDisc className="search-no-results-icon" />
              <p>Wystąpił błąd: {error}</p>
              <p>Spróbuj ponownie później</p>
            </div>
          )}
          
          {!error && albums.length === 0 && query && !isLoading && (
            <div className="search-no-results">
              <FiDisc className="search-no-results-icon" />
              <p>Nie znaleziono żadnych albumów dla "{query}"</p>
              <p>Spróbuj innej frazy</p>
            </div>
          )}
          
          {!error && !query && !isLoading && (
            <div className="search-empty-state">
              <FiSearch className="search-empty-icon" />
              <h3 className="search-empty-message">Wyszukaj ulubione albumy</h3>
              <p className="search-empty-suggestion">
                Wprowadź nazwę albumu lub artysty
              </p>
            </div>
          )}
          
          {!error && albums.length > 0 && (
            <div className="search-results-grid">
              {albums.map((album) => (
                <div key={album.id} className="album-card">
                  <div className="album-image-container">
                    {album.images?.[0]?.url ? (
                      <img
                        src={album.images[0].url}
                        alt={album.name}
                        className="album-image"
                      />
                    ) : (
                      <div className="album-image-placeholder">
                        <FiDisc size={40} />
                      </div>
                    )}
                    {album.release_date && (
                      <div className="album-year">
                        {new Date(album.release_date).getFullYear()}
                      </div>
                    )}
                  </div>
                  
                  <div className="album-content">
                    <h3 className="album-name">{album.name}</h3>
                    <p className="album-artist">
                      {album.artists.map(artist => artist.name).join(', ')}
                    </p>
                    
                    {album.total_tracks && (
                      <p className="album-tracks">
                        <FiMusic size={12} style={{ marginRight: '5px' }} />
                        {album.total_tracks} utworów
                      </p>
                    )}
                    
                    <div className="album-action-buttons">
                      <SaveAlbumButton 
                        userId={userId} 
                        albumId={album.id} 
                        className="album-action-button save-button"
                        buttonText="Zapisz"
                        iconComponent={<FiHeart size={14} />}
                      />
                      
                      <a 
                        href={album.external_urls.spotify} 
                        target="_blank" 
                        rel="noopener noreferrer"
                        className="album-action-button"
                      >
                        <FiExternalLink size={14} /> Otwórz
                      </a>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default SearchAlbumsPage;