package hu.unideb.inf.controller;

import hu.unideb.inf.model.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SessionManager {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        if (user != null && user.getId() != null) {
            currentUser = user;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}