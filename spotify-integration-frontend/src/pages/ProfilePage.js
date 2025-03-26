import React, { useEffect, useState } from 'react';
import LogoutButton from '../components/LogoutButton';
import SpotifyProfileButton from '../components/SpotifyProfileButton';
import Navbar from '../components/Navbar';
import { getCurrentUserProfile, getCurrentUserPlaylists, getCurrentUserFollowedArtists, getUserSavedAlbums, getCurrentUserSavedTracks } from '../services/spotifyService';
import '../styles/Global.css';
import '../styles/ProfilePage.css';
import UnfollowArtistButton from '../components/UnfollowArtistButton';
import RemoveSavedTrackButton from '../components/RemoveSavedTrackButton';
import RemoveSavedAlbumButton from '../components/RemoveSavedAlbumButton';

const ProfilePage = () => {
    const [userId, setUserId] = useState(null);
    const [profile, setProfile] = useState(null);
    const [playlists, setPlaylists] = useState([]);
    const [artists, setArtists] = useState([]);
    const [albums, setAlbums] = useState([]);
    const [tracks, setTracks] = useState([]);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('profile');

    useEffect(() => {
        const urlParams = new URLSearchParams(window.location.search);
        const id = urlParams.get('id');
        if (id) {
            setUserId(id);
            fetchUserProfile(id);
            fetchUserPlaylists(id);
            fetchUserFollowedArtists(id);
            fetchUserSavedAlbums(id);
            fetchUserSavedTracks(id);
        }
    }, []);

    const fetchUserProfile = async (id) => {
        try {
            const data = await getCurrentUserProfile(id); 
            setProfile(data);
        } catch (err) {
            setError("Nie udało się załadować profilu użytkownika.");
            console.error(err);
        }
    };

    const fetchUserPlaylists = async (id) => {
        try {
            const data = await getCurrentUserPlaylists(id); 
            setPlaylists(data);
        } catch (err) {
            setError("Nie udało się załadować playlist.");
            console.error(err);
        }
    };

    const fetchUserFollowedArtists = async (id) => {
        try {
            const data = await getCurrentUserFollowedArtists(id);
            setArtists(data);
        } catch (err) {
            setError("Nie udało się załadować obserwowanych artystów.");
            console.error(err);
        }
    };

    const fetchUserSavedAlbums = async (id) => {
        try {
            const data = await getUserSavedAlbums(id);
            setAlbums(data);
        } catch (err) {
            setError("Nie udało się załadować zapisanych albumów.");
            console.error(err);
        }
    };

    const fetchUserSavedTracks = async (id) => {
        try {
            const data = await getCurrentUserSavedTracks(id);
            setTracks(data);
        } catch (err) {
            setError("Nie udało się załadować zapisanych utworów.");
            console.error(err);
        }
    }

    return (
        <div className="profile-page">
            {userId && <Navbar userId={userId} />}
            
            <div className="container">
                {error && <div className="error-message">{error}</div>}
                
                <div className="profile-header glass-panel">
                    {profile ? (
                        <div className="profile-info-container">
                            <div className="profile-avatar-container">
                                <img
                                    src={profile.images?.[0]?.url || '/placeholder.png'}
                                    alt="Profil"
                                    className="profile-avatar"
                                />
                                <div className="profile-status">Premium</div>
                            </div>
                            <div className="profile-details">
                                <h1>{profile.display_name || `${userId}`}</h1>
                                <div className="profile-stats">
                                    <div className="stat-item">
                                        <span className="stat-value">{profile.followers.total || '0'}</span>
                                        <span className="stat-label">Obserwujących</span>
                                    </div>
                                    <div className="stat-item">
                                        <span className="stat-value">{playlists.length}</span>
                                        <span className="stat-label">Playlisty</span>
                                    </div>
                                    <div className="stat-item">
                                        <span className="stat-value">{tracks.length}</span>
                                        <span className="stat-label">Utwory</span>
                                    </div>
                                </div>
                                <div className="profile-actions">
                                    <SpotifyProfileButton userId={userId} />
                                    <LogoutButton userId={userId} />
                                </div>
                                <div className="profile-meta">
                                    <div className="meta-item">
                                        <span className="meta-icon">🌍</span>
                                        <span>{profile.country || 'N/A'}</span>
                                    </div>
                                    <div className="meta-item">
                                        <span className="meta-icon">💌</span>
                                        <span>{profile.email || 'N/A'}</span>
                                    </div>
                                    <div className="meta-item">
                                        <span className="meta-icon">💎</span>
                                        <span>{profile.product || 'N/A'}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="loading">Ładowanie profilu...</div>
                    )}
                </div>
                
                <div className="profile-tabs">
                    <button 
                        className={`tab-button ${activeTab === 'profile' ? 'active' : ''}`}
                        onClick={() => setActiveTab('profile')}
                    >
                        <span className="tab-icon">👤</span> Profil
                    </button>
                    <button 
                        className={`tab-button ${activeTab === 'playlists' ? 'active' : ''}`}
                        onClick={() => setActiveTab('playlists')}
                    >
                        <span className="tab-icon">📋</span> Playlisty
                    </button>
                    <button 
                        className={`tab-button ${activeTab === 'artists' ? 'active' : ''}`}
                        onClick={() => setActiveTab('artists')}
                    >
                        <span className="tab-icon">🎤</span> Artyści
                    </button>
                    <button 
                        className={`tab-button ${activeTab === 'albums' ? 'active' : ''}`}
                        onClick={() => setActiveTab('albums')}
                    >
                        <span className="tab-icon">💿</span> Albumy
                    </button>
                    <button 
                        className={`tab-button ${activeTab === 'tracks' ? 'active' : ''}`}
                        onClick={() => setActiveTab('tracks')}
                    >
                        <span className="tab-icon">🎵</span> Utwory
                    </button>
                </div>
                
                <div className="tab-content">
                    {activeTab === 'profile' && (
                        <div className="profile-summary glass-panel">
                            <h2>Podsumowanie konta</h2>
                            <p>Witaj w swoim profilu Spotify Stats. Tutaj znajdziesz wszystkie informacje o swojej aktywności na Spotify, ulubionych artystach, albumach i utworach.</p>
                            <div className="account-tips">
                                <h3>Porady</h3>
                                <ul className="tips-list">
                                    <li>Słuchaj więcej swoich ulubionych artystów, aby ulepszyć rekomendacje</li>
                                    <li>Twórz playlisty dla różnych okazji i nastrojów</li>
                                    <li>Zapisuj swoje ulubione utwory, by móc do nich wrócić później</li>
                                </ul>
                            </div>
                        </div>
                    )}
                    
                    {activeTab === 'playlists' && (
                        <div className="playlists-container">
                            <h2>Twoje playlisty</h2>
                            {playlists.length > 0 ? (
                                <div className="playlists-grid grid grid-3">
                                    {playlists.map((playlist) => (
                                        <div key={playlist.id} className="playlist-card card">
                                            <div className="playlist-image-container">
                                                <img
                                                    src={playlist.images?.[0]?.url || '/placeholder.png'}
                                                    alt={playlist.name}
                                                    className="playlist-image"
                                                />
                                                <div className="playlist-overlay">
                                                    <span className="tracks-count">{playlist.tracks.total} utworów</span>
                                                </div>
                                            </div>
                                            <div className="playlist-info">
                                                <h3 className="playlist-name" title={playlist.name}>{playlist.name}</h3>
                                                <p className="playlist-description">{playlist.description || "Brak opisu"}</p>
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="no-content-message">Nie masz żadnych playlist.</p>
                            )}
                        </div>
                    )}
                    
                    {activeTab === 'artists' && (
                        <div className="artists-container">
                            <h2>Obserwowani artyści</h2>
                            {artists.length > 0 ? (
                                <div className="artists-grid grid grid-3">
                                    {artists.map((artist) => (
                                        <div key={artist.id} className="artist-card card">
                                            <div className="artist-image-container">
                                                <img
                                                    src={artist.images?.[0]?.url || '/placeholder.png'}
                                                    alt={artist.name}
                                                    className="artist-image"
                                                />
                                            </div>
                                            <div className="artist-info">
                                                <h3 className="artist-name">{artist.name}</h3>
                                                <p className="artist-followers">{artist.followers.total.toLocaleString()} obserwujących</p>
                                                <div className="artist-genres">
                                                    {artist.genres.slice(0, 3).map((genre, index) => (
                                                        <span key={index} className="genre-tag">{genre}</span>
                                                    ))}
                                                </div>
                                                <UnfollowArtistButton userId={userId} artistId={artist.id} />
                                            </div>
                                        </div>
                                    ))}
                                </div>
                            ) : (
                                <p className="no-content-message">Nie obserwujesz żadnych artystów.</p>
                            )}
                        </div>
                    )}
                    
                    {activeTab === 'albums' && (
                        <div className="albums-container">
                            <h2>Zapisane albumy</h2>
                            {albums.length > 0 ? (
                                <div className="albums-grid grid grid-3">
                                    {albums.map((item, index) => {
                                        const album = item.album;
                                        const artistNames = album.artists.map((artist) => artist.name).join(", ");
                                        return (
                                            <div key={index} className="album-card card">
                                                <div className="album-image-container">
                                                    <img
                                                        src={album.images?.[0]?.url || '/placeholder.png'}
                                                        alt={album.name}
                                                        className="album-image"
                                                    />
                                                    <div className="album-overlay">
                                                        <span className="album-year">{new Date(album.release_date).getFullYear()}</span>
                                                    </div>
                                                </div>
                                                <div className="album-info">
                                                    <h3 className="album-name" title={album.name}>{album.name}</h3>
                                                    <p className="album-artist">{artistNames}</p>
                                                    <p className="album-tracks">{album.tracks.total} utworów</p>
                                                    <RemoveSavedAlbumButton userId={userId} albumId={album.id} />
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            ) : (
                                <p className="no-content-message">Nie masz zapisanych albumów.</p>
                            )}
                        </div>
                    )}
                    
                    {activeTab === 'tracks' && (
                        <div className="tracks-container">
                            <h2>Zapisane utwory</h2>
                            {tracks.length > 0 ? (
                                <div className="tracks-list">
                                    {tracks.map((item, index) => {
                                        const track = item.track;
                                        const artistNames = track.artists.map((artist) => artist.name).join(", ");
                                        return (
                                            <div key={index} className="track-item glass-panel">
                                                <div className="track-number">{index + 1}</div>
                                                <div className="track-image-container">
                                                    <img
                                                        src={track.album?.images?.[0]?.url || '/placeholder.png'}
                                                        alt={track.name}
                                                        className="track-image"
                                                    />
                                                </div>
                                                <div className="track-info">
                                                    <h3 className="track-name">{track.name}</h3>
                                                    <p className="track-artist">{artistNames}</p>
                                                </div>
                                                <div className="track-album">{track.album.name}</div>
                                                <div className="track-duration">
                                                    {Math.floor(track.duration_ms / 60000)}:{((track.duration_ms % 60000) / 1000).toFixed(0).padStart(2, '0')}
                                                </div>
                                                <div className="track-actions">
                                                    <RemoveSavedTrackButton userId={userId} trackId={track.id} />
                                                </div>
                                            </div>
                                        );
                                    })}
                                </div>
                            ) : (
                                <p className="no-content-message">Nie masz zapisanych utworów.</p>
                            )}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default ProfilePage;