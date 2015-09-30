package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scott.bonus.LoginActivity;
import com.example.scott.bonus.MainActivity;
import com.example.scott.bonus.NearbyStoreMapActivity;
import com.example.scott.bonus.R;
import com.example.scott.bonus.fragmentcontrol.storesAdapter.Store;
import com.example.scott.bonus.fragmentcontrol.storesAdapter.StoreAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/9/30.
 */
public class NearbyStoreFragment extends Fragment {

    private MainActivity mainActivity;
    private Intent nearbyStoreIntent;
    private ListView storesListView;
    private StoreAdapter storeAdapter;
    private Gson gson;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
        nearbyStoreIntent = new Intent(mainActivity, NearbyStoreMapActivity.class);
        gson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_nearbystore, container, false);
        storesListView = (ListView) layout.findViewById(R.id.storesListView);
        storeAdapter = new StoreAdapter() {
            @Override
            protected View getItemView(Store store, int index, View convertView, ViewGroup parent) {
                LinearLayout layout;

                layout = (LinearLayout) mainActivity.getLayoutInflater().inflate(R.layout.store_list_view_item, null);
                TextView storeLisViewItem = (TextView) layout.findViewById(R.id.storeTextView);

                storeLisViewItem.setText("店名 : " + store.getName() + "\n" +
                                        "地址 : " + store.getAddress() + "\n" +
                                        "電話 : " + store.getPhoneNum());

                return layout;
            }
        };

        storesListView.setAdapter(storeAdapter);

        storesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View view, int i, long l) {



                Store storeChose = (Store) storeAdapter.getItem(i);
                String storeObject = gson.toJson(storeChose);

                Bundle b = new Bundle();
                b.putString("store", storeObject);
                nearbyStoreIntent.putExtras(b);

                mainActivity.startActivity(nearbyStoreIntent);

                System.out.println(i);
            }
        });


        for(int i = 0 ; i < 10; i++) {
            Store store = new Store();
            store.setName("FamilyMart");
            store.setAddress("台中市南區國光路250號");
            store.setPhoneNum("1234567");

            addStore(store);
        }


        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void addStore(Store store) {
        storeAdapter.addStore(store);
        storeAdapter.notifyDataSetChanged();
    }
}

