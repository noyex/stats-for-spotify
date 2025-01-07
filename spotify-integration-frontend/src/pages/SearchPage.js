import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
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
    }
  }, [userId]);

  return (
    <div className="home-page">
      {userId && (
        <nav className="nav-bar">
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
          <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link>
          <Link to={`/profile?id=${userId}`}>Profile</Link>
          <Link to={`/search?id=${userId}`}>Search</Link>
        </nav>
      )}

      <h1 className="welcome-message">Search for Item</h1>
    </div>
  );
};

export default HomePage;