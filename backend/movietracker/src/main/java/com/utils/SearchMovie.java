package com.utils;

import java.io.IOException;

import com.movietracker.Movie;



import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import org.hibernate.*;

// Code from https://happycoding.io/tutorials/java-server/post

public class SearchMovie {

  public JSONArray getMovieList(String query) throws IOException {
    System.out.println("Searching for some meaning");

    Session session = HibernateUtil.getSessionFactory().openSession();

    try {

      HttpClient client = HttpClient.newHttpClient();

      String[] apiKeysList = { "k_4vcyw6j4" };
      Random rand = new Random();
      String apiKey = apiKeysList[rand.nextInt(apiKeysList.length)];
      String URL = "https://imdb-api.com/en/API/SearchMovie/" + apiKey + "/" + query;
      HttpRequest httprequest = HttpRequest.newBuilder().uri(URI.create(URL)).build();
     
      HttpResponse<String> response =client.send(httprequest, BodyHandlers.ofString());
      System.out.println(response.statusCode());
      
    
      JSONObject resp = new JSONObject(response.body());

      session.beginTransaction();
      JSONArray movieResultList = resp.getJSONArray("results");
      JSONArray searchResultArray = new JSONArray();
      for (int i = 0; i < movieResultList.length(); i++) {
        JSONObject movieResult = (JSONObject) movieResultList.get(i);
        String movieId = movieResult.getString("id");
        String movieName = movieResult.getString("title");
        String description = movieResult.getString("description");
        String posterImage = movieResult.getString("image");
        System.out.println(movieId + " " + movieName + " " + description + " " + posterImage);
        Movie movie = new Movie(movieId, movieName, description, posterImage);
        session.saveOrUpdate( movie);
        JSONObject movieFound = new JSONObject();
        movieFound.put("title", movieName);
        movieFound.put("description", description);
        movieFound.put("image", posterImage);
        searchResultArray.put(movieFound);
      }
      session.getTransaction().commit();

      return searchResultArray;

    } catch (Exception ex) {
      System.out.println(ex);
      return null;

    } finally {
      session.close();
    }

  }
}