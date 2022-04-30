package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import com.movietracker.Movie;
import com.movietracker.TVShow;

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
  public void init() throws ServletException {
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

      String hql = "select * from WatchList w where w.email= '" + email + "'";
      Query q = session.createSQLQuery(hql);

      JSONArray searchResultArray = new JSONArray();

      if (q.list().size() == 0) {
        respJson.put("msg", "Empty WatchList. Start Adding stuff !");
      } else {

        for (Object w : q.list()) {
          Object[] data = (Object[]) w;
          String mediaId = (String) data[1];
          String hql1 = "select m from Movie m where m.movieID =:mediaid";
          Query q1 = session.createQuery(hql1);
          q1.setParameter("mediaid", mediaId);
          if (q1.uniqueResult() == null) {
            String hql2 = "select t from TVShow t where t.seriesID ='" + mediaId + "'";
            Query q2 = session.createQuery(hql2);
            TVShow t = (TVShow) q2.uniqueResult();
            JSONObject mediaFound = new JSONObject();
            mediaFound.put("title", t.getSeriesName());
            mediaFound.put("description", t.getDescription());
            mediaFound.put("image", t.getPosterImage());
            mediaFound.put("id", mediaId);
            searchResultArray.put(mediaFound);
          } else {
            Movie m = (Movie) q1.uniqueResult();
            JSONObject mediaFound = new JSONObject();
            mediaFound.put("title", m.getMovieName());
            mediaFound.put("description", m.getDescription());
            mediaFound.put("image", m.getPosterImage());
            mediaFound.put("id", mediaId);
            searchResultArray.put(mediaFound);
          }

        }
        respJson.put("watchlistresults", searchResultArray);
      }

    } catch (Exception ex) {
      System.out.println(ex);
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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