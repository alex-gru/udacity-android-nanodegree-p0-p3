package nanodegree.p1p2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.Review;
import nanodegree.p1p2.data.ReviewAsyncTask;

/**
 * Created by Alex Gru on 03-Apr-16.
 */
public class ReviewFragment extends Fragment {

    private View view;
    public static ListView reviewListView;
    public static TextView noReviewsTextView;
    public static Movie movie;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_review, container, false);
        reviewListView = (ListView) view.findViewById(R.id.reviewlistview);

        setHasOptionsMenu(true);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (!MainActivity.isTablet) {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviereviews));
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        reviewListView.setAdapter(new ReviewAdapter(getActivity(),inflater));

        noReviewsTextView = (TextView) view.findViewById(R.id.noReviewsTextview);

        if (MovieDetailFragment.movie.getReviews() == null) {
            new ReviewAsyncTask(movie, (AppCompatActivity) getActivity(),reviewListView).execute();
        } else {
            ReviewAdapter.updateCount(movie.getReviews().size());
            reviewListView.invalidateViews();
            MainActivity.setListViewHeightBasedOnItems(reviewListView);

            if (ReviewFragment.movie.getReviews().isEmpty()) {
                noReviewsTextView.setVisibility(View.VISIBLE);
            } else {
                noReviewsTextView.setVisibility(View.GONE);
            }
        }
        return view;
    }
}
