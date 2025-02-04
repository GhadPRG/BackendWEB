package it.unical.web.backend.service;

import it.unical.web.backend.config.security.SecurityConfig;
import it.unical.web.backend.controller.DatabaseConnection;
import it.unical.web.backend.persistence.RegexHandler;
import it.unical.web.backend.persistence.dao.DAOInterface.UserDAO;
import it.unical.web.backend.persistence.dao.UserDAOImpl;
import it.unical.web.backend.persistence.dao.UserInfoDAOImpl;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;
import it.unical.web.backend.service.Request.AuthenticationRequest;
import it.unical.web.backend.service.Request.RegistrationRequest;
import it.unical.web.backend.service.Response.JWTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AuthService {
    @Autowired
    private JWTService jwtService;

    /**
     * Valida i dati contenuti in un oggetto RegistrationRequest.
     *
     * Questo metodo controlla se i campi obbligatori della richiesta di registrazione sono presenti e validi.
     * Restituisce un codice numerico che indica il tipo di errore riscontrato:
     *
     * - -1 se uno o più campi obbligatori sono null.
     * - 1 se il nome utente, il nome, il cognome o il genere contengono caratteri non validi.
     * - 2 se la password non rispetta i criteri di validità.
     * - 3 se l'email non ha un formato valido.
     * - 4 se la data di nascita non è valida.
     * - 5 se altezza o peso sono valori non positivi.
     * - 6 se il valore delle calorie giornaliere è non positivo.
     * - 0 se tutti i dati sono validi.
     *
     * @param registrationRequest l'oggetto contenente i dati di registrazione dell'utente
     * @return un codice di errore specifico o `0` se i dati sono validi
     */
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
                String username = registrationRequest.getUsername();
                String email = registrationRequest.getEmail();
                DatabaseConnection.getConnection();
                UserDAOImpl userDao = new UserDAOImpl();
                UserInfoDAOImpl userInfoDAO = new UserInfoDAOImpl();


                if (!userDao.isUsernameUnique(username)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Message\": \"This username already exists.\"}");
                }
                else if (!userInfoDAO.isEmailUnique(email)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"Message\": \"This email already exists.\"}");
                }

                try {
                    String password = SecurityConfig.passwordEncoder().encode(registrationRequest.getPassword());
                    User u = new User(username, password, null);
                    userDao.createUser(u);
                    UserInfo userInfo = new UserInfo(u,
                            registrationRequest.getFirstname(),
                            registrationRequest.getLastname(),
                            email,
                            LocalDate.parse(registrationRequest.getBirthDate()),
                            registrationRequest.getGender(),
                            registrationRequest.getHeight(),
                            registrationRequest.getWeight(),
                            registrationRequest.getDailyCalories());
                    userInfoDAO.createUserInfo(userInfo);

                    return ResponseEntity.ok().body("{\"Message\": \"Registration successful.\"}");
                } catch (Exception e) {
                    return ResponseEntity.status(401).body("{\"message\": \"Error during registration\"}");
                }
            }
            else {
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
            UserDAOImpl userDao = new UserDAOImpl();
            User user = userDao.getUserByUsername(authenticationRequest.getUsername());

            if (user == null || user.getUserInfo().getEmail() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"Message\": \"User not found.\", \"errorCode\": \"INVALID_CREDENTIALS\"}");
            }
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
}
