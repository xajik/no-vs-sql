package igorsteblii.com.novssql;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import igorsteblii.com.novssql.dto.Song;
import igorsteblii.com.novssql.fragment.BaseFragment;
import igorsteblii.com.novssql.fragment.FirebaseFragment;
import igorsteblii.com.novssql.fragment.PreferenceFragment;
import igorsteblii.com.novssql.fragment.RealmFragment;
import igorsteblii.com.novssql.fragment.SqlLiteFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.IListener {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    private JSONArray mSongsJsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        try {
            mSongsJsonArray = loadCatalogFromAsset();
        } catch (JSONException | IOException e) {
            Snackbar.make(null, e.getMessage(), Snackbar.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_sqlite:
                fragment = new SqlLiteFragment();
                break;
            case R.id.nav_realm:
                fragment = new RealmFragment();
                break;
            case R.id.nav_firebase:
                fragment = new FirebaseFragment();
                break;
            case R.id.nav_settings:
                fragment = new PreferenceFragment();
                break;
            default:
                return false;
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(fragment.getClass().getSimpleName());
        }
        transaction.replace(R.id.container, fragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public JSONArray getJsonSonArray() {
        return mSongsJsonArray;
    }

    /**
     * Load catalog.json file from assets which contains Json enteties of {@link Song}
     */
    private JSONArray loadCatalogFromAsset() throws JSONException, IOException {
        String json;
        InputStream is = getAssets().open("catalog.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");
        JSONObject object = new JSONObject(json);
        return object.getJSONArray("catalog");
    }

    /**
     * Generates 20 random {@link Song}
     */
    @Override
    public List<Song> createRandomSong() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        List<Song> songs = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            String[] strings = randomString(3, 15);
            songs.add(new Song(random.nextInt() + year, strings[1], strings[2], random.nextLong(), random.nextInt(year)));
        }
        return songs;
    }

    private String[] randomString(int count, int length) {
        String[] strings = new String[count];
        char[] chars = ALPHABET.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int j = 0; j < count; j++) {
            for (int i = 0; i < length; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }
            strings[j] = sb.toString();
        }
        return strings;
    }

}
