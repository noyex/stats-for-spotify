import React, { useState } from 'react';

const RemoveSavedTrackButton = ({ userId, trackId }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [isRemoved, setIsRemoved] = useState(false);

    const handleRemoveTrack = async () => {
        if (isLoading || isRemoved) return;
        
        setIsLoading(true);
        
        try {
            const response = await fetch(`/api/remove-saved-track?userId=${userId}&trackId=${trackId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                setIsRemoved(true);
                setTimeout(() => {
                    // Odśwież stronę po pomyślnym usunięciu
                    window.location.reload();
                }, 1000);
            } else {
                console.error('Nie udało się usunąć utworu');
            }
        } catch (error) {
            console.error('Błąd podczas usuwania utworu:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <button 
            onClick={handleRemoveTrack} 
            className={`action-button ${isRemoved ? 'success' : ''} ${isLoading ? 'loading' : ''}`}
            disabled={isLoading || isRemoved}
        >
            {isLoading ? (
                <span className="button-loader"></span>
            ) : isRemoved ? (
                <>
                    <span className="button-icon">✓</span>
                    Usunięto
                </>
            ) : (
                <>
                    <span className="button-icon">🗑️</span>
                    Usuń
                </>
            )}
        </button>
    );
};

export default RemoveSavedTrackButton;