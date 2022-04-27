package com.movietracker.Observer;

public interface Subject 
{
    public void attach(Consumer o);
    public void detach(Consumer o);
    public void notify(String m);
}
