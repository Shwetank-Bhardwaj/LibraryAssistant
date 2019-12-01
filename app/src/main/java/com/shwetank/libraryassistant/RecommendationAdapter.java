package com.shwetank.libraryassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shwetank.libraryassistant.glide.GlideApp;
import com.shwetank.libraryassistant.model.Art;

import java.util.List;

import static android.media.CamcorderProfile.get;

class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.ArtViewHolder> {
    private Context context;
    private List<Art> artList;

    public RecommendationAdapter(Context context, List<Art> artList) {
        this.context = context;
        this.artList = artList;
    }

    @NonNull
    @Override
    public RecommendationAdapter.ArtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.art_layout, parent, false);
        return new RecommendationAdapter.ArtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.ArtViewHolder holder, int position) {
        Art art = artList.get(position);
        holder.setDetails(context, art);
    }

    @Override
    public int getItemCount() {
        return artList.size();
    }

    class ArtViewHolder extends RecyclerView.ViewHolder {

        private TextView artName;
        private TextView artistName;
        private ImageView artImageView;

        public ArtViewHolder(@NonNull View itemView) {
            super(itemView);
            artName = itemView.findViewById(R.id.art_name);
            artistName = itemView.findViewById(R.id.artist_name);
            artImageView = itemView.findViewById(R.id.art_image);
        }

        public void setDetails(final Context context, Art art) {
            artName.setText(art.getArtworkName());
            artistName.setText(art.getArtistName());
            GlideApp.with(context)
                    .load(art.getArtworkImageUrl())
                    .centerCrop()
                    .into(artImageView);
        }
    }
}

