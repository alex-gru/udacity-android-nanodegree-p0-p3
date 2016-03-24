package nanodegree.p1.data;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import nanodegree.p1.MainActivity;
import nanodegree.p1.MoviePosterAdapter;
import nanodegree.p1.R;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class MovieDBAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private static String THE_MOVIE_DB_API_KEY = null;
    private static String MOST_POPULAR_URL = null;
    private final GridView gridView;
    String result = "";

    public MovieDBAsyncTask(GridView gridview) {
        this.gridView = gridview;
        try {
            THE_MOVIE_DB_API_KEY =  new BufferedReader(new InputStreamReader(gridview.getResources().openRawResource(R.raw.themoviedb))).readLine();
            MOST_POPULAR_URL =  "http://api.themoviedb.org/3/movie/popular?api_key=" + THE_MOVIE_DB_API_KEY;
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Could not read API key. Check if 'themoviedb.txt' is present.", e);
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {

        if (android.os.Debug.isDebuggerConnected()) {
            android.os.Debug.waitForDebugger();
        }

        HttpURLConnection urlConnection = null;

        StringBuilder sb = null;
        try {
            URL url = new URL(MOST_POPULAR_URL);

            urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            sb = new StringBuilder();
            String currLine;

            while ((currLine = in.readLine()) != null) {
                sb.append(currLine).append("\n");
            }

            in.close();
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception occured while fetching movie information from The Movie DB.", e);
            e.printStackTrace();
            return -1;
        } finally {
            urlConnection.disconnect();
        }

        result = sb.toString();
        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {

        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode json = mapper.readValue(result, JsonNode.class).get("results");
            MoviePosterAdapter.movies = mapper.treeToValue(json, Movie[].class);
            gridView.invalidateViews();
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Exception occured while parsing JSON data.", e);
        }
    }
}
