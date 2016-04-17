package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.MovieAsyncTask;


public class MovieGridFragment extends Fragment {
    public static final String TAG = "MOVIEGRID";
    public static GridView gridview;

    public static List<Movie> movies_top_rated;
    public static List<Movie> movies_most_popular;
    public static List<Movie> movies_favorites;

    public static GRID_CATEGORY grid_category = GRID_CATEGORY.MOST_POPULAR;
    public enum GRID_CATEGORY {
        MOST_POPULAR,
        TOP_RATED,
        FAVORITES
    };

    public static int lastPositionInGrid = -1;
    public static int selectedPositionInGrid = 0;
    public static int page = 0;
    private static int scrollPositionInGrid = 0;

    public MovieGridFragment() {

        if (movies_most_popular == null || movies_top_rated == null) {
            movies_most_popular = new LinkedList<Movie>();
            movies_top_rated = new LinkedList<Movie>();
            movies_favorites = new LinkedList<Movie>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moviegrid, container, false);
        setHasOptionsMenu(true);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        switch (grid_category) {

            case MOST_POPULAR:
                toolbar.setTitle(getResources().getString(R.string.toolbar_title_most_popular));
                break;
            case TOP_RATED:
                toolbar.setTitle(getResources().getString(R.string.toolbar_title_top_rated));
                break;
            case FAVORITES:
                toolbar.setTitle(getResources().getString(R.string.toolbar_title_favorites));
                break;
        }
        toolbar.setDisplayHomeAsUpEnabled(false);

        gridview =(GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new MoviePosterAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                selectedPositionInGrid = position;

                if (MainActivity.isHorizontalTablet)
                {
                    MovieDetailFragment.scrollUp();
                    MovieDetailFragment detailFragment = (MovieDetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(MovieDetailFragment.TAG);
                    detailFragment.updateMovieDetailUI();

                } else {
                    MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .replace(R.id.gridfragment_container, movieDetailFragment, MovieDetailFragment.TAG)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        gridview.setSelection(scrollPositionInGrid);
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // nop
            }

            // approach as found here:  http://stackoverflow.com/a/16982317
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int _lastItem = firstVisibleItem + visibleItemCount;

                if (grid_category.equals(GRID_CATEGORY.TOP_RATED)
                        || grid_category.equals(GRID_CATEGORY.MOST_POPULAR)) {
                    if (_lastItem > 0 && totalItemCount > 0) {
                        if (_lastItem == MoviePosterAdapter.count) {
                            if (lastPositionInGrid < _lastItem || ((MainActivity) getActivity()).offline) {
                                lastPositionInGrid = _lastItem;
                                // Last item is fully visible.
                                Log.d(MainActivity.TAG, "Now fetch next page from theMovieDB.");
                                new MovieAsyncTask((AppCompatActivity) getActivity()).execute();
                            }
                        }
                    }
                }
            }
        });

        if (movies_most_popular.isEmpty() || movies_top_rated.isEmpty())
            new MovieAsyncTask((AppCompatActivity) getActivity()).execute();

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.menu, menu);
        }

        switch (grid_category) {

            case MOST_POPULAR:
                menu.findItem(R.id.action_show_most_popular).setVisible(false);
                break;
            case TOP_RATED:
                menu.findItem(R.id.action_show_top_rated).setVisible(false);
                break;
            case FAVORITES:
                menu.findItem(R.id.action_show_favorites).setVisible(false);
                break;
        }
        MainActivity.menu = menu;
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPositionInGrid = gridview.getFirstVisiblePosition();
    }
}
