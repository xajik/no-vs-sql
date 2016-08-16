package igorsteblii.com.novssql;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author igorsteblii on 09.08.16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration
                .Builder(this)
                .name("no-vs-sql-realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.getDefaultInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}
