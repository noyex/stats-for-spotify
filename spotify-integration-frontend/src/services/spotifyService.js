export const getUserSavedAlbums = async (userId) => {
    const response = await fetch(`/api/user-saved-album?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch saved albums');
    }
    return response.json();
  };
  
  export const getUserTopTracksMedium = async (userId) => {
    const response = await fetch(`/api/user-top-songs-medium?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch top tracks');
    }
    return response.json();
  };

  export const getUserTopTracksShort= async (userId) => {
    const response = await fetch(`/api/user-top-songs-short?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch top tracks');
    }
    return response.json();
  };

  export const getUserTopTracksLong= async (userId) => {
    const response = await fetch(`/api/user-top-songs-long?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch top tracks');
    }
    return response.json();
  };

  export const getUserRecentlyPlayed= async (userId) => {
    const response = await fetch(`/api/user-recently-played?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch tracks');
    }
    return response.json();
  };

  export const getUserCurrentlyPlayingTrack = async (userId) => {
  const response = await fetch(`/api/user-currently-playing-track?userId=${userId}`);
  if (!response.ok) {
    throw new Error('Failed to fetch currently playing track');
  }
  return response.json();
};

export const getCurrentUserProfile = async (userId) => {
  const response = await fetch(`/api/current-user-profile?userId=${userId}`);
  if(!response.ok){
    throw new Error(`Failed to get user's profile`);
  }
  return response.json();
}

export const getCurrentUserPlaylists = async (userId) => {
  const response = await fetch(`/api/current-user-playlists?userId=${userId}`);
  if(!response.ok){
    throw new Error(`Failed to fetch current user's playlists`)
  }
  return response.json();
}

export const getCurrentUserFollowedArtists = async (userId) => {
  const response = await fetch(`/api/current-user-followed-artists?userId=${userId}`);
  if(!response.ok){
    throw new Error(`Failed to fetch current user's followed artists`)
  }
  return response.json();
}

export const getCurrentUserSavedTracks = async (userId) => {
  const response = await fetch(`/api/current-user-saved-tracks?userId=${userId}`);
  if(!response.ok){
    throw new Error(`Failed to fetch current user's saved tracks`)
  }
  return response.json();
}
