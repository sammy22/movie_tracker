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
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietracker.Media;
import com.movietracker.Movie;
import com.movietracker.TVShow;
import com.movietracker.User;
import com.movietracker.WatchList;
import com.utils.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
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

@WebServlet("/getwatchlist")
public class GetWatchList extends HttpServlet {
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
    session.beginTransaction();

    try {
      // get Reader from request
      Reader reqReader = request.getReader();
      JSONParser parser = new JSONParser();
      // parse our request to json
      reqJson = (JSONObject) parser.parse(reqReader);
      String email = (String) reqJson.get("email");


      publisher.notify("Retrieving the watchlist of " + email);
      
      String hql = "select w from WatchList w where w.email = :email";
      Query q = session.createSQLQuery(hql);
      q.setParameter("email", email);


      JSONArray searchResultArray = new JSONArray();  
      if (q.uniqueResult() == null) {
        respJson.put("msg", "Empty WatchList. Start Adding stuff !");
      }
      
      else {
        List<WatchList> mediaList=(List<WatchList>) q.list();
        for ( WatchList w : mediaList){

            String mediaId = w.getMedia().getMediaId();
            String hql1 = "select m from Movie m where m.movieID ='"+mediaId+"'";
            Query q1 = session.createQuery(hql1);
            if (q1.uniqueResult() == null) {
                String hql2 = "select t from TVShow t where t.seriesID ='"+mediaId+"'";
                Query q2 = session.createQuery(hql2);
                TVShow t = (TVShow) q2.uniqueResult();
                JSONObject mediaFound = new JSONObject();
                mediaFound.put("title", t.getSeriesName());
                mediaFound.put("description", t.getDescription());
                mediaFound.put("image", t.getPosterImage());
                mediaFound.put("id", mediaId);
                searchResultArray.put(mediaFound);
            }
            else {
                Movie m = (Movie) q1.uniqueResult();
                JSONObject mediaFound = new JSONObject();
                mediaFound.put("title", m.getMovieName());
                mediaFound.put("description", m.getDescription());
                mediaFound.put("image", m.getPosterImage());
                mediaFound.put("id", mediaId);
                searchResultArray.put(mediaFound);
                System.out.println(q1.uniqueResult().toString());
            }
            
        }
        respJson.put("watchlistresults", searchResultArray);
      }
      
      

    } catch (Exception ex) {
      System.out.println(ex);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      // respJson.put("answer", "Something bad happened");
      try (PrintWriter out = response.getWriter()) {
        out.println("Something went wrong");
        out.flush();
      }
    } finally {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("what you need");
      PrintWriter writer = response.getWriter();
      writer.println(respJson);
      session.getTransaction().commit();
      session.close();
    }

  }
}