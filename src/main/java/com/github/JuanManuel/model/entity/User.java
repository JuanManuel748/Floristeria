package com.github.JuanManuel.model.entity;

import java.util.Objects;

public class User {
    private String phone;
    private String name;
    private String password;

    public User(String telefono, String name, String password) {
        this.phone = telefono;
        this.name = name;
        this.password = password;
    }

    public User() {}

    public User(String telefono) {
        this.phone = telefono;
    }

    // GETTERS

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return this.phone;
    }

    // SETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String telefono) {
        this.phone = telefono;
    }

    // EQUALS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(phone, user.phone);
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hashCode(phone);
    }


    // ToString
    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
