import React, { useEffect, useState } from 'react';
import { Link, useLocation } from 'react-router-dom'; 
import { getUserRecentlyPlayed } from '../services/spotifyService';
import '../styles/RecentlyPlayed.css';
import '../styles/Global.css';

const RecentlyPlayed = () => {
  const [tracks, setTracks] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const location = useLocation();
  const userId = new URLSearchParams(location.search).get('id');

  useEffect(() => {
    const fetchRecentlyPlayed = async () => {
      try {
        console.log("Fetching recently played tracks for userId:", userId);
        const data = await getUserRecentlyPlayed(userId);
        console.log("Fetched data:", data);
        setTracks(data);
      } catch (err) {
        console.error("Error fetching recently played tracks:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (userId) {
      fetchRecentlyPlayed();
    } else {
      console.error("UserId is null or undefined.");
    }
  }, [userId]);

  return (
    <div className="recently-played-container">
      {userId && (
        <nav className="nav-bar">
          <Link to={`/home?id=${userId}`}>Home</Link>
          <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
          <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link> 
          <Link to={`/profile?id=${userId}`}>Profile</Link>
        </nav>
      )}
      <h1>Your Recently Played Tracks</h1>
      <h4>Please notice that the track must be playing at least for 30 seconds to appear on this page!</h4>
      {error && <div className="error">Error: {error}</div>}

      {loading ? (
        <div className="loading">Loading recently played tracks...</div>
      ) : (
        <ul className="tracks-list">
          {tracks.map((playHistory, index) => (
            <li key={index} className="track-item">
              <span className="track-details">
                {index + 1}. {playHistory.track.name}
              </span>{" "}
              <span className="track-artist">
                by {playHistory.track.artists.map((artist) => artist.name).join(", ")}
              </span>
              <span className="played-at">
                 Played at: {new Date(playHistory.playedAt).toLocaleString()}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default RecentlyPlayed;