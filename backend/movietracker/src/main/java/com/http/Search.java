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

// Code from https://happycoding.io/tutorials/java-server/post

@WebServlet("/search")
public class Search extends HttpServlet {
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
            String query = (String) reqJson.get("query");
            String type = (String) reqJson.get("type");
            if(type.equals("Movie")) {
              SearchMovie s = new SearchMovie();          
           
              respJson.put("searchresults",s.getMovieList(query));
            }
            else {
              SearchTVShow s = new SearchTVShow();          
           
              respJson.put("searchresults",s.getTVShowList(query));
            }
        
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
