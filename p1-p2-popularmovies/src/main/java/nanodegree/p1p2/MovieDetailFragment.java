package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
public class MovieDetailFragment extends Fragment {

    public static Movie movie;
    private ListView trailerListView;
    public static TextView noTrailersTextView;
    private Menu menu;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_moviedetail, container, false);

        setHasOptionsMenu(true);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (!MainActivity.isTablet) {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        trailerListView = (ListView) view.findViewById(R.id.trailerListView);
        trailerListView.setAdapter(new TrailerAdapter(getActivity(), inflater));

        noTrailersTextView = (TextView) view.findViewById(R.id.noTrailersTextview);

        int gridPosition = getArguments().getInt("gridPosition");
        updateMovieDetailUI(gridPosition);

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

    public void updateMovieDetailUI(int gridPosition) {
        if (MoviePosterAdapter.count > 0 ) {
            if (MovieGridFragment.sortModePopular) {
                movie = MovieGridFragment.movies_most_popular.get(gridPosition);
            } else {
                movie = MovieGridFragment.movies_top_rated.get(gridPosition);
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

            Button showReviewButton= (Button) view.findViewById(R.id.showReviewsBtn);
            showReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReviewFragment reviewFragment = new ReviewFragment();
                    ReviewFragment.movie = movie;

                    int container = -1;
                    if (MainActivity.isTablet) {
                        container = R.id.detailfragment_container;
                    } else {
                        container = R.id.gridfragment_container;
                    }

                    getFragmentManager().beginTransaction()
                            .replace(container, reviewFragment)
                            .addToBackStack(null)
                            .commit();
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
        if (!MainActivity.isTablet) {
            inflater.inflate(R.menu.menu, menu);

            menu.findItem(R.id.action_sort_popular).setVisible(false);
            menu.findItem(R.id.action_sort_rating).setVisible(false);
        }
        this.menu = menu;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
