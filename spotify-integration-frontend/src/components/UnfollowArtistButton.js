import React, { useState } from 'react';

const UnfollowArtistButton = ({ userId, artistId }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [isUnfollowed, setIsUnfollowed] = useState(false);

    const handleUnfollow = async () => {
        if (isLoading || isUnfollowed) return;
        
        setIsLoading(true);
        
        try {
            const response = await fetch(`/api/unfollow-artist?userId=${userId}&artistId=${artistId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                setIsUnfollowed(true);
                setTimeout(() => {
                    // Odśwież stronę po pomyślnym anulowaniu obserwacji
                    window.location.reload();
                }, 1000);
            } else {
                console.error('Nie udało się anulować obserwowania artysty');
            }
        } catch (error) {
            console.error('Błąd podczas anulowania obserwowania:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <button 
            onClick={handleUnfollow} 
            className={`action-button ${isUnfollowed ? 'success' : ''} ${isLoading ? 'loading' : ''}`}
            disabled={isLoading || isUnfollowed}
        >
            {isLoading ? (
                <span className="button-loader"></span>
            ) : isUnfollowed ? (
                <>
                    <span className="button-icon">✓</span>
                    Anulowano
                </>
            ) : (
                <>
                    <span className="button-icon">👋</span>
                    Przestań obserwować
                </>
            )}
        </button>
    );
};

export default UnfollowArtistButton;