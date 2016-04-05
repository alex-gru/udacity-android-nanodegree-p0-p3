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
    private final static String BASE_URL_POSTER = "http://image.tmdb.org/t/p/w" + POSTER_WIDTH;

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


    public String getPoster_path() {
        return poster_path;
    }

    public String getFullPosterPath() {
        return BASE_URL_POSTER + poster_path;
    }

    public int getYear () {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(getRelease_date()));
            return cal.get(Calendar.YEAR);
        } catch (ParseException e) {
            Log.e(MainActivity.TAG, "Could not parse year from date string.", e);
        }
        return -1;
    }
    public String getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public List getGenre_ids() {
        return genre_ids;
    }

    public long getId() {
        return id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public String getVideo() {
        return video;
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
}