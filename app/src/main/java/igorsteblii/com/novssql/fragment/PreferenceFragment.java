package igorsteblii.com.novssql.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import igorsteblii.com.novssql.R;
import igorsteblii.com.novssql.dto.Song;
import igorsteblii.com.novssql.sql.SongDBOpenHelper;
import io.realm.Realm;

/**
 * Created by igorsteblii on 09.08.16.
 */
public class PreferenceFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preference_layout, container);
        view.findViewById(R.id.drop_sql).setOnClickListener(this);
        view.findViewById(R.id.drop_realm).setOnClickListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drop_realm:
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.delete(Song.class);
                realm.commitTransaction();
                break;
            case R.id.drop_sql:
                new SongDBOpenHelper(getActivity()).dropTable();
                break;
        }
    }

}
