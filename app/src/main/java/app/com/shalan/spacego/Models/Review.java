package app.com.shalan.spacego.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noura on 13/08/2017.
 */

public class Review implements Parcelable {

    private String userId;
    private String review;
    private String username;
    private float rating;
    private Long date;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<Review> CREATOR = new Creator<Review>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Review createFromParcel(Parcel in) {
            Review instance = new Review();
            instance.userId = ((String) in.readValue((String.class.getClassLoader())));
            instance.username = ((String) in.readValue((String.class.getClassLoader())));
            instance.review = ((String) in.readValue((String.class.getClassLoader())));
            instance.rating = ((float) in.readValue((Float.class.getClassLoader())));
            instance.date = ((Long) in.readValue((Long.class.getClassLoader())));
            instance.additionalProperties = ((Map<String, Object>) in.readValue((Map.class.getClassLoader())));
            return instance;
        }

        public Review[] newArray(int size) {
            return (new Review[size]);
        }

    };


    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(username);
        dest.writeValue(review);
        dest.writeValue(rating);
        dest.writeValue(date);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}