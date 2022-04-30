package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

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
      String mediaId = (String) reqJson.get("mediaid");
      String email = (String) reqJson.get("email");

      publisher.notify("Adding" + mediaId + " to the watchlist of " + email);

      String hql = "select * from WatchList w where w.email = :email and w.mediaId=:mediaId";
      Query q = session.createSQLQuery(hql);
      q.setParameter("email", email);
      q.setParameter("mediaId", mediaId);

      if (q.uniqueResult() != null) {
        WatchList w = (WatchList) q.uniqueResult();
        respJson.put("msg", "Already present in the WatchList");
      } else {
        String hql1 = "select  u from User u where u.email ='" + email + "'";
        Query q1 = session.createQuery(hql1);
        if (q1.list().size() == 0) {
          respJson.put("msg", "user doesn't exist");
          throw new Exception("User not found");
        }

        User user = (User) q1.list().get(0);
        String hql2 = "select m from Media m where m.mediaId='" + mediaId + "'";
        Query q2 = session.createQuery(hql2);

        if (q2.list().size() == 0) {
          respJson.put("msg", "mediaId doesn't exist");
          throw new Exception("ID not found");
        }

        Media media = (Media) q2.list().get(0);
        WatchList w = new WatchList(user, media);
        session.saveOrUpdate(w);
        System.out.println("save");
        respJson.put("successmsg", "Successfully added to the WatchList");

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