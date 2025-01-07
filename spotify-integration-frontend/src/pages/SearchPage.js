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
    }
  }, [userId]);

  return (
    <div className="home-page">
      {userId && <Navbar userId={userId} />}

      <h1 className="welcome-message">Search for Item</h1>
    </div>
  );
};

export default HomePage;