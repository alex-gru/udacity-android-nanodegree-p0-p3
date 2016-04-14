package nanodegree.p1p2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nanodegree.p1p2.MainActivity;
import nanodegree.p1p2.MovieDetailFragment;
import nanodegree.p1p2.MovieGridFragment;
import nanodegree.p1p2.MoviePosterAdapter;
import nanodegree.p1p2.R;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class LocalMovieLoaderAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private final AppCompatActivity activity;

    public LocalMovieLoaderAsyncTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        List<Movie> movies_favorites;

        SQLiteDatabase database = MainActivity.localMovieHelper.getReadableDatabase();

        String[] projection = {
                LocalMovieContract.MovieEntry._ID,
                LocalMovieContract.MovieEntry.COLUMN_NAME_ID,
                LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_OVERVIEW ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_TITLE,
                LocalMovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE
        };

        String sortOrder = LocalMovieContract.MovieEntry._ID + " DESC";

        Cursor cursor = database.query(
                LocalMovieContract.MovieEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.d(MainActivity.TAG,cursor.getString(0));
            Log.d(MainActivity.TAG,cursor.getString(1));
            Log.d(MainActivity.TAG,cursor.getString(2));
            Log.d(MainActivity.TAG,cursor.getString(3));
            Log.d(MainActivity.TAG,cursor.getString(4));

            Movie movie = new Movie();
            movie.setId(Long.parseLong(cursor.getString(1)));
            movie.setPoster_path(cursor.getString(2));
            movie.setOverview(cursor.getString(3));
            movie.setRelease_date(cursor.getString(4));
            movie.setTitle(cursor.getString(5));
            movie.setVote_average(cursor.getString(6));

            cursor.moveToNext();
        }

        return 0;
    }


    @Override
    protected void onPostExecute(Integer integer) {

    }
}
