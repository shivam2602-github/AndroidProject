package com.example.myapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.models.University;
import com.example.myapp.api.ApiService;
import com.example.myapp.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForeGroundService extends Service {
    private static final long REFRESH_INTERVAL = 10000;
    private static final String NOTIFICATION_CHANNEL_ID = "com.example.myapp";
    private static final int NOTIFICATION_ID = 1;

    private Handler handler = new Handler(Looper.getMainLooper());
    private ApiService apiService;

    public ForeGroundService() {
    }

    public void onCreate() {
        super.onCreate();
        apiService = RetrofitClient.getClient().create(ApiService.class);
        startDataRefreshing();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startDataRefreshing() {
        handler.post(refreshTask);
    }

    private Runnable refreshTask = new Runnable() {
        public void run() {
            fetchDataFromAPI();
            handler.postDelayed(refreshTask, REFRESH_INTERVAL);
        }
    };

    private void fetchDataFromAPI() {
        Call<List<University>> call = apiService.getUniversityData();
        call.enqueue(new Callback<List<University>>() {
            @Override
            public void onResponse(Call<List<University>> call, Response<List<University>> response) {
                if (response.isSuccessful()) {
                    List<University> universityDataList = response.body();
                    Intent intent = new Intent(MainActivity.DATA_REFRESHED_ACTION);
                    ArrayList<University> universityDataArrayList = new ArrayList<>(universityDataList);
                    intent.putParcelableArrayListExtra("refreshedData", universityDataArrayList);
                    LocalBroadcastManager.getInstance(ForeGroundService.this).sendBroadcast(intent);
                }
            }
            public void onFailure(Call<List<University>> call, Throwable t) {
                handler.postDelayed(refreshTask, REFRESH_INTERVAL);
            }
        });
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        int notificationIcon = R.drawable.ic_launcher_foreground;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(notificationIcon)
                .setContentTitle("MyApp")
                .setContentText("Data is being refreshed")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "MyApp Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        return builder.build();
    }

}
