import React from 'react';

const LogoutButton = ({ userId }) => {
    const handleLogout = async () => {
        try {
            // Usuń dane sesji użytkownika
            await fetch(`/api/logout?userId=${userId}`, {
                method: 'POST'
            });
            
            // Przekieruj do strony logowania
            window.location.href = '/';
        } catch (error) {
            console.error('Error logging out:', error);
        }
    };

    return (
        <button onClick={handleLogout} className="logout-button">
            <span className="button-icon">🚪</span>
            Wyloguj się
        </button>
    );
};

export default LogoutButton;