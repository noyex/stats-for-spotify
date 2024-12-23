import React from 'react';
import '../styles/LoginPage.css'; // Import pliku CSS

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
      <div className="login-container">
        <h1>Welcome to Spotify Stats</h1>
        <p>Please log in to view your Spotify data.</p>
        <button onClick={handleLogin}>Log in with Spotify</button>
      </div>
    </div>
  );
};

export default LoginPage;