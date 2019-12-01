package com.shwetank.libraryassistant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shwetank.libraryassistant.model.Art;

import java.util.List;

public class RecommendationFragment extends Fragment {

    private List<Art> artList;
    private RecyclerView recyclerView;
    private RecommendationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        findAllId(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    private void findAllId(View view) {
        recyclerView = view.findViewById(R.id.recommended_recyclerview);
    }

    public void setData(List<Art> artList, String artName) {
        this.artList = artList;
        for (int i = 0; i < artList.size(); i++) {
            if(artList.get(i).getArtworkName().equals(artName)){
                artList.remove(i);
            }
        }
    }

    public void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        adapter = new RecommendationAdapter(this.getContext(), artList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);
    }
}
