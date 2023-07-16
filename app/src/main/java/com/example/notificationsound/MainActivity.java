package com.example.notificationsound;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;



public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 1;
    public Button plays,select;
    public EditText time;
    private Uri selUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        time=findViewById(R.id.timperid);
        select=findViewById(R.id.select_sound);
        plays=findViewById(R.id.playsound);


        createNotificationChannel();

        //for selecting the notification sound......
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER); //It allows the user to select a notification sound
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Notification Sound");
                Uri urie = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, urie);
                startActivityForResult(intent,REQUEST_CODE);

            }
        });

        //for Playing the notification sound......its retrives the playNotificationSound method
        plays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNotifiSound();



            }
        });

    }
    //This method is used to create a notification channel for the app if running on Android Oreo  or higher.
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            selUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }
    }

    /*
    this method retrieves the entered time from the from timefield.
    It then plays the selected notification sound  and stops the notification after the specified time
     */
    public void playNotifiSound() {
        String timeString = time.getText().toString();
        int timeInSeconds = Integer.parseInt(timeString);

        if (selUri != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(this, selUri);
            ringtone.play();

            // Stop the ringtone after the specified time
            try {
                Thread.sleep(timeInSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ringtone.stop();
        }
    }



}