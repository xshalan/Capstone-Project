package app.com.shalan.spacego.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Space implements Parcelable
{

    private String name;
    private String description;
    private String address;
    private Integer phone;
    private String website;
    private String city;
    private String country;
    private String imageUrl;
    private List<String> features = null;
    private Double latitude;
    private Double longitude;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    public final static Parcelable.Creator<Space> CREATOR = new Creator<Space>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Space createFromParcel(Parcel in) {
            Space instance = new Space();
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.description = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));
            instance.phone = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.website = ((String) in.readValue((String.class.getClassLoader())));
            instance.city = ((String) in.readValue((String.class.getClassLoader())));
            instance.country = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.features, (java.lang.String.class.getClassLoader()));
            instance.latitude = ((Double) in.readValue((Float.class.getClassLoader())));
            instance.longitude = ((Double) in.readValue((Float.class.getClassLoader())));
            instance.additionalProperties = ((Map<String, Object> ) in.readValue((Map.class.getClassLoader())));
            return instance;
        }

        public Space[] newArray(int size) {
            return (new Space[size]);
        }

    }
            ;

    /**
     * No args constructor for use in serialization
     *
     */
    public Space() {
    }

    /**
     *
     * @param phone
     * @param imageUrl
     * @param website
     * @param address
     * @param description
     * @param name
     * @param features
     * @param longitude
     * @param latitude
     * @param country
     * @param city
     */
    public Space(
                 String name,
                 String description,
                 String address,
                 Integer phone,
                 String website,
                 String city,
                 String country,
                 String imageUrl,
                 List<String> features,
                 Double latitude,
                 Double longitude) {
        super();
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.city = city;
        this.country = country;
        this.imageUrl = imageUrl;
        this.features = features;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(description);
        dest.writeValue(address);
        dest.writeValue(phone);
        dest.writeValue(website);
        dest.writeValue(city);
        dest.writeValue(country);
        dest.writeValue(imageUrl);
        dest.writeList(features);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(additionalProperties);
    }

    public int describeContents() {
        return 0;
    }

}