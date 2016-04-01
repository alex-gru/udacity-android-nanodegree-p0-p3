package nanodegree.p1p2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import nanodegree.p1p2.data.Movie;

/**
 * Created by alexgru on 01-Apr-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class TrailerAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final Context mContext;
    public static int count = -1;
    private ViewGroup parent;

    public TrailerAdapter(Context c, LayoutInflater inflater) {
        this.mContext = c;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.parent = parent;
        View row = inflater.inflate(R.layout.trailerrow, parent, false);

        Movie movie = MovieDetailFragment.movie;
        if (movie.getTrailers() != null)
        {
            TextView trailerTitle = (TextView) row.findViewById(R.id.trailerTitle);
            trailerTitle.setText(movie.getTrailers().get(position).getName());

        }

        row.setOnClickListener(new TrailerOnClickListener(position));
        return row;
    }

    public static void updateCount(Movie movie) {
        count = movie.getTrailers().size();
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

    private class TrailerOnClickListener implements View.OnClickListener {

        private final int trailerIdx;

        public TrailerOnClickListener(int trailerIdx) {
            this.trailerIdx = trailerIdx;
        }

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"now open yt",Toast.LENGTH_SHORT);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MovieDetailFragment.movie.getTrailers().get(trailerIdx).getYoutubeURL())));
            }
    }
}
