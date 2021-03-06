package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import com.movietracker.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.hibernate.*;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import com.utils.*;
import com.movietracker.Observer.Publisher;

import java.util.logging.Level;

// Code modified from https://happycoding.io/tutorials/java-server/post

@WebServlet("/signup")
public class Signup extends HttpServlet {
  private Publisher publisher;

  @Override
  public void init() throws ServletException {
    this.publisher = (Publisher) getServletContext().getAttribute("publisher");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    publisher.notify("Signup post request");

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
      userDetails.setPassword(Encrypt.hashPassword(userDetails.getPassword()));
      // INSERT USER INTO DATABASE:
      session.beginTransaction();
      session.save(userDetails);
      session.getTransaction().commit();

      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("what you need");
      PrintWriter writer = response.getWriter();
      writer.println("success");

      publisher.notify("User " + userDetails.getEmail() + " signed up");

    } catch (Exception ex) {
      System.out.println(ex);
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
