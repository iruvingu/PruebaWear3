package com.example.scott.pruebawear3;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 02/05/2018.
 */

public class MyListenerService extends WearableListenerService {

    public static final String TAG = "MyDataMap...";
    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

        DataMap dataMap;

        for (DataEvent dataEvent : dataEventBuffer){

            if (dataEvent.getType() == DataEvent.TYPE_CHANGED){

                String path = dataEvent.getDataItem().getUri().getPath();

                if (path.equalsIgnoreCase(WEARABLE_DATA_PATH)){
                    dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                    Log.v(TAG,"DataMap Received on Wearable" + dataMap);

                    Intent mainActivity_intent = new Intent(this, MainActivity.class);
                    mainActivity_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mainActivity_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //List<String> mDataMapList = new ArrayList<String>();

                    String[] mDataMapaArray = new String[3];
                    Log.v(TAG,"DataMapArray instanciado con 4 elementos");

                    mDataMapaArray[0] = dataMap.getString("0");
                    Log.v(TAG,"DataMapArray 1" + dataMap.getString("0") );
                    mDataMapaArray[1] = dataMap.getString("1");
                    Log.v(TAG,"DataMapArray 2" + dataMap.getString("1"));
                    mDataMapaArray[2] = dataMap.getString("2");
                    Log.v(TAG,"DataMapArray 3" + dataMap.getString("0"));


                    //int tamDatamap = dataMap.size();

                    /*for (int i = 0; i < tamDatamap; i++){

                        String key = String.valueOf(i);
                        mDataMapList.add(dataMap.getString(key));

                    }*/



                    mainActivity_intent.putExtra("dataMap", mDataMapaArray);
                    //mainActivity_intent.putExtra("dataMap", (Parcelable) mDataMapList);
                    startActivity(mainActivity_intent);
                }
            }
        }
    }

}
