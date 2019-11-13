package com.shwetank.libraryassistant;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shwetank.libraryassistant.glide.GlideApp;
import com.shwetank.libraryassistant.model.Art;

import java.util.ArrayList;
import java.util.Locale;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ArtViewHolder> {

    private final Context mContext;
    private ArrayList<Art> mArtList;

    public MainAdapter(Context context, ArrayList<Art> artList) {
        this.mContext = context;
        this.mArtList = artList;
    }

    @NonNull
    @Override
    public ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.art_layout, parent, false);
        return new ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtViewHolder holder, int position) {
        Art art = mArtList.get(position);
        holder.setDetails(mContext, art);
    }

    @Override
    public int getItemCount() {
        return mArtList.size();
    }

    class ArtViewHolder extends RecyclerView.ViewHolder {

        private TextView artName;
        private TextView artistName;
        private TextView artDes;
        private ImageView artImageView;
        private Button textToSpeech;
        private TextToSpeech t;

        public ArtViewHolder(@NonNull View itemView) {
            super(itemView);
            artName = itemView.findViewById(R.id.art_name);
            artistName = itemView.findViewById(R.id.artist_name);
            artDes = itemView.findViewById(R.id.art_des);
            artImageView = itemView.findViewById(R.id.art_image);
            textToSpeech = itemView.findViewById(R.id.button);
        }

        public void setDetails(final Context context, Art art) {
            artName.setText(art.getName());
            artistName.setText(art.getArtistName());
            artistName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext.getApplicationContext(), ArtistActivity.class);
                    intent.putExtra(mContext.getString(R.string.artist_name),artistName.getText().toString());
                    mContext.startActivity(intent);
                }
            });
            artDes.setText(art.getArtDescription());
            t = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    int i = t.setLanguage(Locale.ENGLISH);
                }
            });

            textToSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (t.isSpeaking()) {
                        t.stop();
                    } else {
                        String toSpeak = artDes.getText().toString();
                        t.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
            GlideApp.with(context)
                    .load(art.getImageUrl())
                    .fitCenter()
                    .into(artImageView);
        }
    }
}

