package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "NANODEGREE.P1P2";
    public static boolean isHorizontalTablet;
    public static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar)findViewById(R.id.progress);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        isHorizontalTablet = getResources().getBoolean(R.bool.isTablet);

        checkFragments();
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
}
