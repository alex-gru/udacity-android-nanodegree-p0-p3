package nanodegree.p1.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class Movie {

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

}