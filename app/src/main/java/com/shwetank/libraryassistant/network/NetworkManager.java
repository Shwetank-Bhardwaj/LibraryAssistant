package com.shwetank.libraryassistant.network;

import com.shwetank.libraryassistant.ArtData;
import com.shwetank.libraryassistant.ArtistData;
import com.shwetank.libraryassistant.model.Art;

import java.util.List;

public interface NetworkManager {

    void getBulkArtData(List<String> beaconList, ArtData artData);

    void getArtistInformation(String artistName, ArtistData artistData);

    void getRecommendedArt(Art art, ArtData artData);
}
