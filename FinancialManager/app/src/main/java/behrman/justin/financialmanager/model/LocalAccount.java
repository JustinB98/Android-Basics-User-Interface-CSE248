package behrman.justin.financialmanager.model;

import java.util.Set;
import java.util.TreeSet;

public class LocalAccount {

    private String mUsername, mPassword;
    private TreeSet<String> mCreditCardNames;

    public LocalAccount(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public void updateCards() {
        // TODO: 10/6/2018 update cards
    }

    public Set<String> getCardNames() {
        return mCreditCardNames;
    }



}
