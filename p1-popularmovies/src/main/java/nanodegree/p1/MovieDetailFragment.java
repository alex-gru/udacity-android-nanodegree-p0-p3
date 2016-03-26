package nanodegree.p1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import nanodegree.p1.data.Movie;

/**
 * Created by alexgru on 24-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDetailFragment extends Fragment {

    private Movie movie;
    private Menu menu;

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_moviedetail, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        ActionBar toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(getResources().getString(R.string.toolbar_title_moviedetail));
        toolbar.setDisplayHomeAsUpEnabled(true);
        
        int gridPosition = getArguments().getInt("gridPosition");
        if (MovieGridFragment.sortModePopular) {
            movie = MovieGridFragment.movies_most_popular[gridPosition];
        } else {
            movie = MovieGridFragment.movies_top_rated[gridPosition];
        }

        ImageView posterImageView = (ImageView) getView().findViewById(R.id.posterImageView);
        posterImageView.setMinimumWidth(Integer.parseInt(Movie.POSTER_WIDTH));
        posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(getContext()).load(movie.getFullPosterPath()).into(posterImageView);

        TextView titleTextView = (TextView) getView().findViewById(R.id.titleTextView);
        titleTextView.setText(movie.getTitle());
        TextView yearTextView = (TextView) getView().findViewById(R.id.yearTextView);
        yearTextView.setText(Integer.toString(movie.getYear()));
        TextView voteAverageTextView = (TextView) getView().findViewById(R.id.voteAverageTextView);
        voteAverageTextView.setText(movie.getVote_average() + "/10");
        TextView overviewTextView = (TextView) getView().findViewById(R.id.overviewTextView);
        overviewTextView.setText(movie.getOverview());

        Button favoriteButton= (Button) getView().findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Stage 2 :-)",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_sort_popular).setVisible(false);
        menu.findItem(R.id.action_sort_rating).setVisible(false);
        this.menu = menu;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
