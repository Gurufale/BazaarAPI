package com.user.builder;

import com.user.entities.User;
import com.user.entities.Wallet;

public class TestUserBuilder {
    private Long id;
    private String email;
    private String phoneNumber;
    private String preference;
    private String password;
    private String role;
    private Wallet wallet;

    public TestUserBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public TestUserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public TestUserBuilder withNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public TestUserBuilder withPreference(String preference) {
        this.preference = preference;
        return this;
    }

    public TestUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public TestUserBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public TestUserBuilder withWallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public User build() {
        return User.builder().email(email).phoneNumber(phoneNumber).preference(preference).password(password).role(role).wallet(wallet).build();
        //return new User(email, number, preference, password, role, wallet);
    }
}

