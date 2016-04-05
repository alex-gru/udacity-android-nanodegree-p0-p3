package nanodegree.p1p2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nanodegree.p1p2.data.Movie;
import nanodegree.p1p2.data.Review;

/**
 * Created by Alex Gru on 03-Apr-16.
 */
public class ReviewFragment extends Fragment {

    private View view;
    private List<Review> reviews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_review, container, false);

        setHasOptionsMenu(true);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (!MainActivity.isTablet) {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
            toolbar.setDisplayHomeAsUpEnabled(true);
        }

        return view;
    }
}
