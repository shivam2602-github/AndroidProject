package com.example.myapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapp.adapter.UniversityAdapter;
import com.example.myapp.models.University;
import com.example.myapp.services.ForeGroundService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String DATA_REFRESHED_ACTION = "com.example.myapp.DATA_REFRESHED";
    private RecyclerView recyclerView;
    private UniversityAdapter universityAdapter;
    private WebView webView;

    private BroadcastReceiver dataRefreshedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DATA_REFRESHED_ACTION)) {
                ArrayList<University> refreshedData = intent.getParcelableArrayListExtra("refreshedData");
                if (refreshedData != null) {
                    universityAdapter.setData(refreshedData);
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        List<University> dataList = new ArrayList<>();

        universityAdapter = new UniversityAdapter(this, dataList,webView);
        recyclerView.setAdapter(universityAdapter);

        IntentFilter filter = new IntentFilter(DATA_REFRESHED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(dataRefreshedReceiver, filter);

        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }

    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}