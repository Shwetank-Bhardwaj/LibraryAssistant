package com.shwetank.libraryassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwetank.libraryassistant.glide.GlideApp;
import com.shwetank.libraryassistant.model.Art;
import com.shwetank.libraryassistant.network.NetworkManager;
import com.shwetank.libraryassistant.network.NetworkManagerImpl;

import java.util.List;

public class ArtActivity extends AppCompatActivity implements ArtData {

    private ImageView imageView;
    private TextView artName;
    private TextView artistName;
    private TextView artDescription;
    private Button audioButton;
    private AudioUtility audioUtility;
    private NetworkManager mNetworkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art);
        findAllId();
        mNetworkManager = NetworkManagerImpl.getInstance(this);
        audioUtility = AudioUtility.getInstance(getApplicationContext());
        Intent i = getIntent();
        Art art = (Art) i.getSerializableExtra("data");
        setDetails(art);
        mNetworkManager.getRecommendedArt(art, this);
    }

    public void setDetails(final Art art) {
        artName.setText(art.getArtworkName());
        artistName.setText(art.getArtistName());
        artDescription.setText(art.getArtworkInfo());
        artistName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ArtistActivity.class);
                intent.putExtra("data", art);
                startActivity(intent);
            }
        });
        GlideApp.with(getApplicationContext())
                .load(art.getArtworkImageUrl())
                .centerCrop()
                .into(imageView);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioUtility.isPlaying()) {
                    audioUtility.stopAudio();
                    audioButton.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
                } else {
                    audioUtility.startAudio(artDescription.getText().toString());
                    audioButton.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
                }
            }
        });
    }

    private void findAllId() {
        imageView = findViewById(R.id.art_image);
        artName = findViewById(R.id.art_name);
        artistName = findViewById(R.id.artist_name);
        artDescription = findViewById(R.id.des);
        audioButton = findViewById(R.id.audio);
    }

    @Override
    protected void onDestroy() {
        audioUtility.stopAudio();
        super.onDestroy();
    }

    @Override
    public void artData(List<Art> artList) {
        RecommendationFragment recommendationFragment = new RecommendationFragment();
        recommendationFragment.setData(artList, artName.getText().toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recommendedLayout, recommendationFragment);
        fragmentTransaction.commit();
    }
}
