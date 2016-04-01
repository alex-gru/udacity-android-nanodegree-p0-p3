package nanodegree.p1p2.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by alexgru on 01-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class Trailer {

    @JsonIgnore
    String YOUTUBE_BASE = "http://www.youtube.com/watch?v=";
    @JsonProperty("id")
    String id;
    @JsonProperty("iso_639_1")
    String iso_639_1;
    @JsonProperty("iso_3166_1")
    String iso_3166_1;
    @JsonProperty("key")
    String key;
    @JsonProperty("name")
    String name;
    @JsonProperty("site")
    String site;
    @JsonProperty("size")
    String size;
    @JsonProperty("type")
    String type;

    public String getId() {
        return id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getYoutubeURL() {
        return YOUTUBE_BASE += getKey();
    }
}
