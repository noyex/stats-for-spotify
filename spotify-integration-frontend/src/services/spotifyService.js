export const getUserSavedAlbums = async (userId) => {
    const response = await fetch(`/api/user-saved-album?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch saved albums');
    }
    return response.json();
  };
  
  export const getUserTopTracks = async (userId) => {
    const response = await fetch(`/api/user-top-songs?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Failed to fetch top tracks');
    }
    return response.json();
  };