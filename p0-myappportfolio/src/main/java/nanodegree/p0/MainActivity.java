package nanodegree.p0;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final String PACKAGE_P1P2 = "nanodegree.p1p2";
    public static final String PACKAGE_P3 = "com.example.sam_chordas.stockhawk";
    public static final String PACKAGE_P4 = "com.udacity.gradle.builditbigger";
    public static final String PACKAGE_P5 = "com.example.xyzreader";
    public static final String PACKAGE_P6 = "com.example.android.sunshine.app";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchPopMovies(View view) throws Exception {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PACKAGE_P1P2, PACKAGE_P1P2 + ".MainActivity"));
        startActivity(intent);
    }
    public void launchStockHawk(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(PACKAGE_P3,PACKAGE_P3 + ".ui.MyStocksActivity"));
        startActivity(intent);
    }
    public void launchBuildItBiggerFree(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(PACKAGE_P4 + ".free",PACKAGE_P4 + ".MainActivity"));
        startActivity(intent);
    }
    public void launchBuildItBiggerPaid(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(PACKAGE_P4 + ".paid",PACKAGE_P4 + ".MainActivity"));
        startActivity(intent);
    }
    public void launchXYZReader(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(PACKAGE_P5 ,PACKAGE_P5 + ".ui.ArticleListActivity"));
        startActivity(intent);
    }
    public void launchSunshine(View view) throws Exception {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(PACKAGE_P6 ,PACKAGE_P6 + ".MainActivity"));
        startActivity(intent);
    }
    public void launchCapstone(View view) throws Exception {
        String text = "This button will launch " + ((Button) view).getText();
        Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT).show();
        //TODO: launch app
    }
}
