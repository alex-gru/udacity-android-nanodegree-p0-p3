package nanodegree.p1p2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import nanodegree.p1p2.data.Movie;

public class MoviePosterAdapter extends BaseAdapter {
    private static final int POSTER_HEIGHT_TABLET = 500;
    private static final int POSTER_HEIGHT_PHONE = 800;
    private Context mContext;

    public static int count = -1;

    public MoviePosterAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            if (MainActivity.isTablet)
            {
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, POSTER_HEIGHT_TABLET));
            } else {
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, POSTER_HEIGHT_PHONE));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Movie movie;
        if (MovieGridFragment.sortModePopular) {
            movie = MovieGridFragment.movies_most_popular.get(position);
        } else {
            movie = MovieGridFragment.movies_top_rated.get(position);
        }

        // set up progress bar, which is shown until poster is fetched and displayed.
        // resulted from various sources:
        // http://stackoverflow.com/a/22786818/2472398
        // http://stackoverflow.com/questions/22143157/android-picasso-placeholder-and-error-image-styling
        // http://stackoverflow.com/q/21333866/2472398

        final ProgressBar progressBar = (ProgressBar) ((RelativeLayout)parent.getParent().getParent().getParent().getParent()).findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        Picasso.with(mContext).load(movie.getFullPosterPath())
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        return imageView;
    }

    public static void setSortModePopular(boolean popular) {
        if (popular) {
            MovieGridFragment.sortModePopular = true;
        } else {
            MovieGridFragment.sortModePopular = false;
        }

        updateCount();
    }


    public static void updateCount() {
        count = MovieGridFragment.movies_most_popular.size();
    }
}
