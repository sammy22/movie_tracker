package com.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import java.util.List;



import com.movietracker.Review;

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

@WebServlet("/getreview")
public class GetReview extends HttpServlet {
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
      String mediaId = (String) reqJson.get("mediaid");
      String email = (String) reqJson.get("email");



      publisher.notify("Checking if " + email + "  has reviewed  " + mediaId);
      
      String hql = "select r from Review r where r.email = :email and r.mediaId=:mediaId";
      Query q = session.createSQLQuery(hql);
      q.setParameter("email", email);
      q.setParameter("mediaId", mediaId);

      if (q.uniqueResult() != null) {
        Review r = (Review) q.uniqueResult();
        JSONObject userReview = new JSONObject();
        userReview.put("isreviewed", true);
        userReview.put("title", r.getTitle());
        userReview.put("description", r.getDescription());
        userReview.put("rating", r.getRating());
        respJson.put("userreview", userReview);
        
      }
      publisher.notify("Retrieving random reviews for " + mediaId);
      String hql1 = "select  r from Review r where r.mediaId=:mediaId order by rand() limit 3";
      Query q1 = session.createSQLQuery(hql1);
      q1.setParameter("mediaId", mediaId);

      List<Review>  reviewList = (List<Review>) q1.list();
      JSONArray reviewResultsArray = new JSONArray();
      for ( Review r : reviewList) {
        JSONObject review = new JSONObject();
        review.put("title", r.getTitle());
        review.put("description", r.getDescription());
        review.put("rating", r.getRating());
        reviewResultsArray.put(review);
      }
      respJson.put("reviewlist", reviewResultsArray);
      
      
      

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