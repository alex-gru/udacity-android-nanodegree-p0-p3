package nanodegree.p1p2.data;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import nanodegree.p1p2.MainActivity;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */

public class Movie {

    public final static String POSTER_WIDTH = "342";
    public final static String POSTER_HEIGHT = "513";
    public final static String POSTER_WIDTH_HIGH_RES = "780";
    private final static String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w";

    @JsonProperty("poster_path")
    String poster_path;

    @JsonProperty("adult")
    String adult;

    @JsonProperty("overview")
    String overview;

    @JsonProperty("release_date")
    String release_date;

    @JsonProperty("genre_ids")
    List genre_ids;

    @JsonProperty("id")
    long id;

    @JsonProperty("original_title")
    String original_title;

    @JsonProperty("original_language")
    String original_language;

    @JsonProperty("title")
    String title;

    @JsonProperty("backdrop_path")
    String backdrop_path;

    @JsonProperty("popularity")
    String popularity;

    @JsonProperty("vote_count")
    String vote_count;

    @JsonProperty("video")
    String video;

    @JsonProperty("vote_average")
    String vote_average;

    @JsonIgnore
    List<Trailer> trailers;

    @JsonIgnore
    List<Review> reviews;

    @JsonIgnore
    byte[] moviePosterByteArray;

    public String getFullPosterPath() {
        return BASE_URL_POSTER + POSTER_WIDTH + poster_path;
    }

    public String getFullPosterPathHighRes() {
        return BASE_URL_POSTER + POSTER_WIDTH_HIGH_RES + poster_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getYear () {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(release_date));
            return cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            Log.e(MainActivity.TAG, "Could not parse year from date string.", e);
        }
        return -1;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public byte[] getMoviePosterByteArray() {
        return moviePosterByteArray;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setMoviePosterByteArray(byte[] moviePosterByteArray) {
        this.moviePosterByteArray = moviePosterByteArray;
    }
}