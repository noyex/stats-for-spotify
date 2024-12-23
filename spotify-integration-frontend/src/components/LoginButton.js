import React from 'react';

const LoginButton = () => {
    const handleLogin = () => {
        window.location.href = '/api/login';
    };
    return <button onClick={handleLogin}>Login with spotify</button>
}