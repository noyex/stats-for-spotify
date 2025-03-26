import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom'; 
import Navbar from '../components/Navbar';
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
        const data = await getUserRecentlyPlayed(userId);
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

  // Funkcja pomocnicza do formatowania czasu "jak dawno temu"
  const getTimeAgo = (dateString) => {
    const playedAt = new Date(dateString);
    const now = new Date();
    const diffMs = now - playedAt;
    const diffMins = Math.floor(diffMs / 60000);
    
    if (diffMins < 1) return "przed chwilą";
    if (diffMins < 60) return `${diffMins} min temu`;
    
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours < 24) return `${diffHours} godz temu`;
    
    const diffDays = Math.floor(diffHours / 24);
    return `${diffDays} dni temu`;
  };

  // Funkcja pomocnicza do formatowania daty
  const formatDate = (dateString) => {
    const options = { 
      day: 'numeric', 
      month: 'short', 
      hour: '2-digit', 
      minute: '2-digit' 
    };
    return new Date(dateString).toLocaleDateString('pl-PL', options);
  };

  // Funkcja grupująca utwory według dni
  const groupTracksByDay = (tracks) => {
    const groupedTracks = {};
    
    tracks.forEach(playHistory => {
      const date = new Date(playHistory.playedAt);
      const dayKey = date.toLocaleDateString('pl-PL', { year: 'numeric', month: 'long', day: 'numeric' });
      
      if (!groupedTracks[dayKey]) {
        groupedTracks[dayKey] = [];
      }
      
      groupedTracks[dayKey].push(playHistory);
    });
    
    return groupedTracks;
  };

  return (
    <div className="recently-played-container">
      {userId && <Navbar userId={userId} />}
      
      <div className="container">
        <div className="recent-tracks-header">
          <h1 className="recent-tracks-title">
            <span className="emoji-icon">🕒</span> Ostatnio odtwarzane
          </h1>
          <p className="recent-tracks-subtitle">
            Historia utworów, których słuchałeś ostatnio. Utwór musi być odtwarzany przez co najmniej 30 sekund, aby pojawił się na liście.
          </p>
        </div>
        
        {error && <div className="error-message">Błąd: {error}</div>}
        
        {loading ? (
          <div className="loading">Ładowanie historii odtwarzania...</div>
        ) : tracks.length === 0 ? (
          <div className="empty-history">
            <div className="empty-history-icon">🎵</div>
            <h2 className="empty-history-message">Brak ostatnio odtwarzanych utworów</h2>
            <p className="empty-history-suggestion">
              Posłuchaj muzyki w Spotify, aby zobaczyć swoją historię odtwarzania
            </p>
          </div>
        ) : (
          <div className="tracks-timeline">
            {Object.entries(groupTracksByDay(tracks)).map(([day, dayTracks]) => (
              <React.Fragment key={day}>
                <div className="day-divider">
                  <span className="day-label">{day}</span>
                </div>
                
                {dayTracks.map((playHistory, index) => (
                  <div key={`${playHistory.track.id}-${index}`} className="track-history-item glass-panel">
                    <div className="track-number">{index + 1}</div>
                    
                    <div className="track-image-container">
                      {playHistory.track.album?.images && playHistory.track.album.images.length > 0 ? (
                        <img
                          src={playHistory.track.album.images[0].url}
                          alt={playHistory.track.name}
                          className="track-image"
                        />
                      ) : (
                        <div className="track-image-placeholder"></div>
                      )}
                    </div>
                    
                    <div className="track-details">
                      <h3 className="track-name">{playHistory.track.name}</h3>
                      <p className="track-artist">
                        {playHistory.track.artists.map(artist => artist.name).join(', ')}
                      </p>
                      <p className="track-album">{playHistory.track.album.name}</p>
                    </div>
                    
                    <div className="played-at-container">
                      <span className="played-date">{formatDate(playHistory.playedAt)}</span>
                      <div className="time-ago">{getTimeAgo(playHistory.playedAt)}</div>
                    </div>
                  </div>
                ))}
              </React.Fragment>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default RecentlyPlayed;