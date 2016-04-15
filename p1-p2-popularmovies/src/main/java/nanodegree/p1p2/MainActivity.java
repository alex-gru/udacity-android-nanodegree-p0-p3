package nanodegree.p1p2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;

import nanodegree.p1p2.data.LocalMovieHelper;
import nanodegree.p1p2.data.LocalMovieLoaderAsyncTask;
import nanodegree.p1p2.data.MovieAsyncTask;


public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "NANODEGREE.P1P2";
    public static boolean isHorizontalTablet;
    public static ProgressBar progressBar;
    public static ImageButton favoriteButton;
    public static ImageButton unfavoriteButton;
    public static LocalMovieHelper localMovieHelper;
    public static SQLiteDatabase movieDB;
    public static Menu menu;
    private static Snackbar snackbar;
    public boolean offline = false;
    public static CountDownTimer networkAlertTimer;
    public static boolean showNetworkAlert = true;
    private View activityContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        activityContainer = findViewById(R.id.activityContainer);
        snackbar = Snackbar.make(this.activityContainer, "No connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("REFRESH",new SnackBarClickListener(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        android.os.Debug.waitForDebugger();
        networkAlertTimer = new CountDownTimer(10000, 10000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                showNetworkAlert = true;
            }
        };
        if (!checkIfNetworkAvailable()) {
            MovieGridFragment.grid_category = MovieGridFragment.GRID_CATEGORY.FAVORITES;
        }


        progressBar = (ProgressBar)findViewById(R.id.progress);
        progressBar = (ProgressBar)findViewById(R.id.progress);

        favoriteButton = (ImageButton) findViewById(R.id.favoriteButton);
        unfavoriteButton = (ImageButton) findViewById(R.id.unfavoriteButton);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        isHorizontalTablet = getResources().getBoolean(R.bool.isTablet);

        checkFragments();

        setupDB();
    }

    private void setupDB() {
        localMovieHelper = new LocalMovieHelper(this);
        movieDB = localMovieHelper.getWritableDatabase();
//        localMovieHelper.onUpgrade(movieDB,0,0);

        new LocalMovieLoaderAsyncTask(this).execute();
    }

    /**
     * This helper method is used for handling all different cases for orientation changes, like
     * tablet horizontal mode to vertical mode (different layouts)
     */
    private void checkFragments() {
        Fragment fragmentInDetailContainer = getSupportFragmentManager().findFragmentById(R.id.detailfragment_container);
        Fragment fragmentInGridContainer = getSupportFragmentManager().findFragmentById(R.id.gridfragment_container);

        if (fragmentInDetailContainer != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragmentInDetailContainer)
                    .commit();
        }
        if (fragmentInGridContainer != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragmentInGridContainer)
                    .commit();
        }

        // found here: http://stackoverflow.com/a/28850280
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fm.popBackStack();
        }

        if (isHorizontalTablet) {
            MovieGridFragment gridFragment  = new MovieGridFragment();
            MovieDetailFragment detailFragment = new MovieDetailFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.gridfragment_container, gridFragment,MovieGridFragment.TAG)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailfragment_container, detailFragment,MovieDetailFragment.TAG)
                    .commit();
        } else {
            MovieGridFragment gridFragment  = new MovieGridFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.gridfragment_container, gridFragment,MovieGridFragment.TAG)
                    .commit();

            if (fragmentInGridContainer instanceof MovieDetailFragment) {
                MainActivity.progressBar.setVisibility(View.GONE);
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .replace(R.id.gridfragment_container, movieDetailFragment, MovieDetailFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onBackStackChanged() {

        // source: http://stackoverflow.com/a/20314570/2472398
        boolean canback = getSupportFragmentManager().getBackStackEntryCount() > 0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return super.onSupportNavigateUp();
    }

    /**
     * source: http://blog.lovelyhq.com/setting-listview-height-depending-on-the-items/
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (MovieGridFragment.movies_most_popular.isEmpty()
                || MovieGridFragment.movies_top_rated.isEmpty())
            new MovieAsyncTask(this).execute();
        switch(item.getItemId()){
            case R.id.action_show_most_popular:
                menu.findItem(R.id.action_show_favorites).setVisible(true);
                menu.findItem(R.id.action_show_top_rated).setVisible(true);
                menu.findItem(R.id.action_show_most_popular).setVisible(false);
                getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_most_popular));

                MovieGridFragment.grid_category = MovieGridFragment.GRID_CATEGORY.MOST_POPULAR;
                MoviePosterAdapter.updateCount();
                MovieGridFragment.gridview.invalidateViews();
                MovieGridFragment.gridview.smoothScrollToPosition(0);
                return true;
            case R.id.action_show_top_rated:
                menu.findItem(R.id.action_show_favorites).setVisible(true);
                menu.findItem(R.id.action_show_most_popular).setVisible(true);
                menu.findItem(R.id.action_show_top_rated).setVisible(false);
                getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_top_rated));

                MovieGridFragment.grid_category = MovieGridFragment.GRID_CATEGORY.TOP_RATED;
                MoviePosterAdapter.updateCount();
                MovieGridFragment.gridview.invalidateViews();
                MovieGridFragment.gridview.smoothScrollToPosition(0);
                return true;

            case R.id.action_show_favorites:
                menu.findItem(R.id.action_show_favorites).setVisible(false);
                menu.findItem(R.id.action_show_top_rated).setVisible(true);
                menu.findItem(R.id.action_show_most_popular).setVisible(true);

                getSupportActionBar().setTitle(getResources().getString(R.string.toolbar_title_favorites));

                MovieGridFragment.grid_category = MovieGridFragment.GRID_CATEGORY.FAVORITES;
                new LocalMovieLoaderAsyncTask(this).execute();
                MoviePosterAdapter.updateCount();
                MovieGridFragment.gridview.invalidateViews();
                MovieGridFragment.gridview.smoothScrollToPosition(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean checkIfNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        offline =  ! (activeNetworkInfo != null && activeNetworkInfo.isConnected());

        if (!offline) {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        } else {
            if (!snackbar.isShown()) {
                snackbar.show();
            }
        }
        return !offline;
    }

    public static class ProgressBarCallBack implements Callback {

        private final MainActivity activity;

        ProgressBarCallBack(MainActivity activity) {
            this.activity = activity;
        }
        @Override
        public void onSuccess() {
            MainActivity.progressBar.setVisibility(View.GONE);
        }

        @Override
        public void onError() {

            MainActivity.progressBar.setVisibility(View.GONE);

            if (MovieDetailFragment.posterFullScreenImageView != null)
                MovieDetailFragment.posterFullScreenImageView.setVisibility(View.GONE);
            if (MovieDetailFragment.posterFullScreenExitIcon != null)
                MovieDetailFragment.posterFullScreenExitIcon.setVisibility(View.GONE);

            activity.checkIfNetworkAvailable();
        }
    }

    private class SnackBarClickListener implements View.OnClickListener {

        private final MainActivity activity;

        public SnackBarClickListener(MainActivity activity) {
            this.activity = activity;
        }
        @Override
        public void onClick(View v) {
            MovieGridFragment.gridview.invalidateViews();
        }
    }
}
