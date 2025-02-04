package it.unical.web.backend.persistence.dao.DAOInterface;

import it.unical.web.backend.persistence.model.UserInfo;

public interface UserInfoDAO {
    UserInfo getUserInfoByUserId(int userId);
    void createUserInfo(UserInfo userInfo);
    void updateUserInfo(UserInfo userInfo);
}
