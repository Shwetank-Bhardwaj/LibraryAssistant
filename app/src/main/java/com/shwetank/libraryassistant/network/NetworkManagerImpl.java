package com.shwetank.libraryassistant.network;

import android.content.Context;
import android.os.AsyncTask;

import com.shwetank.libraryassistant.ArtData;
import com.shwetank.libraryassistant.ArtistData;
import com.shwetank.libraryassistant.model.Art;
import com.shwetank.libraryassistant.model.ArtBeaconList;
import com.shwetank.libraryassistant.model.Artist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManagerImpl implements NetworkManager {

    private Retrofit mRetrofit;
    private String BASE_URL = "https://www.google.com";
    private static NetworkManagerImpl INSTANCE;

    private NetworkManagerImpl(Context context) {
        if (mRetrofit == null) {
            mRetrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static NetworkManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new NetworkManagerImpl(context);
        }
        return INSTANCE;
    }

    @Override
    public void getBulkArtData(List<String> beaconList, ArtData artData) {
        ArtDataAsyncTask asyncTask = new ArtDataAsyncTask(beaconList, artData);
        asyncTask.execute();
    }

    @Override
    public void getArtistInformation(String artistName, ArtistData artistData) {
        ArtistDataAsyncTask artistDataAsyncTask = new ArtistDataAsyncTask(artistName, artistData);
        artistDataAsyncTask.execute();
    }

    class ArtDataAsyncTask extends AsyncTask<Void, Void, Void> {


        private final List<String> beaconList;
        private final ArtData artData;

        public ArtDataAsyncTask(List<String> beaconList, ArtData artData) {
            this.beaconList = beaconList;
            this.artData = artData;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ArtBeaconList artBeaconList = new ArtBeaconList(beaconList);
            mRetrofit.create(GetDataService.class).getBulkArt(artBeaconList).enqueue(new Callback<List<Art>>() {
                @Override
                public void onResponse(Call<List<Art>> call, Response<List<Art>> response) {
                    sendData();
                }

                @Override
                public void onFailure(Call<List<Art>> call, Throwable t) {
                    sendData();
                }
            });
            return null;
        }

        public void sendData() {
            List<Art> list = new ArrayList<>();
            Art art1 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1984.114.2_1", "Art Name", "Artist Name", "The painting is thought by many to be a portrait of Lisa Gherardini,[4] the wife of Francesco del Giocondo, and is in oil on a white Lombardy poplar panel. It had been believed to have been painted between 1503 and 1506; however, Leonardo may have continued working on it as late as 1517. Recent academic work suggests that it would not have been started before 1513.[5][6][7][8] It was acquired by King Francis I of France and is now the property of the French Republic, on permanent display at the Louvre Museum in Paris since 1797.[9]");
            list.add(art1);
            Art art2 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-2013.74_2", "Art Name", "Artist Name", "The painting is thought by many to be a portrait of Lisa Gherardini,[4] the wife of Francesco del Giocondo, and is in oil on a white Lombardy poplar panel. It had been believed to have been painted between 1503 and 1506; however, Leonardo may have continued working on it as late as 1517. Recent academic work suggests that it would not have been started before 1513.[5][6][7][8] It was acquired by King Francis I of France and is now the property of the French Republic, on permanent display at the Louvre Museum in Paris since 1797.[9]");
            list.add(art2);
            Art art3 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1991.183_1", "Art Name", "Artist Name", "The painting is thought by many to be a portrait of Lisa Gherardini,[4] the wife of Francesco del Giocondo, and is in oil on a white Lombardy poplar panel. It had been believed to have been painted between 1503 and 1506; however, Leonardo may have continued working on it as late as 1517. Recent academic work suggests that it would not have been started before 1513.[5][6][7][8] It was acquired by King Francis I of France and is now the property of the French Republic, on permanent display at the Louvre Museum in Paris since 1797.[9]");
            list.add(art3);
            Art art4 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1991.183_1", "Art Name", "Artist Name", "The painting is thought by many to be a portrait of Lisa Gherardini,[4] the wife of Francesco del Giocondo, and is in oil on a white Lombardy poplar panel. It had been believed to have been painted between 1503 and 1506; however, Leonardo may have continued working on it as late as 1517. Recent academic work suggests that it would not have been started before 1513.[5][6][7][8] It was acquired by King Francis I of France and is now the property of the French Republic, on permanent display at the Louvre Museum in Paris since 1797.[9]");
            list.add(art4);
            artData.artData(list);
        }
    }

    class ArtistDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private String artistName;
        private final ArtistData artistData;

        public ArtistDataAsyncTask(String artistName, ArtistData artistData) {
            this.artistName = artistName;
            this.artistData = artistData;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mRetrofit.create(GetDataService.class).getArtist(artistName).enqueue(new Callback<Artist>() {
                @Override
                public void onResponse(Call<Artist> call, Response<Artist> response) {
                    sendData();
                }

                @Override
                public void onFailure(Call<Artist> call, Throwable t) {
                    sendData();
                }
            });
            return null;
        }

        public void sendData() {
            Artist artist = new Artist();
            artist.setBirthDate(new Date().toString().substring(0, 10));
            artist.setNationality("American");
            artist.setArtistDescription("Leonardo da Vinci was a Renaissance painter, sculptor, architect, inventor, military engineer and draftsman — the epitome of a true Renaissance man. Gifted with a curious mind and a brilliant intellect, da Vinci studied the laws of science and nature, which greatly informed his work. His drawings, paintings and other works have influenced countless artists and engineers over the centuries. ");
            artist.setImageUrl("https://ids.si.edu/ids/deliveryService?id=SAAM-1991.183_1");
            artist.setArtistName(artistName);
            artistData.artistData(artist);
        }
    }
}
