import React, { useEffect, useState } from 'react';
import Navbar from '../components/Navbar';
import '../styles/HomePage.css'; 
import '../styles/Global.css'; 

const HomePage = () => {
  const [userId, setUserId] = useState(null);
  const [currentTrack, setCurrentTrack] = useState(null);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (id) {
      setUserId(id);
    }
  }, []);

  useEffect(() => {
    const fetchCurrentlyPlaying = async () => {
      try {
        const response = await fetch(`/api/user-currently-playing-track?userId=${userId}`);
        const data = await response.json();
        setCurrentTrack(data);
      } catch (err) {
        console.error('Error fetching currently playing track:', err);
      }
    };

    if (userId) {
      fetchCurrentlyPlaying();
      // Odświeżaj co 30 sekund informacje o aktualnie odtwarzanym utworze
      const interval = setInterval(fetchCurrentlyPlaying, 30000);
      return () => clearInterval(interval);
    }
  }, [userId]);

  return (
    <div className="home-container">
      {userId && <Navbar userId={userId} />}

      <div className="container">
        <div className="hero-section glass-panel">
          <div className="welcome-header">
            <h1 className="welcome-message">
              <span className="emoji-icon">✨</span> Witaj w Spotify Stats!
            </h1>
            <p className="welcome-subtitle">
              Odkrywaj swoje muzyczne preferencje i statystyki Spotify
            </p>
          </div>
        </div>

        <div className="features-grid grid grid-3">
          <div className="feature-card card">
            <div className="feature-icon">🎵</div>
            <h3>Ulubione utwory</h3>
            <p>Zobacz swoje najczęściej słuchane utwory w różnych przedziałach czasowych</p>
          </div>
          <div className="feature-card card">
            <div className="feature-icon">🔍</div>
            <h3>Wyszukiwanie</h3>
            <p>Przeglądaj i zapisuj swoje ulubione albumy, utwory i wykonawców</p>
          </div>
          <div className="feature-card card">
            <div className="feature-icon">📊</div>
            <h3>Statystyki</h3>
            <p>Analizuj swoje nawyki słuchania i preferencje muzyczne</p>
          </div>
        </div>

        {currentTrack && currentTrack.item ? (
          <div className="currently-playing-container glass-panel">
            <h2>
              <span className="emoji-icon">🎧</span> Aktualnie odtwarzane
            </h2>
            <div className="currently-playing-content flex items-center gap-4">
              {currentTrack.item.album.images.length > 0 && (
                <img
                  src={currentTrack.item.album.images[0].url}
                  alt={`${currentTrack.item.album.name} album cover`}
                  className="album-cover"
                />
              )}
              <div className="track-details-home">
                <h3>{currentTrack.item.name}</h3>
                <p className="track-artist">
                  {currentTrack.item.artists.map((artist) => artist.name).join(', ')}
                </p>
                <p className="track-album">{currentTrack.item.album.name}</p>
                {currentTrack.is_playing ? (
                  <div className="playing-badge">
                    <span className="pulse-dot"></span> Odtwarzane
                  </div>
                ) : (
                  <div className="paused-badge">Wstrzymane</div>
                )}
              </div>
            </div>
          </div>
        ) : (
          <div className="no-track-playing glass-panel">
            <h2><span className="emoji-icon">🔇</span> Brak odtwarzanej muzyki</h2>
            <p>Włącz muzykę w Spotify, aby zobaczyć aktualnie odtwarzany utwór</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default HomePage;