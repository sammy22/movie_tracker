package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import com.utils.HibernateUtil;
import com.utils.SearchMovie;
import com.utils.SearchTVShow;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

// Code from https://happycoding.io/tutorials/java-server/post

@WebServlet("/mediadetails")
public class MediaDetails extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
        System.out.println("Sign up post works!");
        JSONObject reqJson;
        JSONObject respJson = new JSONObject();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            //get Reader from request
            Reader reqReader = request.getReader();
            JSONParser parser = new JSONParser();
            //parse our request to json
            reqJson = (JSONObject) parser.parse(reqReader);
            String mediaID = (String) reqJson.get("mediaid");
            HttpClient client = HttpClient.newHttpClient();

            String[] apiKeysList = { "567760ea" };
            Random rand = new Random();
            String apiKey = apiKeysList[rand.nextInt(apiKeysList.length)];
            String URL = "https://www.omdbapi.com/?i="+mediaID+"&apikey="+apiKey;
            HttpRequest httprequest = HttpRequest.newBuilder().uri(URI.create(URL)).build();
     
            HttpResponse<String> httpresponse =client.send(httprequest, BodyHandlers.ofString());
            System.out.println(httpresponse.statusCode());
      
    
            respJson = (JSONObject) parser.parse(httpresponse.body());

           
        
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("what you need");
            PrintWriter writer=response.getWriter();
            writer.println(respJson.toString());
            
        } catch ( Exception ex) {
            System.out.println(ex);
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
