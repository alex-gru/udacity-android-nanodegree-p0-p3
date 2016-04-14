package nanodegree.p1p2.data;

import android.provider.BaseColumns;

/**
 * Created by alexgru on 13-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public final class LocalMovieContract {
    public LocalMovieContract() {}

    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_POSTER_BYTES = "poster_bytes";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
    }
}
