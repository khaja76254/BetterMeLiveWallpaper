package com.khaja.betterme;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 40, 40, 40);
        root.setBackgroundColor(Color.rgb(11, 18, 32));

        TextView title = new TextView(this);
        title.setText("A BETTER ME EVERYDAY");
        title.setTextColor(Color.WHITE);
        title.setTextSize(25);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView message = new TextView(this);
        message.setText("Your wallpaper changes with the time of day.");
        message.setTextColor(Color.LTGRAY);
        message.setTextSize(16);
        message.setGravity(Gravity.CENTER);
        message.setPadding(0, 30, 0, 30);
        root.addView(message);

        Button setWallpaper = new Button(this);
        setWallpaper.setText("SET LIVE WALLPAPER");

        setWallpaper.setOnClickListener(v -> {
            Intent intent = new Intent(
                    WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            );
            intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(
                            MainActivity.this,
                            BetterMeWallpaperService.class
                    )
            );
            startActivity(intent);
        });

        root.addView(setWallpaper);

        setContentView(root);
    }
}
