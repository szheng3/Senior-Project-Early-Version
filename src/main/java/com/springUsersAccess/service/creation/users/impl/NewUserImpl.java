package com.springUsersAccess.service.creation.users.impl;

import com.springUsersAccess.dao.UserDao;
import com.springUsersAccess.service.creation.users.NewUserService;

import java.sql.SQLException;

/**
 * Created by Alex Almanza on 2/4/17.
 */
public class NewUserImpl implements NewUserService {
    private UserDao userDao;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(String username, String password) throws SQLException {
        if (userDao.isUsernameTaken(username)) {
            throw new IllegalArgumentException("Cant add a username that is already taken: " + username);
        }

        userDao.addUser(username, password);
    }
}