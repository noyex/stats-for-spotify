import React from "react";
import { Link } from 'react-router-dom'

const NavbarSearch = ({ userId }) => {
    return (
        <nav className="nav-bar">
            <Link to={`/search-albums?id=${userId}`}>Search for Albums</Link>
            <Link to={`/search-tracks?id=${userId}`}>Search for Tracks</Link>
            <Link to={`/search-artists?id=${userId}`}>Search for Artists</Link>
        </nav>
    );
};

export default NavbarSearch;