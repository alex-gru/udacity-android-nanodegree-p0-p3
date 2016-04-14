package nanodegree.p1p2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alexgru on 13-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class LocalMovieHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Movie.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LocalMovieContract.MovieEntry.TABLE_NAME + " (" +
                    LocalMovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_BYTES + BLOB_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_ADULT + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_GENRE_IDS + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_BACKDROP_PATH + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_POPULARITY + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_VOTE_COUNT + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_VIDEO + TEXT_TYPE + COMMA_SEP +
                    LocalMovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocalMovieContract.MovieEntry.TABLE_NAME;

    public LocalMovieHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
