package com.utils;

import java.io.IOException;

import com.movietracker.TVShow;
import com.movietracker.Media;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Random;
import org.hibernate.query.Query;

import org.hibernate.*;

// Code from https://happycoding.io/tutorials/java-server/post

public class SearchTVShow {

  public JSONArray getTVShowList(String query) throws IOException {

    Session session = HibernateUtil.getSessionFactory().openSession();

    try {

      HttpClient client = HttpClient.newHttpClient();

      String[] apiKeysList = { "k_4vcyw6j4" };
      Random rand = new Random();
      String apiKey = apiKeysList[rand.nextInt(apiKeysList.length)];
      String URL = "https://imdb-api.com/en/API/SearchSeries/" + apiKey + "/" + query;
      HttpRequest httprequest = HttpRequest.newBuilder().uri(URI.create(URL)).build();

      HttpResponse<String> response = client.send(httprequest, BodyHandlers.ofString());

      JSONObject resp = new JSONObject(response.body());

      session.beginTransaction();
      JSONArray seriesResultList = resp.getJSONArray("results");
      JSONArray searchResultArray = new JSONArray();
      for (int i = 0; i < seriesResultList.length(); i++) {
        JSONObject seriesResult = (JSONObject) seriesResultList.get(i);
        String seriesId = seriesResult.getString("id");
        String seriesName = seriesResult.getString("title");
        String description = seriesResult.getString("description");
        String posterImage = seriesResult.getString("image");
        String hql = "select m from Media m where m.mediaId='" + seriesId + "'";
        Query q = session.createQuery(hql);
        if (q.uniqueResult() == null) {

          Media media = new Media(seriesId, seriesName);
          TVShow tvshow = new TVShow(seriesId, seriesName, description, posterImage);
          session.saveOrUpdate(media);
          session.saveOrUpdate(tvshow);
          JSONObject seriesFound = new JSONObject();
          seriesFound.put("title", seriesName);
          seriesFound.put("description", description);
          seriesFound.put("image", posterImage);
          seriesFound.put("id", seriesId);
          searchResultArray.put(seriesFound);
        } else {
          String mvl = "select m from TVShow m where m.seriesID='" + seriesId + "'";
          Query qvl = session.createQuery(mvl);
          TVShow t = (TVShow) qvl.list().get(0);
          JSONObject seriesFound = new JSONObject();
          seriesFound.put("title", t.getSeriesName());
          seriesFound.put("description", t.getDescription());
          seriesFound.put("image", t.getPosterImage());
          seriesFound.put("id", seriesId);
          searchResultArray.put(seriesFound);
        }
      }
      session.getTransaction().commit();

      return searchResultArray;

    } catch (Exception ex) {
      System.out.println(ex);
      return null;

    } finally {
      session.close();
    }

  }
}
