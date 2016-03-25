package nanodegree.p1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import nanodegree.p1.data.Movie;

public class MoviePosterAdapter extends BaseAdapter {
    private Context mContext;
    public static Movie[] movies_top_rated;
    public static Movie[] movies_most_popular;
    public static boolean sortModePopular = true;
    private static int count = -1;

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
        if (sortModePopular) {
            movie = movies_most_popular[position];
        } else {
            movie = movies_top_rated[position];
        }
        Picasso.with(mContext).load(movie.getFullPosterPath()).into(imageView);
        return imageView;
    }

    public static void setSortModePopular(boolean popular) {
        if (popular) {
            count = movies_most_popular == null ? 0 : movies_most_popular.length;
            sortModePopular = true;
        } else {
            count = movies_top_rated == null ? 0 : movies_top_rated.length;
            sortModePopular = false;
        }
    }


}
