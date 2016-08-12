package igorsteblii.com.novssql.dto;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author igorsteblii on 12.08.16.
 */
public class Date extends RealmObject {

    @PrimaryKey
    private int year;

    public Date() {
    }

    public Date(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
