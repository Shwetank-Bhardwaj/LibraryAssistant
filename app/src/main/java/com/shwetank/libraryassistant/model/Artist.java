package com.shwetank.libraryassistant.model;

import com.google.gson.annotations.SerializedName;

public class Artist {
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("artistDescription")
    private String artistDescription;
    @SerializedName("birthDate")
    private String birthDate;
    @SerializedName("nationality")
    private String nationality;

    public Artist(String imageUrl, String artistName, String artistDescription, String birthDate) {
        this.imageUrl = imageUrl;
        this.artistName = artistName;
        this.artistDescription = artistDescription;
        this.birthDate = birthDate;
    }

    public Artist() {

    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtistDescription(String artistDescription) {
        this.artistDescription = artistDescription;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistDescription() {
        return artistDescription;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}