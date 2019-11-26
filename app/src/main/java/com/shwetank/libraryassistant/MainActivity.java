package com.shwetank.libraryassistant;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.shwetank.libraryassistant.beacon.Beacon;
import com.shwetank.libraryassistant.beacon.BeaconActivity;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNetworkManager = NetworkManagerImpl.getInstance(this);
        findAllIds();
        setUpRecyclerView();
    }

    private void getUpdatedArtDataFromBeacons(List<Beacon> beaconList) {
        List<String> list = new ArrayList<>();
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
        createListData();
    }

    private void createListData() {
        Art art = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1906.9.11_1", "Independence (Squire Jack Porter)", "Mayer", "Jack Porter, a veteran of the War of 1812 who made a handsome living from Pennsylvania's coal mines, is surrounded by handmade objects, including a corncob pipe, a roughly-hewn wooden bench, and his wife's knitting. As a self-sufficient landholder and businessman, \"Squire Jack\" embodied an independent and enduring spirit that, by the 1850s, had become an American ideal, celebrated by painters and writers alike. The squire takes his ease on the porch of a substantial home, dressed in a flowered vest, black cravat, and polished boots that signal the rewards of his hard work.<p>Exhibition Label, Smithsonian American Art Museum, 2006");
        Art art1 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1906.9.11_1", "Independence (Squire Jack Porter)", "Mayer", "Jack Porter, a veteran of the War of 1812 who made a handsome living from Pennsylvania's coal mines, is surrounded by handmade objects, including a corncob pipe, a roughly-hewn wooden bench, and his wife's knitting. As a self-sufficient landholder and businessman, \"Squire Jack\" embodied an independent and enduring spirit that, by the 1850s, had become an American ideal, celebrated by painters and writers alike. The squire takes his ease on the porch of a substantial home, dressed in a flowered vest, black cravat, and polished boots that signal the rewards of his hard work.<p>Exhibition Label, Smithsonian American Art Museum, 2006");
        Art art2 = new Art("https://ids.si.edu/ids/deliveryService?id=SAAM-1906.9.11_1", "Independence (Squire Jack Porter)", "Mayer", "Jack Porter, a veteran of the War of 1812 who made a handsome living from Pennsylvania's coal mines, is surrounded by handmade objects, including a corncob pipe, a roughly-hewn wooden bench, and his wife's knitting. As a self-sufficient landholder and businessman, \"Squire Jack\" embodied an independent and enduring spirit that, by the 1850s, had become an American ideal, celebrated by painters and writers alike. The squire takes his ease on the porch of a substantial home, dressed in a flowered vest, black cravat, and polished boots that signal the rewards of his hard work.<p>Exhibition Label, Smithsonian American Art Museum, 2006");
        mArtList.add(art);
        mArtList.add(art1);
        mArtList.add(art2);
        mMainAdapter.notifyDataSetChanged();
    }

    private void findAllIds() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);
        mSwipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    @Override
    public void artData(List<Art> artList) {
        mArtList.addAll(artList);
        mMainAdapter.notifyDataSetChanged();
    }

}
