import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom'; 
import { getUserTopTracksLong} from '../services/spotifyService';
import '../styles/TopSongs.css'; 
import '../styles/Global.css'; 

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
        const data = await getUserTopTracksLong(userId);
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
          <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
          <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link>   
          <Link to={`/profile?id=${userId}`}>Profile</Link>      
          </nav>
      )}
      <h1>Your Top Songs From Last 12 Months!</h1>
        <nav className="nav-bar">
          <Link to={`/top-songs-short?id=${userId}`}>Last 4 weeks</Link>
          <Link to={`/top-songs-medium?id=${userId}`}>Last 6 months</Link>
          <Link to={`/top-songs-long?id=${userId}`}>Last 12 months</Link>
        </nav>      
      
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