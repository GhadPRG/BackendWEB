package it.unical.web.backend.persistence;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHandler {

    public static RegexHandler getInstance() {
        return new RegexHandler();
    }

    RegexHandler instance = null;

    public RegexHandler() {}

    private boolean match(String Regex, String x) {
        Pattern pattern = Pattern.compile(Regex);
        Matcher matcher = pattern.matcher(x);
        return matcher.matches();
    }

    public boolean onlyCharacters(String x) {
        String Regex = "^[a-zA-Z]+$";
        return match(Regex, x);
    }


    public boolean isAValidEmail(String email) {
        String Regex = "^[a-zA-Z0-9_.±]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$";
        return match(Regex, email);
    }

    public boolean isAValidPassword(String password) {
        String Regex = "\"^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$\"";
        String regex_debug = "^.*$"; // per evitare di scrivere password buone nelle fasi di testing
        return match(regex_debug, password);
    }

    public boolean isAValidDate(String date) {
        String Regex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        return match(Regex, date);
    }

    public boolean isAValidUsername(String username) {
        String Regex = "^[A-Za-z][A-Za-z0-9_]{3,29}+$";
        return match(Regex, username);
    }


}
