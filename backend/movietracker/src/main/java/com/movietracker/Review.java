package com.movietracker;

import java.io.Serializable;


import javax.persistence.*;

@Entity
@Table(name = "Review")
public class Review implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;

    public Review(User user, Media media, String title, String description, double rating) {
        this.user = user;
        this.media = media;
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

  
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediaId")
    private Media media;

    private String title;
    
    @Lob
    private String description;

    private double rating;

	



    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return double return the rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(double rating) {
        this.rating = rating;
    }


    /**
     * @return int return the reviewID
     */
    public int getReviewID() {
        return reviewID;
    }

    /**
     * @param reviewID the reviewID to set
     */
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

}