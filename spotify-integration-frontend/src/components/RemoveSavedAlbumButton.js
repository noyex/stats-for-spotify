import React, { useState } from 'react';

const RemoveSavedAlbumButton = ({ userId, albumId }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [isRemoved, setIsRemoved] = useState(false);

    const handleRemoveAlbum = async () => {
        if (isLoading || isRemoved) return;
        
        setIsLoading(true);
        
        try {
            const response = await fetch(`/api/remove-saved-album?userId=${userId}&albumId=${albumId}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                setIsRemoved(true);
                setTimeout(() => {
                    // Odśwież stronę po pomyślnym usunięciu
                    window.location.reload();
                }, 1000);
            } else {
                console.error('Nie udało się usunąć albumu');
            }
        } catch (error) {
            console.error('Błąd podczas usuwania albumu:', error);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <button 
            onClick={handleRemoveAlbum} 
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
                    Usuń album
                </>
            )}
        </button>
    );
};

export default RemoveSavedAlbumButton;