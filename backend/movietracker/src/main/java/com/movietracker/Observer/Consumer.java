package com.movietracker.Observer;

public abstract class Consumer {
    Subject subject = new Publisher();
    public void update(String m){}

}
