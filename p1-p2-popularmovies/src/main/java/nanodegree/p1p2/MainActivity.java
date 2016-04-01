package nanodegree.p1p2;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {
    public static final String TAG = "NANODEGREE.P1P2";
    public static boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        isTablet = getResources().getBoolean(R.bool.isTablet);

        if (findViewById(R.id.gridfragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            MovieGridFragment movieGridFragment = new MovieGridFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.gridfragment_container, movieGridFragment).commit();
            if (isTablet)
            {
                Bundle args = new Bundle();
                args.putInt("gridPosition", 0);
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                movieDetailFragment.setArguments(args);

                if (findViewById(R.id.detailfragment_container) != null) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.detailfragment_container, movieDetailFragment).commit();
                }
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
}
