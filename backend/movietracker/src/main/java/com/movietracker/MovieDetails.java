package com.movietracker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDetails {
	@JsonProperty("imdbRating")
	private String imdbRating;
	@JsonProperty("Runtime")
	private String runtime;
	@JsonProperty("Language")
	private String language;
	@JsonProperty("Rated")
	private String rated;
	@JsonProperty("Released")
	private String released;
	@JsonProperty("Plot")
	private String plot;
	@JsonProperty("Director")
	private String director;
	@JsonProperty("Actors")
	private String actors;
	@JsonProperty("Awards")
	private String awards;
	@JsonProperty("Year")
	private String year;

	@JsonProperty("Genre")
	private String genre;

	public String getImdbRating() {
		return imdbRating;
	}

	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRated() {
		return rated;
	}

	public void setRated(String rated) {
		this.rated = rated;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

	public String getPlot() {
		return plot;
	}

	public void setPlot(String plot) {
		this.plot = plot;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActors() {
		return actors;
	}

	public void setActors(String actors) {
		this.actors = actors;
	}

	public String getAwards() {
		return awards;
	}

	public void setAwards(String awards) {
		this.awards = awards;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}