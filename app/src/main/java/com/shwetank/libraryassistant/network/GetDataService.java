package com.shwetank.libraryassistant.network;

import com.shwetank.libraryassistant.model.Art;
import com.shwetank.libraryassistant.model.ArtBeaconList;
import com.shwetank.libraryassistant.model.Artist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/art")
    Call<List<Art>> getAllArt();

    @GET("/artist")
    Call<List<Artist>> getAllArtist();

    @GET("/art")
    Call<List<Art>> getBulkArt(@Query("artlist") ArtBeaconList artBeaconList);

    @GET("/artist")
    Call<Artist> getArtist(@Query("artist") String artistName);
}
