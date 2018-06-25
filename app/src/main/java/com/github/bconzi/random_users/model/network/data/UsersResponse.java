package com.github.bconzi.random_users.model.network.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Random users api response class.
 *
 * @author marlonlom
 */
public class UsersResponse implements Parcelable {

    @SerializedName("results")
    @Expose
    private List<RandomUser> results = new ArrayList<>();
    public final static Parcelable.Creator<UsersResponse> CREATOR = new Creator<UsersResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UsersResponse createFromParcel(Parcel in) {
            return new UsersResponse(in);
        }

        public UsersResponse[] newArray(int size) {
            return (new UsersResponse[size]);
        }

    };

    protected UsersResponse(Parcel in) {
        in.readList(this.results, (RandomUser.class.getClassLoader()));
    }

    public List<RandomUser> getResults() {
        return results;
    }

    public void setResults(List<RandomUser> results) {
        this.results = results;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(results);
    }

    public int describeContents() {
        return 0;
    }

}