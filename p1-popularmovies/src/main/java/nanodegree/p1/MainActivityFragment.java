package nanodegree.p1;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import nanodegree.p1.data.MovieDBAsyncTask;


public class MainActivityFragment extends Fragment implements AbsListView.OnScrollListener {
    public GridView gridview;
    private Menu menu;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gridview = (GridView) getActivity().findViewById(R.id.fragment).findViewById(R.id.gridview);
        gridview.setAdapter(new MoviePosterAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        new MovieDBAsyncTask(gridview).execute();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //nop
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        //TODO: handle further movie fetch here, if user scrolls till end of list
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_sort_popular).setVisible(false);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_popular) {
            menu.findItem(R.id.action_sort_rating).setVisible(true);
            menu.findItem(R.id.action_sort_popular).setVisible(false);

            MoviePosterAdapter.setSortModePopular(true);
            gridview.invalidateViews();
            return true;
        } else if (id == R.id.action_sort_rating) {
            menu.findItem(R.id.action_sort_popular).setVisible(true);
            menu.findItem(R.id.action_sort_rating).setVisible(false);

            MoviePosterAdapter.setSortModePopular(false);
            gridview.invalidateViews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
