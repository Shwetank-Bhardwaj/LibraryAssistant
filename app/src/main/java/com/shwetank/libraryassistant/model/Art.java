package com.shwetank.libraryassistant.model;

import com.google.gson.annotations.SerializedName;

public class Art {
    @SerializedName("imageUrl")
    private String image_url;
    @SerializedName("artName")
    private String name;
    @SerializedName("artistName")
    private String artistName;
    @SerializedName("artDescription")
    private String artDescription;

    public Art(String image_url, String name, String artistName, String artDescription) {
        this.image_url = image_url;
        this.name = name;
        this.artistName = artistName;
        this.artDescription = artDescription;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getName() {
        return name;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtDescription() {
        return artDescription;
    }

}
