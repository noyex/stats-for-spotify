import React from "react";
import { Link } from 'react-router-dom'

const Navbar = ({ userId }) => {
    return (
        <nav className="nav-bar">
            <Link to={`/home?id=${userId}`}>Home</Link>
            <Link to={`/top-songs-medium?id=${userId}`}>Top Songs</Link>
            <Link to={`/user-recently-played?id=${userId}`}>Recently Played</Link>
            <Link to={`/profile?id=${userId}`}>Profile</Link>
            <Link to={`/search?id=${userId}`}>Search</Link>
        </nav>
    );
};

export default Navbar;