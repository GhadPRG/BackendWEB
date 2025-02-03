package it.unical.web.backend.service;

import it.unical.web.backend.config.security.SecurityConfig;
import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.RegexHandler;
import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.service.Request.AuthenticationRequest;
import it.unical.web.backend.service.Request.RegistrationRequest;
import it.unical.web.backend.service.Response.JWTResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {
    private JWTService jwtService;

    public AuthService(JWTService jwtService) {
        this.jwtService = jwtService;
    }


    // -1 non valido
    // 0 tutto ok
    // 1 username, first name, last name, gender
    // 2 pwd
    private int parseRegistationRequest(RegistrationRequest registrationRequest) {

        if (registrationRequest == null || registrationRequest.getEmail() == null || registrationRequest.getPassword() == null
                || registrationRequest.getFirstname() == null || registrationRequest.getLastname() == null || registrationRequest.getUsername() == null
                || registrationRequest.getGender() == null) {
            return -1;
        }
        String username = registrationRequest.getUsername();
        String firstname = registrationRequest.getFirstname();
        String lastname = registrationRequest.getLastname();
        String gender = registrationRequest.getGender();
        if (!RegexHandler.getInstance().isAValidUsername(username) || !RegexHandler.getInstance().onlyCharacters(firstname)
                || !RegexHandler.getInstance().onlyCharacters(lastname) || !RegexHandler.getInstance().onlyCharacters(gender))
        {
            return 1;
        }


        String password = registrationRequest.getPassword();
        if (!RegexHandler.getInstance().isAValidPassword(password)) {
            return 2;
        }


        String email = registrationRequest.getEmail();
        if (!RegexHandler.getInstance().isAValidEmail(email)) {
            return 3;
        }

        String birthDate = registrationRequest.getBirthDate();
        if (!RegexHandler.getInstance().isAValidDate(birthDate)) {
            return 4;
        }

        float height = registrationRequest.getHeight();
        float weight = registrationRequest.getWeight();
        if (height <= 0 || weight <= 0 ) {
            return 5;
        }

        int dailyCalories = registrationRequest.getDailyCalories();
        if (dailyCalories <= 0) {
            return 6;
        }

        return 0;
    }


    public ResponseEntity<?> registerUser(RegistrationRequest registrationRequest) {
        try {
            int status = parseRegistationRequest(registrationRequest);
            if (status == 0) {
                String email = registrationRequest.getEmail();
                DatabaseConnection.getConnection();
                UserDAOImpl userDao = new UserDAOImpl();

                if (!userDao.isEmailUnique(email)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Message\": \"This email already exists.\"}");
                }

                String password = SecurityConfig.passwordEncoder().encode(registrationRequest.getPassword());
                User u = new User(registrationRequest.getUsername(),
                        password,
                        registrationRequest.getFirstname(),
                        registrationRequest.getLastname(),
                        registrationRequest.getEmail(),
                        LocalDate.parse(registrationRequest.getBirthDate()),
                        registrationRequest.getGender(),
                        registrationRequest.getHeight(),
                        registrationRequest.getWeight(),
                        registrationRequest.getDailyCalories());
                try {
                    userDao.add(u);
                    return ResponseEntity.ok().body("{\"Message\": \"Registration successful.\"}");
                } catch (Exception e) {
                    return ResponseEntity.status(401).body("{\"message\": \"Error during registration\"}");
                }
            }
            else {
                System.out.println(status);
                switch (status){
                    case -1:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Missing fields.\"}");
                    case 1:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Error in First name, Last name, Username or Gender.\"}");
                    case 2:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Password is invalid. Must have minimum eight characters, at least one letter, one number and one special character.\"}");
                    case 3:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Email is invalid. Must have a valid email address.\"}");
                    case 4:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Birth date is invalid.\"}");
                    case 5:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Height or Weight is invalid.\"}");
                    case 6:
                        return ResponseEntity.badRequest().body("{\"Message\":\"DailyCalories is invalid.\"}");
                    default:
                        return ResponseEntity.badRequest().body("{\"Message\":\"Something went wrong\"}");
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> login(AuthenticationRequest authenticationRequest) {
        try {
            DatabaseConnection.getConnection();
            User user = UserDAOImpl.getByUsername(authenticationRequest.getUsername());

            if (user == null || user.getEmail() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"User not found.\", \"errorCode\": \"INVALID_CREDENTIALS\"}");
            }
            //System.out.println(user.getUsername());
            if (BCrypt.checkpw(authenticationRequest.getPassword(), user.getPassword())) {
                JWTResponse t = new JWTResponse(jwtService.generateToken(user.getUsername()));
                return ResponseEntity.ok().body(t);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Wrong password.\", \"errorCode\": \"INVALID_CREDENTIALS\"}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            User user = (User) auth.getPrincipal();

            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"Unauthorized\"}");
    }
}
