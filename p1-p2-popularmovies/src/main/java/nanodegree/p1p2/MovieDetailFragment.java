package nanodegree.p1p2;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import nanodegree.p1p2.data.LocalMovieContract;
import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.ReviewAsyncTask;
import nanodegree.p1p2.data.TrailerAsyncTask;

/**
 * Created by alexgru on 24-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDetailFragment extends Fragment {

    public static boolean active = false;
    public static Movie movie;
    public static String TAG = "MOVIEDETAILS";
    public static ListView trailerListView;
    public static ListView reviewListView;
    public static TextView noReviewsTextView;
    public static TextView noTrailersTextView;
    private Menu menu;
    private static View view;
    private static ScrollView scrollView;
    public static ImageView posterImageView;
    public static ImageView posterFullScreenImageView;
    public static ImageView posterFullScreenIcon;
    public static ImageView posterFullScreenExitIcon;

    public static void scrollUp () {
        scrollView.setScrollY(0);
    }

    @Override
    public void onPause() {
        super.onPause();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_moviegrid));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (MainActivity.isHorizontalTablet) {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviegrid));
            toolbar.setDisplayHomeAsUpEnabled(false);
            setHasOptionsMenu(true);
        } else {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
            toolbar.setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(false);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        android.os.Debug.waitForDebugger();
        view = inflater.inflate(R.layout.fragment_moviedetail, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.movieDetailScrollView);

        trailerListView = (ListView) view.findViewById(R.id.trailerListView);
        trailerListView.setAdapter(new TrailerAdapter(getActivity(), inflater));

        reviewListView = (ListView) view.findViewById(R.id.reviewlistview);
        reviewListView.setAdapter(new ReviewAdapter(getActivity(),inflater));

        noReviewsTextView = (TextView) view.findViewById(R.id.noReviewsTextview);
        noTrailersTextView = (TextView) view.findViewById(R.id.noTrailersTextview);

        MainActivity.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "Saving in local DB...");

                Bitmap moviePosterBitmap = ((BitmapDrawable)posterImageView.getDrawable()).getBitmap();
                byte[] moviePosterByteArray = getBitmapAsByteArray(moviePosterBitmap);

                ContentValues values = new ContentValues();
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_ID,movie.getId());
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_BYTES,moviePosterByteArray);
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_POSTER_PATH,movie.getPoster_path());
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_OVERVIEW,movie.getOverview());
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_RELEASE_DATE,movie.getRelease_date());
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_TITLE,movie.getTitle());
                values.put(LocalMovieContract.MovieEntry.COLUMN_NAME_VOTE_AVERAGE,movie.getVote_average());

                long rowId = MainActivity.movieDB.insert(LocalMovieContract.MovieEntry.TABLE_NAME, null, values);
//                    android.os.Debug.waitForDebugger();
                MainActivity.favoriteButton.setVisibility(View.GONE);
                MainActivity.unfavoriteButton.setVisibility(View.VISIBLE);
            }
        });

        MainActivity.unfavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "Todo: unfavorite...");

                MainActivity.unfavoriteButton.setVisibility(View.GONE);
                MainActivity.favoriteButton.setVisibility(View.VISIBLE);
            }
        });

        updateMovieDetailUI();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateMovieDetailUI() {
        if (MoviePosterAdapter.count > 0 ) {

            switch (MovieGridFragment.grid_category) {

                case MOST_POPULAR:
                    movie = MovieGridFragment.movies_most_popular.get(MovieGridFragment.selectedPositionInGrid);
                    break;
                case TOP_RATED:
                    movie = MovieGridFragment.movies_top_rated.get(MovieGridFragment.selectedPositionInGrid);
                    break;
                case FAVORITES:
                    movie = MovieGridFragment.movies_favorites.get(MovieGridFragment.selectedPositionInGrid);
                    break;
            }

//            android.os.Debug.waitForDebugger();
            if (movie.getTrailers() == null) {
                new TrailerAsyncTask(movie, (AppCompatActivity) getActivity(),trailerListView).execute();
            } else {
                TrailerAdapter.updateCount(movie.getTrailers().size());
                trailerListView.invalidateViews();
                MainActivity.setListViewHeightBasedOnItems(trailerListView);

                if (movie.getTrailers().isEmpty()) {
                    noTrailersTextView.setVisibility(View.VISIBLE);
                } else {
                    noTrailersTextView.setVisibility(View.GONE);
                }
            }

            if (movie.getReviews() == null) {
                new ReviewAsyncTask(movie, (AppCompatActivity) getActivity(),reviewListView).execute();
            } else {
                ReviewAdapter.updateCount(movie.getReviews().size());
                reviewListView.invalidateViews();

                if (movie.getReviews().isEmpty()) {
                    noReviewsTextView.setVisibility(View.VISIBLE);
                } else {
                    noReviewsTextView.setVisibility(View.GONE);
                }
            }

            if (!MainActivity.isHorizontalTablet && !MovieGridFragment.grid_category.equals(MovieGridFragment.GRID_CATEGORY.FAVORITES)) {
                MainActivity.favoriteButton.setVisibility(View.VISIBLE);
            }

            posterFullScreenImageView = (ImageView) view.findViewById(R.id.posterFullScreenImageView);
            posterFullScreenImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            posterFullScreenImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posterFullScreenImageView.setVisibility(View.GONE);
                    posterFullScreenExitIcon.setVisibility(View.GONE);
                }
            });
            posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
            posterImageView.setMinimumWidth(Integer.parseInt(Movie.POSTER_WIDTH));
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posterFullScreenImageView.setVisibility(View.VISIBLE);
                    posterFullScreenExitIcon.setVisibility(View.VISIBLE);
                    MainActivity.progressBar.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(movie.getFullPosterPathHighRes()).placeholder(R.color.posterPlaceholderColor).into(posterFullScreenImageView, new MainActivity.ProgressBarCallBack());
                }
            });

            MainActivity.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(movie.getFullPosterPath()).placeholder(R.color.posterPlaceholderColor).into(posterImageView, new MainActivity.ProgressBarCallBack());

            posterFullScreenIcon = (ImageView) view.findViewById(R.id.posterIcon);
            posterFullScreenIcon.setVisibility(View.VISIBLE);
            posterFullScreenExitIcon = (ImageView) view.findViewById(R.id.posterFullScreenIcon);
            posterFullScreenExitIcon.setVisibility(View.GONE);

            TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            titleTextView.setText(movie.getTitle());
            TextView yearTextView = (TextView) view.findViewById(R.id.yearTextView);
            yearTextView.setText(Integer.toString(movie.getYear()));
            TextView voteAverageTextView = (TextView) view.findViewById(R.id.voteAverageTextView);
            voteAverageTextView.setText(movie.getVote_average() + "/10");
            TextView overviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
            overviewTextView.setText(movie.getOverview());
        }
    }

    /**
     * source: http://stackoverflow.com/a/9357943/2472398
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (!MainActivity.isHorizontalTablet) {

            if (!menu.hasVisibleItems()) {
                inflater.inflate(R.menu.menu, menu);
            }

            menu.findItem(R.id.action_show_most_popular).setVisible(false);
            menu.findItem(R.id.action_show_top_rated).setVisible(false);
        }
        this.menu = menu;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
