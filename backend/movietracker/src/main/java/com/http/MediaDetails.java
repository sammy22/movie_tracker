package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietracker.MovieDetails;
import com.utils.HibernateUtil;

import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import com.movietracker.Observer.*;

import java.util.logging.Level;

// Code modified from https://happycoding.io/tutorials/java-server/post

@WebServlet("/mediadetails")
public class MediaDetails extends HttpServlet {
  private Publisher publisher;

  @Override
  public void init() throws ServletException {
    this.publisher = (Publisher) getServletContext().getAttribute("publisher");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    publisher.notify("Media details post request");

    JSONObject reqJson;

    java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
      // get Reader from request
      Reader reqReader = request.getReader();
      JSONParser parser = new JSONParser();
      // parse our request to json
      reqJson = (JSONObject) parser.parse(reqReader);
      String mediaID = (String) reqJson.get("mediaid");
      HttpClient client = HttpClient.newHttpClient();

      String[] apiKeysList = { "567760ea" };
      Random rand = new Random();
      String apiKey = apiKeysList[rand.nextInt(apiKeysList.length)];
      String URL = "https://www.omdbapi.com/?i=" + mediaID + "&apikey=" + apiKey;
      HttpRequest httprequest = HttpRequest.newBuilder().uri(URI.create(URL)).build();

      HttpResponse<String> httpresponse = client.send(httprequest, BodyHandlers.ofString());

      ObjectMapper objectMapper = new ObjectMapper();
      MovieDetails details = objectMapper.readValue(httpresponse.body().toString(), MovieDetails.class);

      publisher.notify("Viewed media details for media ID: " + mediaID);

      String respJson = objectMapper.writeValueAsString(details);
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("what you need");
      PrintWriter writer = response.getWriter();
      writer.println(respJson);

    } catch (Exception ex) {
      System.out.println(ex);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      try (PrintWriter out = response.getWriter()) {
        out.println("Something went wrong");
        out.flush();
      }
    } finally {
      session.close();
    }

  }
}
