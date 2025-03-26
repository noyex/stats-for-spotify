import React, { useState } from "react";
import { Link } from 'react-router-dom';

// Komponent ikony menu dla widoku mobilnego
const MenuIcon = ({ open, onClick }) => (
  <button className="menu-icon" onClick={onClick} aria-label={open ? 'Zamknij menu' : 'Otwórz menu'}>
    <span className={`bar ${open ? 'open' : ''}`}></span>
    <span className={`bar ${open ? 'open' : ''}`}></span>
    <span className={`bar ${open ? 'open' : ''}`}></span>
  </button>
);

const Navbar = ({ userId }) => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const toggleMobileMenu = () => {
    setMobileMenuOpen(!mobileMenuOpen);
  };

  return (
    <header className="navbar-container">
      <nav className={`nav-bar ${mobileMenuOpen ? 'mobile-open' : ''}`}>
        <div className="nav-brand">
          <Link to={`/home?id=${userId}`} className="brand-link">
            <span className="brand-icon">🎧</span>
            <span className="brand-name">Spotify Stats</span>
          </Link>
        </div>
        
        <MenuIcon open={mobileMenuOpen} onClick={toggleMobileMenu} />
        
        <div className={`nav-links ${mobileMenuOpen ? 'show' : ''}`}>
          <Link to={`/home?id=${userId}`} onClick={() => setMobileMenuOpen(false)}>
            <span className="nav-icon">🏠</span> Start
          </Link>
          <Link to={`/top-songs-medium?id=${userId}`} onClick={() => setMobileMenuOpen(false)}>
            <span className="nav-icon">🔥</span> Top utwory
          </Link>
          <Link to={`/user-recently-played?id=${userId}`} onClick={() => setMobileMenuOpen(false)}>
            <span className="nav-icon">🕒</span> Ostatnio odtwarzane
          </Link>
          <Link to={`/profile?id=${userId}`} onClick={() => setMobileMenuOpen(false)}>
            <span className="nav-icon">👤</span> Profil
          </Link>
          <Link to={`/search-albums?id=${userId}`} onClick={() => setMobileMenuOpen(false)}>
            <span className="nav-icon">🔍</span> Wyszukaj
          </Link>
        </div>
      </nav>
    </header>
  );
};

export default Navbar;