package it.unical.web.backend.service;

import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.dao.UserInfoDAOImpl;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;
import it.unical.web.backend.persistence.proxy.UserProxy;
import it.unical.web.backend.service.Response.UserDetailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public ResponseEntity<?> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();

            UserDAOImpl userDAO = new UserDAOImpl();
            UserProxy userProxy = (UserProxy) userDAO.getUserByUsername(username);

            UserInfo userInfo = userProxy.getUserInfo();

            UserDetailResponse response = new UserDetailResponse(userInfo);
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }

    public int getCurrentUserIdByUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            String username = auth.getName();
            UserDAOImpl userDAO = new UserDAOImpl();
            UserProxy userProxy = (UserProxy) userDAO.getUserByUsername(username);
            return userProxy.getId();
        }
        throw new IllegalStateException("Utente non autenticato");
    }

    public User getUserById(int id) {
        UserDAOImpl userDAO = new UserDAOImpl();
        return userDAO.getUserById(id);
    }
}