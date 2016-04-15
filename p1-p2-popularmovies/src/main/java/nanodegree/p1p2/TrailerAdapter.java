package nanodegree.p1p2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        row.setOnClickListener(new TrailerOnClickListener((MainActivity) mContext, position));
        return row;
    }

    public static void updateCount(int count) {
        TrailerAdapter.count = count;
        TextView noTrailersTextView = MovieDetailFragment.noTrailersTextView;

        if (count == 0) {
            noTrailersTextView.setVisibility(View.VISIBLE);
        } else {
            noTrailersTextView.setVisibility(View.GONE);
        }
    }

    private class TrailerOnClickListener implements View.OnClickListener {

        private final int trailerIdx;
        private final MainActivity activity;

        public TrailerOnClickListener(MainActivity activity, int trailerIdx) {
            this.trailerIdx = trailerIdx;
            this.activity = activity;
        }

            @Override
            public void onClick(View v) {
                if (!activity.isNetworkAvailable()) {

                } else {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MovieDetailFragment.movie.getTrailers().get(trailerIdx).getYoutubeURL())));
                }
            }
    }
}
