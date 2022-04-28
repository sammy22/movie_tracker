package com.movietracker;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table
public class WatchList {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user;

  
  
    @Id
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "mediaId")
    private Set<Media> watchList;

    public WatchList( User user, Set<Media> watchList){
        this.user=user;
        this.watchList= watchList;
    }

	public Set<Media> getWatchList() {
		return watchList;
	}

	public boolean addToWatchList(Media media) {
		
        if (watchList.contains(media)) {
            // What do you want me to do?
            return false;
        }
        else {
            watchList.add(media);
            return true;
        }
        
	}






   

}