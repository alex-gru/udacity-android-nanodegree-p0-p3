package nanodegree.p1.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nanodegree.p1.MainActivity;
import nanodegree.p1.MovieGridFragment;
import nanodegree.p1.MoviePosterAdapter;
import nanodegree.p1.R;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDBAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private String THE_MOVIE_DB_API_KEY = null;
    private String MOST_POPULAR_URL = "http://api.themoviedb.org/3/movie/popular/";
    private String TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated/";
    private final GridView gridView;
    String result_most_popular = "";
    String result_top_rated = "";

    public MovieDBAsyncTask(GridView gridview) {
        this.gridView = gridview;
        try {
            THE_MOVIE_DB_API_KEY =  new BufferedReader(new InputStreamReader(gridview.getResources().openRawResource(R.raw.themoviedb))).readLine();
            MOST_POPULAR_URL +=  "?api_key=" + THE_MOVIE_DB_API_KEY;
            TOP_RATED_URL +=  "?api_key=" + THE_MOVIE_DB_API_KEY;
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Could not read API key. Check if 'themoviedb.txt' is present.", e);
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {

        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        try {
            result_most_popular = getMovieDataFromURL(MOST_POPULAR_URL);
            result_top_rated = getMovieDataFromURL(TOP_RATED_URL);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception occured while fetching movie information from The Movie DB.", e);
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private String getMovieDataFromURL(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String currLine;

            while ((currLine = in.readLine()) != null) {
                sb.append(currLine).append("\n");
            }
            in.close();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(Integer integer) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json_most_popular = mapper.readValue(result_most_popular, JsonNode.class).get("results");
            JsonNode json_top_rated = mapper.readValue(result_top_rated, JsonNode.class).get("results");
            MovieGridFragment.movies_most_popular = mapper.treeToValue(json_most_popular, Movie[].class);
            MovieGridFragment.movies_top_rated = mapper.treeToValue(json_top_rated, Movie[].class);

            if (gridView.getCount() == -1)
            {
                MoviePosterAdapter.setSortModePopular(true);
            }
            gridView.invalidateViews();
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Exception occured while parsing JSON data.", e);
        }
    }
}
