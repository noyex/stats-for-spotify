import React from 'react';

const LogoutButton = ({ userId }) => {
    const handleLogout = async () => {
        try {
            const response = await fetch(`/api/logout?userId=${userId}`, { method: 'POST' });
            if (response.ok) {
                window.location.href = '/'; 
            } else {
                console.error('Failed to logout');
            }
        } catch (error) {
            console.error('Error during logout:', error);
        }
    };

    return <button onClick={handleLogout} className="logout-button">Logout</button>;
};

export default LogoutButton;