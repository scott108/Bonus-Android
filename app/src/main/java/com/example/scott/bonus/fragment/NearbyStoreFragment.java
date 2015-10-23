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

                storeLisViewItem.setText("\n店名 : " + store.getName() + "\n" +
                                        "地址 : " + store.getAddress() + "\n" +
                                        "電話 : " + store.getPhoneNum() + "\n");

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


        addTestStore();

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

    String[] name = {"萊爾富國際股份有限公司-台中中大店", "萊爾富國際股份有限公司-台中復興店", "全國電子股份有限公司台中市第六分公司", "全國電子股份有限公司台中市第十分公司", "松青仁和店", "遠百企業股份有限公司台中復興分公司", "7-11興學門市", "7-11興大門市"};
    String[] address = {"南區興大路300號1樓", "南區復興路二段177之1號", "南區德義里國光路170號", "南區工學里復興路一段415-4號", "南區國光路198號", "南區復興路一段359號", "南區學府路64號", "南區國光路301號"};
    String[] phone = {"02-66068688#356", "02-66068688#356", "04-23591688#206", "04-23591688#206", "02-26968940", "04-22658686", "02-27478711#3961", "02-27478711#3961"};

    public void addTestStore() {
        for(int i = 0; i < 8; i++) {
            Store store = new Store();
            store.setName(name[i]);
            store.setAddress(address[i]);
            store.setPhoneNum(phone[i]);
            addStore(store);
        }
    }

    public void addStore(Store store) {
        storeAdapter.addStore(store);
        storeAdapter.notifyDataSetChanged();
    }
}

