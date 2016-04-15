package nanodegree.p1p2.data;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nanodegree.p1p2.MainActivity;
import nanodegree.p1p2.R;
import nanodegree.p1p2.TrailerAdapter;

/**
 * Created by alexgru on 22-Mar-16.
 * Android Developer Nanodegree
 * UDACITY
 */
public class TrailerAsyncTask extends AsyncTask<Void, Integer, Integer> {

    private final AppCompatActivity activity;
    private final Movie movie;
    private String THE_MOVIE_DB_API_KEY = null;
    private String VIDEOS_URL = "http://api.themoviedb.org/3/movie/<id>/videos";
    private ListView trailerListView;
    String result = "";

    public TrailerAsyncTask(Movie movie, AppCompatActivity activity, ListView trailerListView) {
        this.movie = movie;
        this.trailerListView = trailerListView;
        this.activity = activity;
        try {
            THE_MOVIE_DB_API_KEY =  new BufferedReader(new InputStreamReader(trailerListView.getResources().openRawResource(R.raw.themoviedb))).readLine();
            VIDEOS_URL = VIDEOS_URL.replace("<id>",String.valueOf(movie.getId())) + "?api_key=" + THE_MOVIE_DB_API_KEY;
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Could not read API key. Check if 'themoviedb.txt' is present.", e);
        }

        if (!((MainActivity)activity).isNetworkAvailable()) {
            trailerListView.setVisibility(View.GONE);
            cancel(false);
        }
    }

    @Override
    protected Integer doInBackground(Void... params) {

        try {
            result = getTrailerDataFromURL(VIDEOS_URL);
        } catch (IOException e) {
            Log.e(MainActivity.TAG, "Exception occured while fetching trailer information from The Movie DB.", e);
            e.printStackTrace();
            return -1;
        }

        return 0;
    }

    private String getTrailerDataFromURL(String urlString) throws IOException {
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

            String jsonTrailers = mapper.readValue(result, JsonNode.class).get("results").toString();
            List<Trailer> trailers = mapper.readValue(jsonTrailers, TypeFactory.defaultInstance().constructCollectionType(List.class, Trailer.class));
            movie.trailers = trailers;
            TrailerAdapter.updateCount(movie.getTrailers().size());
            trailerListView.invalidateViews();
            trailerListView.setVisibility(View.VISIBLE);
            MainActivity.setListViewHeightBasedOnItems(trailerListView);
        } catch (Exception e) {
            Log.e(MainActivity.TAG, "Exception occured while parsing JSON data.", e);
        }
    }
}
