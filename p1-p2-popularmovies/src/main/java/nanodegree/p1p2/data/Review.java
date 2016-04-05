package nanodegree.p1p2.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by alexgru on 02-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class Review {

    @JsonProperty("id")
    String id;
    @JsonProperty("author")
    String author;
    @JsonProperty("content")
    String content;
    @JsonProperty("url")
    String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
