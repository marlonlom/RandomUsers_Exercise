package com.github.bconzi.random_users.model.network.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Random user name data class.
 *
 * @author marlonlom
 */
public class Name implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("first")
    @Expose
    private String first;
    @SerializedName("last")
    @Expose
    private String last;
    public final static Creator<Name> CREATOR = new Creator<Name>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Name createFromParcel(Parcel in) {
            return new Name(in);
        }

        public Name[] newArray(int size) {
            return (new Name[size]);
        }

    };

    Name(Parcel in) {
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.first = ((String) in.readValue((String.class.getClassLoader())));
        this.last = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(title);
        dest.writeValue(first);
        dest.writeValue(last);
    }

    public int describeContents() {
        return 0;
    }

    public String getFullName() {
        final String firstName = getFirst().substring(0, 1).toUpperCase() + getFirst().substring(1);
        final String lastName = getLast().substring(0, 1).toUpperCase() + getLast().substring(1);
        return String.format("%s %s", firstName, lastName);
    }
}