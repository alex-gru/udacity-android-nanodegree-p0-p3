package nanodegree.p1p2.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import nanodegree.p1p2.MainActivity;
import nanodegree.p1p2.MovieGridFragment;
import nanodegree.p1p2.MoviePosterAdapter;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class LocalMovieLoaderAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private final AppCompatActivity activity;
    private List<Movie> movies_favorites = new ArrayList<>();

    public LocalMovieLoaderAsyncTask(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Integer doInBackground(Void... params) {

        SQLiteDatabase database = MainActivity.localMovieHelper.getReadableDatabase();

        String[] projection = {
                LocalMovieContract.MovieEntry.COLUMN_NAME_ID,
                LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_BYTES,
                LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_OVERVIEW ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE ,
                LocalMovieContract.MovieEntry.COLUMN_NAME_TITLE,
                LocalMovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE
        };

        String sortOrder = LocalMovieContract.MovieEntry._ID + " ASC";

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
            Movie movie = new Movie();
            movie.setId(Long.parseLong(cursor.getString(0)));
            movie.setMoviePosterByteArray(cursor.getBlob(1));
            movie.setPoster_path(cursor.getString(2));
            movie.setOverview(cursor.getString(3));
            movie.setRelease_date(cursor.getString(4));
            movie.setTitle(cursor.getString(5));
            movie.setVote_average(cursor.getString(6));

            movies_favorites.add(movie);
            cursor.moveToNext();
        }

        return 0;
    }


    @Override
    protected void onPostExecute(Integer integer) {

        MovieGridFragment.movies_favorites = movies_favorites;
        MoviePosterAdapter.updateCount();
        MovieGridFragment.gridview.invalidateViews();
    }
}
