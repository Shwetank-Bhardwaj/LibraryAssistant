package com.shwetank.libraryassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwetank.libraryassistant.glide.GlideApp;
import com.shwetank.libraryassistant.model.Artist;
import com.shwetank.libraryassistant.network.NetworkManager;
import com.shwetank.libraryassistant.network.NetworkManagerImpl;

public class ArtistActivity extends AppCompatActivity implements ArtistData{

    ImageView mArtistImageView;
    TextView mArtistNameTV;
    TextView mBirthDateTV;
    TextView mArtistDescriptionTV;
    TextView mNationalityTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_profile_layout);
        Intent intent = getIntent();
        String artistName = intent.getStringExtra(getApplicationContext().getString(R.string.artist_name));
        findAllIds();
        mArtistNameTV.setText(artistName);
        NetworkManager networkManager = NetworkManagerImpl.getInstance(this);
        networkManager.getArtistInformation(artistName, this);
    }

    private void findAllIds() {
        mArtistImageView = findViewById(R.id.artist_image);
        mArtistDescriptionTV = findViewById(R.id.artist_description);
        mArtistNameTV = findViewById(R.id.artist_name);
        mBirthDateTV = findViewById(R.id.birth_date);
        mNationalityTV = findViewById(R.id.nationality_name);
    }

    @Override
    public void artistData(Artist artist) {
        GlideApp.with(this)
                .load(artist.getImageUrl())
                .into(mArtistImageView);
        mBirthDateTV.setText(artist.getBirthDate());
        mNationalityTV.setText(artist.getNationality());
        mArtistDescriptionTV.setText(artist.getArtistDescription());
    }

}
