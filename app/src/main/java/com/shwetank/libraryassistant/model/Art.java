package com.shwetank.libraryassistant.model;


import java.io.Serializable;

public class Art implements Serializable {

    private String artistDeathDateLabel;
    private String artistImageUrl;
    private String artworkImageUrl;
    private String artworkMedium;
    private String artworkInfo;
    private String artistBirthDateLabel;
    private String artistName;
    private String artworkType;
    private String artistBio;
    private String artworkClassification;
    private String currentKeeper;
    private String artworkName;
    private String artistNationality;

    public Art() {

    }

    public String getArtistDeathDateLabel() {
        return artistDeathDateLabel;
    }

    public String getArtistImageUrl() {
        return artistImageUrl;
    }

    public String getArtworkImageUrl() {
        return artworkImageUrl;
    }

    public String getArtworkMedium() {
        return artworkMedium;
    }

    public String getArtworkInfo() {
        return artworkInfo;
    }

    public String getArtistBirthDateLabel() {
        return artistBirthDateLabel;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtworkType() {
        return artworkType;
    }

    public String getArtistBio() {
        return artistBio;
    }

    public String getArtworkClassification() {
        return artworkClassification;
    }

    public String getCurrentKeeper() {
        return currentKeeper;
    }

    public String getArtworkName() {
        return artworkName;
    }

    public String getArtistNationality() {
        return artistNationality;
    }

    public void setArtistDeathDate(String artistdeathdatelabel) {
        artistDeathDateLabel = artistdeathdatelabel;
    }

    public void setArtistImageUrl(String artistImageUrl) {

        this.artistImageUrl = artistImageUrl;
    }

    public void setArtWorkImageUrl(String artworkImageUrl) {

        this.artworkImageUrl = artworkImageUrl;
    }

    public void setArtWorkMedium(String artworkMedium) {

        this.artworkMedium = artworkMedium;
    }

    public void setArtWorkDescription(String artworkInfo) {
        this.artworkInfo = artworkInfo;
    }

    public void setArtistBirthDate(String artistBirthDateLabel) {
        this.artistBirthDateLabel = artistBirthDateLabel;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public void setArtWorkType(String artworkType) {
        this.artworkType = artworkType;
    }

    public void setArtistBio(String artistBio) {
        this.artistBio = artistBio;
    }

    public void setArtWorkClassification(String artworkClassification) {
        this.artworkClassification = artworkClassification;
    }

    public void setCurrentKeeper(String currentKeeper) {
        this.currentKeeper = currentKeeper;
    }

    public void setArtWorkName(String artworkName) {
        this.artworkName = artworkName;
    }

    public void setArtistNationality(String artistNationality) {
        this.artistNationality = artistNationality;
    }
}

