package nanodegree.p1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import nanodegree.p1.data.Movie;

public class MoviePosterAdapter extends BaseAdapter {
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
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 800));
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
        Picasso.with(mContext).load(movie.getFullPosterPath()).into(imageView);
        return imageView;
    }

    public static void setSortModePopular(boolean popular) {
        if (popular) {
            count = MovieGridFragment.movies_most_popular == null ? 0 : MovieGridFragment.movies_most_popular.size();
            MovieGridFragment.sortModePopular = true;
        } else {
            count = MovieGridFragment.movies_top_rated == null ? 0 : MovieGridFragment.movies_top_rated.size();
            MovieGridFragment.sortModePopular = false;
        }
    }


    public static void updateCount() {
        count = MovieGridFragment.movies_most_popular.size();
        // same as
        //count = MovieGridFragment.movies_top_rated.size();

    }
}
