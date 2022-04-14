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
public class TVShow {

    @Id
    private String seriesID;

    private String seriesName;

    String description;

    String posterImage;

    public TVShow(String seriesID, String seriesName, String description, String posterImage) {
        this.seriesID = seriesID;
        this.seriesName = seriesName;
        this.description = description;
        this.posterImage = posterImage;
    }

    public String getSeriesName() {
        return this.seriesName;
    }

    public String getSeriesId() {
        return this.seriesID;
    }

    public String getDescription() {
        return this.description;
    }

    public String getPosterImage() {
        return this.posterImage;
    }

    public void getSeriesInfo() {
        // to be implemented
    }

    public void setSeriesInfo(HashMap info) {
        // to be implemented
    }

}