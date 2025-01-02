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
