package it.unical.web.backend.persistence.proxy;


import it.unical.web.backend.persistence.dao.DAOInterface.UserInfoDAO;
import it.unical.web.backend.persistence.dao.UserInfoDAOImpl;
import it.unical.web.backend.persistence.model.User;
import it.unical.web.backend.persistence.model.UserInfo;

public class UserProxy extends User {
    private boolean userInfoLoaded = false;

    @Override
    public UserInfo getUserInfo() {
        if (!userInfoLoaded) {
            UserInfoDAO userInfoDAO = new UserInfoDAOImpl();
            UserInfo userInfo = userInfoDAO.getUserInfoByUserId(this.getId());
            super.setUserInfo(userInfo);
            userInfoLoaded = true;
        }
        return super.getUserInfo();
    }
}
