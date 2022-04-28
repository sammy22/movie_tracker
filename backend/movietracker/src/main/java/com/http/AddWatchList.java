package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietracker.Media;
import com.movietracker.User;
import com.movietracker.WatchList;
import com.utils.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import com.movietracker.Observer.*;

import java.util.logging.Level;



// Code from https://happycoding.io/tutorials/java-server/post

@WebServlet("/addtowatchlist")
public class AddWatchList extends HttpServlet {
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
      String mediaId = (String) reqJson.get("mediaid");
      String email = (String) reqJson.get("email");


      publisher.notify("Adding" + mediaId +" to the watchlist of " + email);
      
      String hql = "select WatchList w where c.email = :email";
      Query q = session.createQuery(hql);
      q.setParameter("email", email);
      WatchList w = (WatchList) q.uniqueResult();
      if (w != null) {
        String hql2 = "select Media m where m.mediaId = :mediaId";
        Query q2 = session.createQuery(hql2);
        q2.setParameter("mediaId", mediaId);
        Media media= (Media) q2.uniqueResult();
        boolean isSuccess = w.addToWatchList(media);
        if (isSuccess) {
            session.saveOrUpdate(w);
            respJson.put("successmsg", "Successfully added to the WatchList");
        }
        else {
            //What are we doing?
            respJson.put("successmsg", "Already present in the WatchList");
        }
      }
      else {
        String hql1 = "select User u where u.email = :email";
        Query q1 = session.createQuery(hql1);
        q1.setParameter("email", email);
        User user = (User) q1.uniqueResult();  
        String hql2 = "select Media m where m.mediaId = :mediaId";
        Query q2 = session.createQuery(hql2);
        q2.setParameter("mediaId", mediaId);
        Media media= (Media) q2.uniqueResult();
        Set<Media> watchList = new HashSet<Media>();
        watchList.add(media);
        w = new WatchList(user,watchList);
        session.saveOrUpdate(w);
        respJson.put("successmsg", "Successfully added to the WatchList");

      }
      
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("what you need");
      PrintWriter writer = response.getWriter();
      writer.println(respJson);

    } catch (Exception ex) {
      System.out.println(ex);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      // respJson.put("answer", "Something bad happened");
      try (PrintWriter out = response.getWriter()) {
        out.println("Something went wrong");
        out.flush();
      }
    } finally {
      session.close();
    }

  }
}