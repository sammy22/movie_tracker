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

// Code from https://happycoding.io/tutorials/java-server/post

@WebServlet("/signin")
public class Signin extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    JSONObject reqJson;
    JSONObject respJson = new JSONObject();
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
