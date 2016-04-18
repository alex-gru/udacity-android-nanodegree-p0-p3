package nanodegree.p1p2;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import nanodegree.p1p2.data.LocalMovieContract;
import nanodegree.p1p2.data.LocalMovieLoaderAsyncTask;
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
    public static RelativeLayout posterFullScreenView;
    public static ImageView posterFullScreenImageView;

    public static ImageButton favoriteButton;
    public static ImageButton unfavoriteButton;

    public static void scrollUp () {
        scrollView.setScrollY(0);
    }

    @Override
    public void onPause() {
        super.onPause();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_moviegrid));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        trailerListView.setVisibility(View.GONE);
        reviewListView.setVisibility(View.GONE);

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

        if (((MainActivity)getActivity()).checkIfNetworkAvailable()) {
            trailerListView.setVisibility(View.VISIBLE);
            reviewListView.setVisibility(View.VISIBLE);
        } else {
            trailerListView.setVisibility(View.GONE);
            reviewListView.setVisibility(View.GONE);
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

        favoriteButton = (ImageButton) view.findViewById(R.id.favoriteButton);
        unfavoriteButton = (ImageButton) view.findViewById(R.id.unfavoriteButton);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.TAG, "Saving in local DB...");

                boolean isAlreadyFavorite = checkIfFavorite(movie);

                if (isAlreadyFavorite) {
                    Toast.makeText(getContext(),"Already saved to Favorites",Toast.LENGTH_SHORT).show();
                    return;
                }

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
                favoriteButton.setVisibility(View.GONE);
                unfavoriteButton.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(),"Saved to Favorites",Toast.LENGTH_SHORT).show();

                new LocalMovieLoaderAsyncTask((AppCompatActivity) getActivity()).execute();
            }
        });

        unfavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfavoriteButton.setVisibility(View.GONE);
                favoriteButton.setVisibility(View.VISIBLE);

                String whereClause = LocalMovieContract.MovieEntry.COLUMN_NAME_ID + " = ?";
                String[] whereArgs = { String.valueOf(movie.getId()) };
                MainActivity.movieDB.delete(LocalMovieContract.MovieEntry.TABLE_NAME, whereClause, whereArgs);

                Toast.makeText(getContext(),"Removed from Favorites",Toast.LENGTH_SHORT).show();

                new LocalMovieLoaderAsyncTask((AppCompatActivity) getActivity()).execute();
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

            boolean isFavorite = false;
            switch (MovieGridFragment.grid_category) {

                case MOST_POPULAR:
                    movie = MovieGridFragment.movies_most_popular.get(MovieGridFragment.selectedPositionInGrid);
                    isFavorite = checkIfFavorite(movie);
                    break;
                case TOP_RATED:
                    movie = MovieGridFragment.movies_top_rated.get(MovieGridFragment.selectedPositionInGrid);
                    isFavorite = checkIfFavorite(movie);
                    break;
                case FAVORITES:
                    movie = MovieGridFragment.movies_favorites.get(MovieGridFragment.selectedPositionInGrid);
                    isFavorite = true;
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

            if (isFavorite) {
                favoriteButton.setVisibility(View.GONE);
                unfavoriteButton.setVisibility(View.VISIBLE);
            } else {
                favoriteButton.setVisibility(View.VISIBLE);
                unfavoriteButton.setVisibility(View.GONE);
            }

            posterFullScreenView = (RelativeLayout) view.findViewById(R.id.posterFullScreenView);
            posterFullScreenView.setVisibility(View.GONE);
            posterFullScreenImageView = (ImageView) view.findViewById(R.id.posterFullScreenImageView);
            posterFullScreenImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            posterFullScreenView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posterFullScreenView.setVisibility(View.GONE);
                }
            });
            posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
            posterImageView.setMinimumWidth(Movie.POSTER_WIDTH);
            posterImageView.setMinimumHeight(Movie.POSTER_HEIGHT);
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posterFullScreenView.setVisibility(View.VISIBLE);
                    MainActivity.progressBar.setVisibility(View.VISIBLE);
                    Picasso.with(getContext()).load(movie.getFullPosterPathHighRes()).placeholder(R.color.posterPlaceholderColor).into(posterFullScreenImageView, new MainActivity.ProgressBarCallBack((MainActivity) getActivity()));
                }
            });

            MainActivity.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(movie.getFullPosterPath()).placeholder(R.color.posterPlaceholderColor).into(posterImageView, new MainActivity.ProgressBarCallBack((MainActivity) getActivity()));

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

    private boolean checkIfFavorite(Movie movie) {
        for (int i = 0; i < MovieGridFragment.movies_favorites.size(); i++) {
            if (movie.getId() == MovieGridFragment.movies_favorites.get(i).getId())
                return true;
        }
        return false;
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
