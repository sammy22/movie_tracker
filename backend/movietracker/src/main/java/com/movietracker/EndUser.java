package com.movietracker;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EndUser {
    private int userID;
    private String userName;
    //passwordHash
    private String email;
    protected int userType; // Professional Critic or Normal user
    protected List<Movie> toWatchList;
    protected List<Movie> watchedList;


    EndUser(int userID, String userName, String email, int userType) {
        userID=this.userID;
        userName=this.userName;
        email=this.email;
        userType=this.userType;
    }

    public void setUserName(String userName) {
        this.userName=userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean setEmail(String email) {
        if (emailAuthenticator(email)) {
            this.email=email;
            return true;
        }

        else return false;

    }

    public String getEmail(){
        return this.email;
    }

    private boolean emailAuthenticator(String email){
        
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // RFC-5322
 
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
