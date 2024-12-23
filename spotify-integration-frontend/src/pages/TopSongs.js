import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom'; 
import { getUserTopTracks } from '../services/spotifyService';
import '../styles/TopSongs.css'; 

const TopSongs = () => {
  const [tracks, setTracks] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  useEffect(() => {
    const fetchTopTracks = async () => {
      try {
        console.log("Fetching top tracks for userId:", userId);
        const data = await getUserTopTracks(userId);
        console.log("Fetched data:", data); 
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
      {userId && (
        <nav className="nav-bar">
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs?id=${userId}`}>Top Songs</Link>
          <Link to={`/saved-albums?id=${userId}`}>Saved Albums</Link>
        </nav>
      )}
      <h1>Your Top Songs</h1>
  
      {error && <div className="error">Error: {error}</div>}
  
      {loading ? (
        <div className="loading">Loading top tracks...</div>
      ) : (
        <ul className="tracks-list">
          {tracks.map((track, index) => (
            <li key={track.id} className="track-item">
              {track.album?.images && track.album.images.length > 0 && (
                <img
                  src={track.album.images[0].url}
                  alt={track.name}
                  className="track-image"
                />
              )}
              <strong>
                {index + 1}. {track.name}
              </strong>{" "}
              by {track.artists.map((artist) => artist.name).join(", ")}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default TopSongs;