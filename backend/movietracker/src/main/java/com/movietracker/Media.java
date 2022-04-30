package com.movietracker;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Media {

    @Id
    private String mediaId;

    private String mediaName;

    public Media() {
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public Media(String mediaId, String mediaName) {
        this.mediaId = mediaId;
        this.mediaName = mediaName;

    }

    public String getMediaName() {
        return this.mediaName;
    }

}