export const getUserRecentlyPlayed= async (userId) => {
  try {
    const response = await fetch(`/api/user-recently-played?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania ostatnio odtwarzanych utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserRecentlyPlayed:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};

export const getUserSavedAlbums = async (userId) => {
  try {
    const response = await fetch(`/api/user-saved-album?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania zapisanych albumów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserSavedAlbums:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};
  
export const getUserTopTracksMedium = async (userId) => {
  try {
    const response = await fetch(`/api/user-top-songs-medium?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania najczęściej odtwarzanych utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserTopTracksMedium:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};

export const getUserTopTracksShort= async (userId) => {
  try {
    const response = await fetch(`/api/user-top-songs-short?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania najczęściej odtwarzanych utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserTopTracksShort:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};

export const getUserTopTracksLong= async (userId) => {
  try {
    const response = await fetch(`/api/user-top-songs-long?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania najczęściej odtwarzanych utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserTopTracksLong:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};

export const getUserCurrentlyPlayingTrack = async (userId) => {
  try {
    const response = await fetch(`/api/user-currently-playing-track?userId=${userId}`);
    if (!response.ok) {
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania aktualnie odtwarzanego utworu: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getUserCurrentlyPlayingTrack:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
};

export const getCurrentUserProfile = async (userId) => {
  try {
    const response = await fetch(`/api/current-user-profile?userId=${userId}`);
    if(!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania profilu użytkownika: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getCurrentUserProfile:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const getCurrentUserPlaylists = async (userId) => {
  try {
    const response = await fetch(`/api/current-user-playlists?userId=${userId}`);
    if(!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania playlist użytkownika: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getCurrentUserPlaylists:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const getCurrentUserFollowedArtists = async (userId) => {
  try {
    const response = await fetch(`/api/current-user-followed-artists?userId=${userId}`);
    if(!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania obserwowanych artystów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getCurrentUserFollowedArtists:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const getCurrentUserSavedTracks = async (userId) => {
  try {
    const response = await fetch(`/api/current-user-saved-tracks?userId=${userId}`);
    if(!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas pobierania zapisanych utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in getCurrentUserSavedTracks:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const searchAlbums = async (userId, query) => {
  try {
    const response = await fetch(`/api/search-albums?userId=${userId}&query=${encodeURIComponent(query)}`);
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas wyszukiwania albumów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in searchAlbums:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const searchArtists = async (userId, query) => {
  try {
    const response = await fetch(`/api/search-artists?userId=${userId}&query=${encodeURIComponent(query)}`);
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas wyszukiwania artystów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in searchArtists:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const searchTracks = async (userId, query) => {
  try {
    const response = await fetch(`/api/search-tracks?userId=${userId}&query=${encodeURIComponent(query)}`);
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas wyszukiwania utworów: ${response.status}`);
    }
    return response.json();
  } catch (error) {
    console.error('Fetch error in searchTracks:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const saveTrack = async (userId, trackId) => {
  try {
    const response = await fetch(`/api/save-track-for-current-user?userId=${userId}&trackId=${trackId}`, {method: 'PUT'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas zapisywania utworu: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in saveTrack:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const saveAlbum = async (userId, albumId) => {
  try {
    const response = await fetch(`/api/save-album-for-current-user?userId=${userId}&albumId=${albumId}`, {method: 'PUT'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas zapisywania albumu: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in saveAlbum:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const followArtist = async (userId, artistId) => {
  try {
    const response = await fetch(`/api/follow-artist-for-current-user?userId=${userId}&artistId=${artistId}`, {method: 'PUT'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas obserwowania artysty: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in followArtist:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const unfollowArtist = async (userId, artistId) => {
  try {
    const response = await fetch(`/api/unfollow-artist-for-current-user?userId=${userId}&artistId=${artistId}`, {method: 'DELETE'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas przestania obserwowania artysty: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in unfollowArtist:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const removeSavedTrack = async (userId, trackId) => {
  try {
    const response = await fetch(`/api/remove-saved-track-for-current-user?userId=${userId}&trackId=${trackId}`, {method: 'DELETE'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas usuwania zapisanego utworu: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in removeSavedTrack:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
}

export const removeSavedAlbum = async (userId, albumId) => {
  try {
    const response = await fetch(`/api/remove-saved-album-for-current-user?userId=${userId}&albumId=${albumId}`, {method: 'DELETE'});
    if (!response.ok){
      const errorText = await response.text();
      console.error('Error response from API:', errorText);
      throw new Error(`Błąd podczas usuwania zapisanego albumu: ${response.status}`);
    }
  } catch (error) {
    console.error('Fetch error in removeSavedAlbum:', error);
    throw new Error('Nie można połączyć się z serwisem - upewnij się, że serwer backend jest uruchomiony');
  }
} 