package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietracker.User;
import com.utils.Encrypt;
import com.utils.HibernateUtil;

import org.hibernate.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;
import com.movietracker.Observer.Publisher;

import java.util.logging.Level;

// Code modified from https://happycoding.io/tutorials/java-server/post

@WebServlet("/signin")
public class Signin extends HttpServlet {
  private Publisher publisher;

  @Override
  public void init() throws ServletException
  {
      this.publisher = (Publisher) getServletContext().getAttribute("publisher");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
      
    JSONObject reqJson;
    JSONObject respJson = new JSONObject();
    
    java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
    Session session = HibernateUtil.getSessionFactory().openSession();

    try {
      // get Reader from request
      Reader reqReader = request.getReader();
      JSONParser parser = new JSONParser();
      // parse our request to json
      reqJson = (JSONObject) parser.parse(reqReader);
      ObjectMapper objectMapper = new ObjectMapper();
      User userDetails = objectMapper.readValue(reqJson.toJSONString(), User.class);

      session.beginTransaction();
      User userExisting = (User) session.get(User.class, userDetails.getEmail());

      if (Encrypt.checkPass(userDetails.getPassword(), userExisting.getPassword())) {
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.println("success");
        
        // log 
        publisher.notify("User " + userDetails.getEmail() + " signed in");

      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try (PrintWriter out = response.getWriter()) {
          out.println(respJson.toString());
          out.flush();
        }
      }

    } catch (Exception ex) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      // respJson.put("answer", "Something bad happened");
      try (PrintWriter out = response.getWriter()) {
        out.println(respJson.toString());
        out.flush();
      }
    } finally {
      session.close();
    }

  }
}
