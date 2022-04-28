package com.movietracker;


import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Media {

    @Id
    private String mediaID;

    private String mediaName;



    public Media(String mediaID, String mediaName) {
        this.mediaID = mediaID;
        this.mediaName = mediaName;
        
    }

    public String getMediaName() {
        return this.mediaName;
    }



}