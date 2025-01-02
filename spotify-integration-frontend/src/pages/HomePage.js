import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css'; 

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
          <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link>
          <Link to={`/user-recently-played?id=${userId}`}>Playback History</Link>
          <Link to={`/profile?id=${userId}`}>Profile</Link>
        </nav>
      )}

      <h1 className="welcome-message">Welcome to Spotify Stats!</h1>

      {currentTrack && currentTrack.item ? (
        <div className="currently-playing-container">
          {currentTrack.item.album.images.length > 0 && (
            <img
              src={currentTrack.item.album.images[0].url}
              alt={`${currentTrack.item.album.name} album cover`}
              className="album-cover"
            />
          )}
          <div className="track-details">
            <h2>Currently Playing</h2>
            <p>
              <strong>{currentTrack.item.name}</strong> by{' '}
              {currentTrack.item.artists.map((artist) => artist.name).join(', ')}
            </p>
          </div>
        </div>
      ) : (
        <p>No track is currently playing.</p>
      )}
    </div>
  );
};

export default HomePage;