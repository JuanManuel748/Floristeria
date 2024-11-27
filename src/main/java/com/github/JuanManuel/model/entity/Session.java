package com.github.JuanManuel.model.entity;

public class Session {
    private static Session _instance;
    private static User currentUser;

    private Session() {}

    public static Session getInstance() {
        if(_instance == null) {
            _instance = new Session();
            _instance.logIn(currentUser);
        }
        return _instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logIn(User usr) {
        currentUser = usr;
    }

    public void logOut(User usr) {
        currentUser = null;
    }
}
