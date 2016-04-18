package nanodegree.p1p2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

        if (MovieGridFragment.grid_category.equals(MovieGridFragment.GRID_CATEGORY.FAVORITES)
                && count == 0)
            MovieGridFragment.noFavoritesHint.setVisibility(View.VISIBLE);
        else
            MovieGridFragment.noFavoritesHint.setVisibility(View.GONE);
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
        } else {
            imageView = (ImageView) convertView;
        }
        if (MainActivity.isHorizontalTablet)
        {
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, POSTER_HEIGHT_TABLET));
        } else {
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, POSTER_HEIGHT_PHONE));
        }
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Movie movie = null;

        // progress bar handling - shown until poster is fetched and displayed.
        // resulted from various sources:
        // http://stackoverflow.com/a/22786818/2472398
        // http://stackoverflow.com/questions/22143157/android-picasso-placeholder-and-error-image-styling
        // http://stackoverflow.com/q/21333866/2472398

        if (MainActivity.progressBar.getVisibility() != View.VISIBLE)
            MainActivity.progressBar.setVisibility(View.VISIBLE);

        switch (MovieGridFragment.grid_category) {
            case MOST_POPULAR:
                if (position >= MovieGridFragment.movies_most_popular.size())
                    return imageView;
                movie = MovieGridFragment.movies_most_popular.get(position);
                Picasso.with(mContext).load(movie.getFullPosterPath())
                        .placeholder(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.posterplaceholder, null))
                        .error(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.posterplaceholder, null))
                        .into(imageView, new MainActivity.ProgressBarCallBack((MainActivity) mContext));
                break;
            case TOP_RATED:
                if (position >= MovieGridFragment.movies_top_rated.size())
                    return imageView;
                movie = MovieGridFragment.movies_top_rated.get(position);
                Picasso.with(mContext).load(movie.getFullPosterPath())
                        .placeholder(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.posterplaceholder, null))
                        .error(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.posterplaceholder, null))
                        .into(imageView, new MainActivity.ProgressBarCallBack((MainActivity) mContext));
                break;
            case FAVORITES:
                if (position >= MovieGridFragment.movies_favorites.size())
                    return imageView;
                movie = MovieGridFragment.movies_favorites.get(position);
                Bitmap poster = BitmapFactory.decodeByteArray(movie.getMoviePosterByteArray(), 0, movie.getMoviePosterByteArray().length);
                imageView.setImageBitmap(poster);
                MainActivity.progressBar.setVisibility(View.GONE);
                break;
        }

        return imageView;
    }


    public static void updateCount() {
        switch (MovieGridFragment.grid_category) {
            case MOST_POPULAR:
                count = MovieGridFragment.movies_most_popular.size();
                break;
            case TOP_RATED:
                count = MovieGridFragment.movies_top_rated.size();
                break;
            case FAVORITES:
                count = MovieGridFragment.movies_favorites.size();
                break;
        }
    }
}
