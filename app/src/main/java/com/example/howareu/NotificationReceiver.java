package com.example.howareu;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.howareu.activity.MainMenuActivity;
import com.example.howareu.activity.SplashScreenActivity;
import com.example.howareu.constant.Strings;

import java.util.Calendar;

public class NotificationReceiver extends BroadcastReceiver {
    private SharedPreferences mPrefs;
    private static final String CHANNEL_ID = "notification_channel";
    private static final String NOTIFY = "NOTIFY";
    private static final String RESET = "RESET";
    private static final int NOTIFICATION_ID = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        mPrefs= context.getSharedPreferences(Strings.PREF_NOTIF, Context.MODE_PRIVATE);

        boolean isDayDone =mPrefs.getBoolean(Strings.IS_DAY_DONE, false); // Get the login status (e.g., from shared preferences or database)

        Log.e("Broadcasted==:",isDayDone+"");
        Log.e("Broadcasted==:",intent.getAction()+"");

        if(intent.getAction() != null) {
            switch (intent.getAction()) {
                case NOTIFY:
                    if (!isDayDone) {
                        showNotification(context);
                    }
                    break;
                case RESET:
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean(Strings.IS_DAY_DONE, false);
                    editor.apply();
                    break;

                default:
                    Log.d("No Action","Default case");
                    break;
            }
        }
        setBroadcast(context);
    }


    private void showNotification(Context context) {
        createNotificationChannel(context);
        Intent intent = new Intent(context, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.lg_1)
                .setContentTitle("Do Activity")
                .setContentText("You haven't done your activities yet.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        // Add code to show the notification
        // You can use NotificationCompat.Builder or other notification methods
    }



    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Notification Channel";
            String channelDescription = "Channel for app notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            try {
                notificationManager.createNotificationChannel(channel);
            }
            catch (Exception e){
                Log.d("Error",e.toString());
            }
        }
    }


    public void setBroadcast(Context context){
        setAlarmManager(context,2008,NOTIFY, 20);
        //setAlarmManager(context,3010,"NOTIFY2", 22);
        setAlarmManager(context,4000,RESET, 0);

    }
    public void setAlarmManager(Context context,  int requestCode, String action,int hourOfDay ){

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_MUTABLE );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Set the alarm to repeat every day

        try {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        catch (Exception e){
            Log.d("Error",e.toString());
        }

    }


}
