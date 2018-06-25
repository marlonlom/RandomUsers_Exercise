package com.github.bconzi.random_users.model.network.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Random user main data class.
 *
 * @author marlonlom
 */
public class RandomUser implements Parcelable {

    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("dob")
    @Expose
    private Dob dob;
    @SerializedName("picture")
    @Expose
    private Picture picture;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("cell")
    @Expose
    private String cell;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    public final static Parcelable.Creator<RandomUser> CREATOR = new Creator<RandomUser>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RandomUser createFromParcel(Parcel in) {
            return new RandomUser(in);
        }

        public RandomUser[] newArray(int size) {
            return (new RandomUser[size]);
        }

    };

    RandomUser(Parcel in) {
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((Name) in.readValue((Name.class.getClassLoader())));
        this.location = ((Location) in.readValue((Location.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.dob = ((Dob) in.readValue((Dob.class.getClassLoader())));
        this.picture = ((Picture) in.readValue((Picture.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.cell = ((String) in.readValue((String.class.getClassLoader())));
        this.nationality = ((String) in.readValue((String.class.getClassLoader())));
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Dob getDob() {
        return dob;
    }

    public void setDob(Dob dob) {
        this.dob = dob;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(gender);
        dest.writeValue(name);
        dest.writeValue(location);
        dest.writeValue(email);
        dest.writeValue(dob);
        dest.writeValue(picture);
        dest.writeValue(phone);
        dest.writeValue(cell);
        dest.writeValue(nationality);
    }

    public int describeContents() {
        return 0;
    }


}
