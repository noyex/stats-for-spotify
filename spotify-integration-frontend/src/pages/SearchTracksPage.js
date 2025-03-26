import React, { useState } from 'react';
import { searchTracks } from '../services/spotifyService';
import { useLocation, Link } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchTracksPage.css';
import '../styles/Global.css';
import { FiSearch, FiMusic, FiUser, FiDisc, FiHeart, FiExternalLink } from 'react-icons/fi';
import { MdOutlineQueueMusic } from 'react-icons/md';
import { toast } from 'react-toastify';
import SaveTrackButton from '../components/SaveTrackButton';

const SearchTracksPage = () => {
  const [query, setQuery] = useState('');
  const [tracks, setTracks] = useState([]);
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
      const data = await searchTracks(userId, query);
      setTracks(data);
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
          <Link to={`/search-tracks?id=${userId}`} className="search-nav-link active">
            <FiMusic /> Utwory
          </Link>
          <Link to={`/search-artists?id=${userId}`} className="search-nav-link">
            <FiUser /> Artyści
          </Link>
          <Link to={`/search-albums?id=${userId}`} className="search-nav-link">
            <FiDisc /> Albumy
          </Link>
        </div>
      </div>
      
      <div className="search-container">
        <div className="search-form">
          <FiSearch className="search-icon" />
          <input
            type="text"
            placeholder="Wyszukaj utwór..."
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
              <FiMusic className="search-no-results-icon" />
              <p>Wystąpił błąd: {error}</p>
              <p>Spróbuj ponownie później</p>
            </div>
          )}
          
          {!error && tracks.length === 0 && query && !isLoading && (
            <div className="search-no-results">
              <FiMusic className="search-no-results-icon" />
              <p>Nie znaleziono żadnych utworów dla "{query}"</p>
              <p>Spróbuj innej frazy</p>
            </div>
          )}
          
          {!error && !query && !isLoading && (
            <div className="search-empty-state">
              <FiSearch className="search-empty-icon" />
              <h3 className="search-empty-message">Wyszukaj ulubione utwory</h3>
              <p className="search-empty-suggestion">
                Wprowadź nazwę utworu, artysty lub słowa z tekstu piosenki
              </p>
            </div>
          )}
          
          {!error && tracks.length > 0 && (
            <div className="search-results-grid">
              {tracks.map((track) => (
                <div key={track.id} className="search-result-card">
                  <div className="search-result-image-container">
                    {track.album.images?.[0]?.url ? (
                      <img
                        src={track.album.images[0].url}
                        alt={track.name}
                        className="search-result-image"
                      />
                    ) : (
                      <div className="search-result-image-placeholder">
                        <FiMusic size={40} />
                      </div>
                    )}
                  </div>
                  
                  <div className="search-result-content">
                    <h3 className="search-result-name">{track.name}</h3>
                    <p className="search-result-artist">
                      {track.artists.map(artist => artist.name).join(', ')}
                    </p>
                    
                    <p className="search-result-info">
                      Album: {track.album.name}
                    </p>
                    
                    <div className="search-action-buttons">
                      <SaveTrackButton 
                        userId={userId} 
                        trackId={track.id} 
                        className="search-action-button save-button"
                        buttonText="Zapisz"
                        iconComponent={<FiHeart size={14} />}
                      />
                      
                      <a 
                        href={track.external_urls.spotify} 
                        target="_blank" 
                        rel="noopener noreferrer"
                        className="search-action-button"
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

export default SearchTracksPage;