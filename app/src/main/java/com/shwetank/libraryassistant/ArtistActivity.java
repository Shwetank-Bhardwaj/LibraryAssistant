package com.shwetank.libraryassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shwetank.libraryassistant.glide.GlideApp;
import com.shwetank.libraryassistant.model.Art;

public class ArtistActivity extends AppCompatActivity{

    ImageView mArtistImageView;
    TextView mArtistNameTV;
    TextView mBirthDate;
    TextView mDeathDateTV;
    TextView mArtistDescriptionTV;
    TextView mNationalityTV;
    Button audio;
    private AudioUtility audioUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_profile_layout);
        audioUtility = AudioUtility.getInstance(getApplicationContext());
        Intent intent = getIntent();
        Art art = (Art)intent.getSerializableExtra("data");
        findAllIds();
        setDetail(art);
    }

    private void setDetail(Art art) {
        mArtistNameTV.setText(art.getArtistName());
        mArtistDescriptionTV.setText(art.getArtistBio());
        GlideApp.with(getApplicationContext())
                .load(art.getArtistImageUrl())
                .fitCenter()
                .into(mArtistImageView);
        mBirthDate.setText(art.getArtistBirthDateLabel());
        if(art.getArtistDeathDateLabel() == null){
            mDeathDateTV.setText("- " + getResources().getString(R.string.Present));
        }else{
            mDeathDateTV.setText("- " + art.getArtistDeathDateLabel());
        }
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioUtility.isPlaying()) {
                    audioUtility.stopAudio();
                    audio.setBackgroundResource(R.drawable.ic_volume_up_black_24dp);
                } else {
                    audioUtility.startAudio(mArtistDescriptionTV.getText().toString());
                    audio.setBackgroundResource(R.drawable.ic_volume_off_black_24dp);
                }
            }
        });
    }

    private void findAllIds() {
        mArtistImageView = findViewById(R.id.artist_image);
        mArtistDescriptionTV = findViewById(R.id.artist_description);
        mArtistDescriptionTV.setMovementMethod(new ScrollingMovementMethod());
        mArtistNameTV = findViewById(R.id.artist_name);
        mDeathDateTV = findViewById(R.id.death_date);
        mBirthDate = findViewById(R.id.born_in);
        mNationalityTV = findViewById(R.id.nationality_name);
        audio = findViewById(R.id.audio);
    }

    @Override
    protected void onDestroy() {
        audioUtility.stopAudio();
        super.onDestroy();
    }
}
