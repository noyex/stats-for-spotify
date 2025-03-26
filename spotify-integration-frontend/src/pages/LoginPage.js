import React from 'react';
import '../styles/LoginPage.css'; 
import '../styles/Global.css'; 

const LoginPage = () => {
  const handleLogin = async () => {
    try {
      const response = await fetch('/api/login');
      const loginUrl = await response.text();
      window.location.href = loginUrl;
    } catch (error) {
      console.error('Error fetching login URL:', error);
    }
  };

  return (
    <div className="login-page">
      <div className="login-background">
        <div className="animated-bg-circle circle-1"></div>
        <div className="animated-bg-circle circle-2"></div>
        <div className="animated-bg-circle circle-3"></div>
      </div>
      
      <div className="login-container glass-panel">
        <div className="login-logo">
          <span className="spotify-icon">🎧</span>
        </div>
        <h1>Spotify Stats</h1>
        <p className="login-subtitle">Odkryj swoje muzyczne preferencje i statystyki</p>
        
        <div className="features-list">
          <div className="feature-item">
            <span className="feature-icon">🎵</span>
            <span>Twoje ulubione utwory</span>
          </div>
          <div className="feature-item">
            <span className="feature-icon">👤</span>
            <span>Informacje o profilu</span>
          </div>
          <div className="feature-item">
            <span className="feature-icon">🔍</span>
            <span>Wyszukiwanie muzyki</span>
          </div>
        </div>
        
        <button className="login-button" onClick={handleLogin}>
          <span className="login-icon">🔒</span>
          Zaloguj się przez Spotify
        </button>
        
        <p className="privacy-note">Bezpieczne logowanie przez API Spotify</p>
      </div>
    </div>
  );
};

export default LoginPage;