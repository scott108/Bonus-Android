package com.example.scott.bonus.fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
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
import com.example.scott.bonus.utility.HttpRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;

import de.greenrobot.event.EventBus;

/**
 * Created by Scott on 15/9/30.
 */
public class NearbyStoreFragment extends Fragment {

    private String dataAPIURL = "http://opendata.epa.gov.tw/ws/Data/GPStore/?%24skip=0&%24top=1000&format=json";
    private MainActivity mainActivity;
    private Intent nearbyStoreIntent;
    private ListView storesListView;
    private StoreAdapter storeAdapter;
    private Gson gson;
    Geocoder geocoder;
    float results[] = new float[1];

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        MainActivity mainActivity = (MainActivity)activity;
        this.mainActivity = mainActivity;
        nearbyStoreIntent = new Intent(mainActivity, NearbyStoreMapActivity.class);
        gson = new Gson();
        geocoder = new Geocoder(mainActivity.getBaseContext());
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
        //new GetDataTask().execute();
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

    String[] name = {"7-11建南門市", "棉花田羅斯福門市", "全國電子台北和平東門市", "全家便利商店股份有限公司-建國店", "全家便利商店股份有限公司-敦南店", "全家便利商店股份有限公司-臺科大店", "全國電子台北和平東門市", "7-11師大門市"};
    String[] address = {"台北市大安區建國南路二段151巷6之8號", "臺北市大安區羅斯福路3段273之1", "臺北市大安區和平東路二段78號", "臺北市大安區建國南路二段１５１巷３８號１樓", "臺北市大安區敦化南路二段９８號", "臺北市大安區基隆路４段４３號地下一樓", "臺北市大安區和平東路二段78號", "台北市大安區師大路87號"};
    String[] phone = {"02-27478711#3961", "02-25791777#218", "02-22989922#5302", "02-25239588*6142", "02-26968940", "02-25239588*6142", "02-22989922#5302", "02-27478711#3961"};

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

    class GetDataTask extends AsyncTask<String, Integer, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            HttpRequest httpRequest = new HttpRequest(dataAPIURL, HttpRequest.METHOD_GET);
            System.out.println("Start...");
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(httpRequest.body());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }
        @Override
        protected void onPostExecute(JSONArray result) {
            storeFilter(result);
        }
    }

    public void storeFilter(JSONArray result) {
        for(int i = 0; i < result.length(); i++) {
            try {
                JSONObject jsonObject = result.getJSONObject(i);
                nearbyStore(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void nearbyStore(JSONObject jsonObject) throws JSONException {
        String location = jsonObject.getString("StoreAddress");
        String strOut = location.substring(0,3);
        System.out.println(strOut);

        if(strOut.equals("106")) {
            //現在緯度,現在經度,目標緯度,目標經度,
            try {

                Location.distanceBetween(25.021404, 121.535508, geocoder.getFromLocationName(location, 1).get(0).getLatitude(), geocoder.getFromLocationName(location, 1).get(0).getLongitude(), results);

                //String distance = NumberFormat.getInstance().format(results[0] / 1000);

                if(results[0] / 1000 <= 20) {
                    Store store = new Store();
                    store.setName(jsonObject.getString("StoreName"));
                    store.setAddress(jsonObject.getString("StoreAddress"));
                    store.setPhoneNum(jsonObject.getString("ContactTel"));
                    addStore(store);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

