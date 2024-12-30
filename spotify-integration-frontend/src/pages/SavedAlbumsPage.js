import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { getUserSavedAlbums } from '../services/spotifyService';
import '../styles/SavedAlbumsPage.css'; 

const SavedAlbumsPage = () => {
  const [albums, setAlbums] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true); 

  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  useEffect(() => {
    const fetchSavedAlbums = async () => {
      try {
        console.log("Fetching saved albums for userId:", userId);
        const data = await getUserSavedAlbums(userId); 
        console.log("Fetched albums:", data);
        setAlbums(data); 
      } catch (err) {
        console.error("Error fetching saved albums:", err);
        setError(err.message); 
      } finally {
        setLoading(false); 
      }
    };

    if (userId) {
      fetchSavedAlbums();
    } else {
      console.error("UserId is null or undefined.");
    }
  }, [userId]);

  return (
    <div className="saved-albums-container">
      {userId && (
        <nav className="nav-bar">
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
          <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link> 
          <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link>         </nav>
      )}

      <h1>Your Saved Albums</h1>

      {error && <div className="error">Error: {error}</div>}

      {loading ? (
        <div className="loading">Loading saved albums...</div>
      ) : (
        <ul className="albums-list">
          {albums.map((item, index) => {
            const album = item.album;
            return (
              <li key={index} className="album-item">
                {album.images && album.images.length > 0 ? (
                  <img
                    src={album.images[0].url}
                    alt={album.name}
                    className="album-image"
                  />
                ) : (
                  <div className="album-placeholder">No Image</div>
                )}
                <p className="album-name">{album.name}</p>
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
};

export default SavedAlbumsPage;