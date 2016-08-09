package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.json.JSONArray;

import igorsteblii.com.novssql.dto.Song;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author igorsteblii on 05.08.16.
 */
public class RealmFragment extends BaseFragment {

    private Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }

    @Override
    protected void selectComplex() {

    }

    @Override
    protected void selectAll() {
        RealmResults<Song> all = realm.where(Song.class).findAll();

    }

    @Override
    protected void addFromJson() {
        JSONArray array = listener.getJsonSonArray();
        long time = System.currentTimeMillis();
        realm.beginTransaction();
        realm.createAllFromJson(Song.class, array);
        realm.commitTransaction();
        time = System.currentTimeMillis() - time;
        String s = "count : " + array.length() + ", " + "from JSON to Realm :" + time + "ms";
        timeView.setText(s);
    }

    @Override
    protected void addRandom() {

    }

}
