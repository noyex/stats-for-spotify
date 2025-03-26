import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
import { getUserTopTracksLong } from '../services/spotifyService';
import '../styles/TopSongs.css'; 
import '../styles/Global.css'; 

const TopSongsLong = () => {
  const [tracks, setTracks] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  useEffect(() => {
    const fetchTopTracks = async () => {
      try {
        const data = await getUserTopTracksLong(userId);
        setTracks(data);
      } catch (err) {
        console.error("Error fetching top tracks:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
  
    if (userId) {
      fetchTopTracks();
    } else {
      console.error("UserId is null or undefined.");
    }
  }, [userId]);

  return (
    <div className="top-songs-container">
      {userId && <Navbar userId={userId} />}
      
      <div className="container">
        <div className="top-songs-header">
          <h1 className="top-songs-title">
            <span className="emoji-icon">🔥</span> Twoje top utwory
          </h1>
          <p className="top-songs-subtitle">
            Odkryj swoją ulubioną muzykę z ostatniego roku
          </p>
        </div>
        
        <div className="time-filter-tabs">
          <Link to={`/top-songs-short?id=${userId}`} className="time-filter-tab">
            Ostatnie 4 tygodnie
          </Link>
          <Link to={`/top-songs-medium?id=${userId}`} className="time-filter-tab">
            Ostatnie 6 miesięcy
          </Link>
          <Link to={`/top-songs-long?id=${userId}`} className="time-filter-tab active">
            Ostatnie 12 miesięcy
          </Link>
        </div>
        
        {error && <div className="error-message">Błąd: {error}</div>}
      
        {loading ? (
          <div className="loading">Ładowanie najczęściej odtwarzanych utworów...</div>
        ) : tracks.length === 0 ? (
          <div className="no-tracks-container">
            <div className="no-tracks-icon">🎵</div>
            <h2 className="no-tracks-message">Brak danych do wyświetlenia</h2>
            <p className="no-tracks-suggestion">
              Słuchaj więcej muzyki na Spotify, aby zobaczyć swoje ulubione utwory w tym okresie
            </p>
          </div>
        ) : (
          <div className="tracks-grid">
            {tracks.map((track, index) => (
              <div key={track.id} className="track-card card">
                <div className="track-rank">{index + 1}</div>
                <div className="track-image-container">
                  {track.album?.images && track.album.images.length > 0 ? (
                    <img
                      src={track.album.images[0].url}
                      alt={track.name}
                      className="track-image"
                    />
                  ) : (
                    <div className="track-image-placeholder"></div>
                  )}
                </div>
                <div className="track-info">
                  <h3 className="track-name">{track.name}</h3>
                  <p className="track-artist">
                    {track.artists.map((artist) => artist.name).join(", ")}
                  </p>
                  <p className="track-album">{track.album.name}</p>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default TopSongsLong;