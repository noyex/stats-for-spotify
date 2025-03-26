import React, { useState } from 'react';
import { searchArtists } from '../services/spotifyService';
import { useLocation, Link } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import '../styles/SearchArtistsPage.css';
import '../styles/Global.css';
import { FiSearch, FiMusic, FiUser, FiDisc, FiHeart, FiExternalLink } from 'react-icons/fi';
import { toast } from 'react-toastify';
import FollowArtistButton from '../components/FollowArtistButton';

const formatNumber = (num) => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + ' mln';
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + ' tys.';
  }
  return num;
};

const SearchArtistsPage = () => {
  const [query, setQuery] = useState('');
  const [artists, setArtists] = useState([]);
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
      const data = await searchArtists(userId, query);
      setArtists(data);
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

  const capitalizeFirstLetter = (str) => {
    return str.split(' ').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
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
          <Link to={`/search-artists?id=${userId}`} className="search-nav-link active">
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
            placeholder="Wyszukaj artystę..."
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
              <FiUser className="search-no-results-icon" />
              <p>Wystąpił błąd: {error}</p>
              <p>Spróbuj ponownie później</p>
            </div>
          )}
          
          {!error && artists.length === 0 && query && !isLoading && (
            <div className="search-no-results">
              <FiUser className="search-no-results-icon" />
              <p>Nie znaleziono żadnych artystów dla "{query}"</p>
              <p>Spróbuj innej frazy</p>
            </div>
          )}
          
          {!error && !query && !isLoading && (
            <div className="search-empty-state">
              <FiSearch className="search-empty-icon" />
              <h3 className="search-empty-message">Wyszukaj ulubionych artystów</h3>
              <p className="search-empty-suggestion">
                Wprowadź nazwę artysty lub zespołu
              </p>
            </div>
          )}
          
          {!error && artists.length > 0 && (
            <div className="search-results-grid">
              {artists.map((artist) => (
                <div key={artist.id} className="artist-card">
                  <div className="artist-image-container">
                    {artist.images?.[0]?.url ? (
                      <img
                        src={artist.images[0].url}
                        alt={artist.name}
                        className="artist-image"
                      />
                    ) : (
                      <div className="artist-image-placeholder">
                        <FiUser size={40} />
                      </div>
                    )}
                  </div>
                  
                  <div className="artist-content">
                    <h3 className="artist-name">{artist.name}</h3>
                    
                    {artist.genres && artist.genres.length > 0 && (
                      <p className="artist-genres">
                        {artist.genres.slice(0, 2).map(genre => capitalizeFirstLetter(genre)).join(', ')}
                      </p>
                    )}
                    
                    {artist.followers && (
                      <p className="artist-followers">
                        {formatNumber(artist.followers.total)} obserwujących
                      </p>
                    )}
                    
                    {artist.popularity && (
                      <div className="artist-popularity">
                        <div className="popularity-bar">
                          <div 
                            className="popularity-fill" 
                            style={{ width: `${artist.popularity}%` }}
                          ></div>
                        </div>
                        <span className="popularity-text">{artist.popularity}%</span>
                      </div>
                    )}
                    
                    <div className="artist-action-buttons">
                      <FollowArtistButton 
                        userId={userId} 
                        artistId={artist.id} 
                        className="artist-action-button"
                        buttonText="Obserwuj"
                        iconComponent={<FiHeart size={14} />}
                      />
                      
                      <a 
                        href={artist.external_urls.spotify} 
                        target="_blank" 
                        rel="noopener noreferrer"
                        className="artist-action-button"
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

export default SearchArtistsPage;