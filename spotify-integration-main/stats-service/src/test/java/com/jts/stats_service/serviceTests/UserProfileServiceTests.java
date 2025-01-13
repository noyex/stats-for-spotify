package com.jts.stats_service.serviceTests;

import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.repositories.UserDetailsRepository;
import com.jts.stats_service.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.michaelthelin.spotify.model_objects.specification.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileServiceTests {

    @Mock
    private UserDetailsRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertOrUpdateUserDetailsInsertNewUser() {
        User spotifyUser = mock(User.class);
        when(spotifyUser.getId()).thenReturn("userId123");
        when(spotifyUser.getDisplayName()).thenReturn("Test User");
        when(spotifyUser.getEmail()).thenReturn("testuser@example.com");

        String accessToken = "access_token";
        String refreshToken = "refresh_token";

        when(userRepository.findByRefId("userId123")).thenReturn(null);
        when(userRepository.save(any(UserDetails.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetails result = userProfileService.insertOrUpdateUserDetails(spotifyUser, accessToken, refreshToken);

        verify(userRepository, times(1)).findByRefId("userId123");
        verify(userRepository, times(1)).save(any(UserDetails.class));

        assertNotNull(result);
        assertEquals("userId123", result.getRefId());
        assertEquals("Test User", result.getUsername());
        assertEquals("testuser@example.com", result.getEmail());
        assertEquals("access_token", result.getAccessToken());
        assertEquals("refresh_token", result.getRefreshToken());
    }

    @Test
    void testInsertOrUpdateUserDetailsUpdateExistingUser() {
        User spotifyUser = mock(User.class);
        when(spotifyUser.getId()).thenReturn("userId123");
        when(spotifyUser.getDisplayName()).thenReturn("Updated User");
        when(spotifyUser.getEmail()).thenReturn("updateduser@example.com");

        String accessToken = "new_access_token";
        String refreshToken = "new_refresh_token";

        UserDetails existingUserDetails = new UserDetails();
        existingUserDetails.setRefId("userId123");
        existingUserDetails.setUsername("Old User");
        existingUserDetails.setEmail("olduser@example.com");
        existingUserDetails.setAccessToken("old_access_token");
        existingUserDetails.setRefreshToken("old_refresh_token");

        when(userRepository.findByRefId("userId123")).thenReturn(existingUserDetails);
        when(userRepository.save(any(UserDetails.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetails result = userProfileService.insertOrUpdateUserDetails(spotifyUser, accessToken, refreshToken);

        verify(userRepository, times(1)).findByRefId("userId123");
        verify(userRepository, times(1)).save(existingUserDetails);

        assertNotNull(result);
        assertEquals("userId123", result.getRefId());
        assertEquals("Updated User", result.getUsername());
        assertEquals("updateduser@example.com", result.getEmail());
        assertEquals("new_access_token", result.getAccessToken());
        assertEquals("new_refresh_token", result.getRefreshToken());
    }
}