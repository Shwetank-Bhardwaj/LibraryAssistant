package com.shwetank.libraryassistant;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.shwetank.libraryassistant.beacon.Beacon;
import com.shwetank.libraryassistant.beacon.BeaconActivity;
import com.shwetank.libraryassistant.beacon.BeaconUtility;
import com.shwetank.libraryassistant.model.Art;
import com.shwetank.libraryassistant.network.NetworkManager;
import com.shwetank.libraryassistant.network.NetworkManagerImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BeaconActivity implements ArtData {

    private RecyclerView mRecyclerView;
    private ArrayList<Art> mArtList = new ArrayList<>();
    private MainAdapter mMainAdapter;
    private NetworkManager mNetworkManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
            getUpdatedArtDataFromBeacons(getBeaconList());
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    protected void beaconDetected() {
        getUpdatedArtDataFromBeacons(getBeaconList());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetworkManager = NetworkManagerImpl.getInstance(this);
        findAllIds();
        setUpRecyclerView();
    }

    private void getUpdatedArtDataFromBeacons(List<Beacon> beaconList) {
        List<String> list = new ArrayList<>();
        for (Beacon beacon : beaconList) {
            list.add(BeaconUtility.getBeaconId(String.valueOf(beacon.getMinor())));
        }
        mNetworkManager.getBulkArtData(list, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanBleDevice(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        scanBleDevice(false);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArtList = new ArrayList<>();
        mMainAdapter = new MainAdapter(this, mArtList);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);
    }


    private void findAllIds() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    @Override
    public void artData(List<Art> artList) {
        mArtList.clear();
        mArtList.addAll(artList);
        mMainAdapter.notifyDataSetChanged();
    }

}
