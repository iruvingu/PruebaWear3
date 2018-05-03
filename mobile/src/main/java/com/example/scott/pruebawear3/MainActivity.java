package com.example.scott.pruebawear3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        String[] dataMap = getIntent().getStringArrayExtra("dataMap");

        //ArrayList<String> mDataMapList = getIntent().getStringArrayListExtra("dataMap");
        Log.v("Aqui MainActivity", "Array List recibida");

        //int tam = mDataMapList.size();
        //Log.v("Aqui 2", "Tamaño de lista:" + String.valueOf(tam));

        if (dataMap != null){
            /*for (int i = 0; i < tam; i++){
                mTextView.append(mDataMapList.get(i) + "\n");
            }*/
            StringBuilder builder = new StringBuilder();
            for (String s : dataMap){
                builder.append(s + "\n");
            }
            mTextView.setText(builder.toString());

        }else {
            mTextView.setText("Waiting for the DataMap...");
        }

    }
}
