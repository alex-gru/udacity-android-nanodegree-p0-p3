package nanodegree.p1p2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import nanodegree.p1p2.data.Movie;

public class ReviewAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final Context mContext;
    public static int count = -1;
    private ViewGroup parent;

    public ReviewAdapter(Context c, LayoutInflater inflater) {
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
        View row = inflater.inflate(R.layout.reviewrow, parent, false);

        Movie movie = MovieDetailFragment.movie;
        if (movie.getReviews() != null)
        {
            TextView reviewAuthor = (TextView) row.findViewById(R.id.reviewAuthor);
            reviewAuthor.setText(movie.getReviews().get(position).getAuthor());
            TextView reviewContent = (TextView) row.findViewById(R.id.reviewContent);
            reviewContent.setText(movie.getReviews().get(position).getContent());
        }

        row.setOnClickListener(new ReviewOnClickListener(position));
        return row;
    }

    public static void updateCount(int count) {
        ReviewAdapter.count = count;

        TextView noReviewsTextView = ReviewFragment.noReviewsTextView;

        if (count == 0) {
            noReviewsTextView.setVisibility(View.VISIBLE);
        } else {
            noReviewsTextView.setVisibility(View.GONE);
        }
    }

    private class ReviewOnClickListener implements View.OnClickListener {

        private final int trailerIdx;

        public ReviewOnClickListener(int trailerIdx) {
            this.trailerIdx = trailerIdx;
        }

            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Do something....",Toast.LENGTH_SHORT);
            }
    }
}
