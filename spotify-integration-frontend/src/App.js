import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage'; 
import HomePage from './pages/HomePage';
import TopSongsMedium from './pages/TopSongsMedium';
import TopSongsShort from './pages/TopSongsShort';
import TopSongsLong from './pages/TopSongsLong';
import SavedAlbumsPage from './pages/SavedAlbumsPage';
import PlaybackHistory from './pages/RecentlyPlayed';
import RecentlyPlayed from './pages/RecentlyPlayed';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} /> 
        <Route path="/home" element={<HomePage />} />
        <Route path="/top-songs-medium" element={<TopSongsMedium />} />
        <Route path="/top-songs-short" element={<TopSongsShort />} />
        <Route path="/top-songs-long" element={<TopSongsLong />} />
        <Route path="/saved-albums" element={<SavedAlbumsPage />} />
        <Route path="/user-recently-played" element={<RecentlyPlayed />} />
      </Routes>
    </Router>
  );
};

export default App;