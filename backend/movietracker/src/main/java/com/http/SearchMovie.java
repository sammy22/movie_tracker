package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import com.movietracker.User;
import com.movietracker.Movie;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import org.hibernate.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.utils.*;

// Code from https://happycoding.io/tutorials/java-server/post

@WebServlet("/searchMovie")
public class SearchMovie extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
      System.out.println("Searching for some meaning");
      JSONObject reqJson;
      JSONObject respJson = new JSONObject();
      Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            //get Reader from request
            System.out.println("Are you here?");
            Reader reqReader = request.getReader();
            JSONParser parser = new JSONParser();
            //parse our request to json
            reqJson = (JSONObject) parser.parse(reqReader);
            System.out.println("vrefe"+reqJson.toString());
            /*
            For storing the searches in the future?
              ObjectMapper objectMapper = new ObjectMapper();
            User userDetails = objectMapper.readValue(reqJson.toJSONString(),UserSearch.class);
            */
            HttpClient client = HttpClient.newHttpClient();
            String term = reqJson.getString("query");
            String[] apiKeysList={"k_4vcyw6j4"};
      	    Random rand =new Random();        
      	    String apiKey =apiKeysList[rand.nextInt(apiKeysList.length)];
            String URL="https://imdb-api.com/en/API/SearchMovie/"+apiKey+"/"+term;
            HttpRequest httprequest = HttpRequest.newBuilder().uri(URI.create(URL)).build();
            System.out.println(client.sendAsync(httprequest,BodyHandlers.ofString())
                .thenApply(HttpResponse::body));
                //.thenApply(EndUser::parseJSON)
                //.join());
            String responseBody=client.sendAsync(httprequest,BodyHandlers.ofString())
                .thenApply(HttpResponse::body).toString();
            JSONObject resp = new JSONObject(responseBody);
            if (!resp.getString("errorMessage").equals("")) {
                throw new Exception(resp.getString("errorMessage"));
            }  
            //start a transaction
            session.beginTransaction();
            JSONArray movieResultList = resp.getJSONArray("results");	
            JSONArray searchResultArray = new JSONArray();
	          for (int i = 0 ; i < movieResultList.length(); i++) {
		            JSONObject movieResult =(JSONObject) movieResultList.get(i);			
		            String movieId = movieResult.getString("id");
		            String movieName = movieResult.getString("title");
                String description= movieResult.getString("description");
                String posterImage = movieResult.getString("image");
		            System.out.println(movieId+" "+movieName+" "+description+" "+posterImage);
                Movie movie = new Movie(movieId, movieName, description, posterImage);
                session.save(movie);
                JSONObject movieFound = new JSONObject();
                movieFound.put("title",movieName);
                movieFound.put("description", description);
                movieFound.put("image", posterImage);
                searchResultArray.put(movieFound);
            }
            session.getTransaction().commit();
            
            respJson.put("searchresults",searchResultArray);

            
            response.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out=response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respJson.toString());
            out.flush();  
            
        } catch ( Exception ex) {
          // System.out.println(ex);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            // respJson.put("answer", "Something bad happened");
            try (PrintWriter out = response.getWriter()) {
                out.println(respJson.toString());
                out.flush();
            }
  
        }
        finally{
          session.close();
        }
        
        
  }
}