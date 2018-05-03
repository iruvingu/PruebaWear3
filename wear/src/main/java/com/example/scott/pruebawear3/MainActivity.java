package com.example.scott.pruebawear3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    GoogleApiClient mGoogleApiClient = null;

    public static final String TAG = "MyDataMap...";
    public static final String WEARABLE_DATA_PATH = "/wearable/data/path";

    private ListView listView;
    private Button btnAddNewItem;

    List<String> pulsos = new ArrayList<String>();

    Random rand = new Random();

    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(Wearable.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);

        mGoogleApiClient = builder.build();

        listView = (ListView) findViewById(R.id.listView);
        btnAddNewItem = (Button) findViewById(R.id.btnNuevoItem);

        AddValorRandom();

        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,pulsos);

        listView.setAdapter(arrayAdapter);

        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarLista();
                arrayAdapter.notifyDataSetChanged();
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }

    public void AddValorRandom(){
        for (int i = 0; i < 1; i++ ){
            int  n = rand.nextInt(190 - 60) + 60;
            pulsos.add(String.valueOf(n));
        }
    }

    public void VerificarLista(){
        if (pulsos.size() < 3){
            AddValorRandom();
            Log.v("Aqui 1","Añadido a lista");
        }else {
            sendDataMapToDataLayer();
            Log.v("Aqui 2", "Enviando Datos");
            pulsos.clear();
            AddValorRandom();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //sendMessage();
    }

    private DataMap createDataMap(){
        DataMap dataMap = new DataMap();

        for (int i = 0; i < pulsos.size(); i++){
            String key = String.valueOf(i);
            dataMap.putString(key, pulsos.get(i));
            Log.v("Aqui 3", "key" + key + pulsos.get(i));
        }
        /*
        dataMap.putString("one","Pulso: 60");
        dataMap.putString("two", "Pulso: 65");
        dataMap.putString("three", "Pulso: 70");*/

        return dataMap;
    }

    private void sendDataMapToDataLayer() {
        if (mGoogleApiClient.isConnected()){
            DataMap dataMap = createDataMap();
            /*Despues de la primer condicion
            * necesitamos mandar el DataMap
            * a la capa de datos
            * Para esto necesitamos hacerlo en un nuevo hilo para asegurar que no se bloquee nuestro hilo principal*/
            Log.v("Aqui 4", "DataMAp Listo");
            new SendDataMapToDataLayer(WEARABLE_DATA_PATH,dataMap).start();

        }else {
            Log.v(TAG,"Connection is Clossed");
        }
    }

    //Crear clase para Mandar mensaje a la capa de datos

    public class SendDataMapToDataLayer extends Thread{

        String path;
        DataMap dataMap;

        public SendDataMapToDataLayer(String path, DataMap dataMap){
            this.path = path;
            this.dataMap = dataMap;
        }

        /*La primer cosa a entender es donde mandar el mensaje
        * a los dispositivos conectados
        * Si es por Bloetooth son identificados via API como nodos
        * Por lo que se necesita enlistar a los nodos usando la API Nodes*/
        @Override
        public void run() {
            //super.run();

            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(WEARABLE_DATA_PATH);
            putDataMapRequest.getDataMap().putAll(dataMap);

            PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
            DataApi.DataItemResult dataItemResult =
                    Wearable.DataApi.putDataItem(mGoogleApiClient,putDataRequest).await();

            if (dataItemResult.getStatus().isSuccess()){

                Log.v(TAG,"#########DATAITEM SUCCESSFULLY SENT!!!!!!!!#########");
            }
            else {
                Log.v(TAG,"Error while sending the Dataitem");
            }

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
