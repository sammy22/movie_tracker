package com.movietracker;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "WatchList")
public class WatchList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediaId")
    private Media media;

    public WatchList(User user, Media media) {
        this.user = user;
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

}