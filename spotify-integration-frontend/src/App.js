import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import LoginPage from './pages/LoginPage'; 
import HomePage from './pages/HomePage';
import TopSongs from './pages/TopSongs';
import SavedAlbumsPage from './pages/SavedAlbumsPage';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} /> 
        <Route path="/home" element={<HomePage />} />
        <Route path="/top-songs" element={<TopSongs />} />
        <Route path="/saved-albums" element={<SavedAlbumsPage />} />
      </Routes>
    </Router>
  );
};

export default App;