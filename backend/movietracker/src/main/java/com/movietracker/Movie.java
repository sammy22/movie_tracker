package com.movietracker;

import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Movie {

    @Id
    private String movieID;

    private String movieName;

    String description;

    String posterImage;

    public Movie(String movieID, String movieName, String description, String posterImage) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.description = description;
        this.posterImage = posterImage;
    }

    public String getMovieName() {
        return this.movieName;
    }

    public String getMovieId() {
        return this.movieID;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPosterImage() {
        return this.posterImage;
    }

    public void getmovieInfo() {
        // to be implemented
    }

    public void setMovieInfo(HashMap info) {
        // to be implemented
    }

}
