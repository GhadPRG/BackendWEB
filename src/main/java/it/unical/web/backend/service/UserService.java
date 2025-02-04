package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.dao.UserInfoDAOImpl;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;
import it.unical.web.backend.service.Response.UserDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseEntity<?> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            UserInfoDAOImpl userDAO = new UserInfoDAOImpl();
            UserInfo user = userDAO.getUserInfoByUsername(userDetails.getUsername());

            UserDetailResponse response = new UserDetailResponse(user);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }
}
