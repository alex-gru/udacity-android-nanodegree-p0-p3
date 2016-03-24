package nanodegree.p1.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */

public class Movie {

    final static String POSTER_WIDTH = "w342";
    final static String BASE_URL_POSTER = "http://image.tmdb.org/t/p/" + POSTER_WIDTH;

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
    int id;

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

    public String getPoster_path() {
        return poster_path;
    }

    public String getFullPosterPath() {
        return BASE_URL_POSTER + poster_path;
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

    public int getId() {
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
}