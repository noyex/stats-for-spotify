package com.jts.stats_service.service;

import com.jts.stats_data.entity.UserDetails;
import com.jts.stats_data.entity.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import se.michaelthelin.spotify.model_objects.specification.User;

import java.util.Objects;

@Service
public class UserProfileService {

	@Autowired
	private UserDetailsRepository userRepository;

	public UserDetails insertOrUpdateUserDetails(User user, String accessToken, String refreshToken) {
		UserDetails userDetails = userRepository.findByRefId(user.getId());

		if(Objects.isNull(userDetails)) {
			userDetails = new UserDetails();
		}
		userDetails.setAccessToken(accessToken);
		userDetails.setRefreshToken(refreshToken);
		userDetails.setRefId(user.getId());
		userDetails.setUsername(user.getDisplayName());
		userDetails.setEmail(user.getEmail());
		return userRepository.save(userDetails);
	}
}
