import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/HomePage.css';

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
    <div>
      {userId && (
        <nav>
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs?id=${userId}`}>Top Songs</Link>
          <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link>   
        </nav>
      )}

      {userId ? (
        <h1>Welcome, User {userId}</h1>
      ) : (
        <p>Loading...</p>
      )}
    </div>
  );
};

export default HomePage;