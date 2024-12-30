import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css'; // Import pliku CSS

const HomePage = () => {
  const [userId, setUserId] = useState(null);

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    if (id) {
      setUserId(id);
    }
  }, []);

  return (
    <div className="home-page">
      {userId && (
        <nav className="nav-bar">
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs?id=${userId}`}>Top Songs</Link>
          <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link>   
        </nav>
      )}

      {userId ? (
        <h1 className="welcome-message">Welcome, {userId}</h1>
      ) : (
        <p className="loading-message">Loading...</p>
      )}
    </div>
  );
};

export default HomePage;