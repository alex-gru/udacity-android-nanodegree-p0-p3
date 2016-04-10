package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.ReviewAsyncTask;
import nanodegree.p1p2.data.TrailerAsyncTask;

/**
 * Created by alexgru on 24-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDetailFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {

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

        if (MainActivity.isHorizontalTablet) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_moviegrid));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setHasOptionsMenu(true);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        updateMovieDetailUI();
        active = true;
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
            if (MovieGridFragment.sortModePopular) {
                movie = MovieGridFragment.movies_most_popular.get(MovieGridFragment.selectedPositionInGrid);
            } else {
                movie = MovieGridFragment.movies_top_rated.get(MovieGridFragment.selectedPositionInGrid);
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

            ImageView posterImageView = (ImageView) view.findViewById(R.id.posterImageView);
            posterImageView.setMinimumWidth(Integer.parseInt(Movie.POSTER_WIDTH));
            posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getContext()).load(movie.getFullPosterPath()).into(posterImageView);

            TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
            titleTextView.setText(movie.getTitle());
            TextView yearTextView = (TextView) view.findViewById(R.id.yearTextView);
            yearTextView.setText(Integer.toString(movie.getYear()));
            TextView voteAverageTextView = (TextView) view.findViewById(R.id.voteAverageTextView);
            voteAverageTextView.setText(movie.getVote_average() + "/10");
            TextView overviewTextView = (TextView) view.findViewById(R.id.overviewTextView);
            overviewTextView.setText(movie.getOverview());

            Button favoriteButton= (Button) view.findViewById(R.id.favoriteButton);
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Stage 2 :-)", Toast.LENGTH_SHORT).show();
                }
            });
        }
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

            menu.findItem(R.id.action_sort_popular).setVisible(false);
            menu.findItem(R.id.action_sort_rating).setVisible(false);
        }
        this.menu = menu;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackStackChanged() {
        active = false;
        ((MainActivity)getActivity()).onBackStackChanged();
    }
}
