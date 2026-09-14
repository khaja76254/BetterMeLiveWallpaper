package com.khaja.betterme;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView usage;
    private TextView suggestion;
    private final UsageFeedback feedback = new UsageFeedback(this);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        buildUi();
        refresh();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(36, 50, 36, 32);
        root.setBackgroundColor(Color.rgb(11,18,32));

        TextView title = new TextView(this);
        title.setText("A BETTER ME EVERYDAY");
        title.setTextColor(Color.WHITE); title.setTextSize(26); title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView sub = new TextView(this);
        sub.setText("Time-aware wallpaper + honest phone-usage feedback");
        sub.setTextColor(Color.LTGRAY); sub.setTextSize(14); sub.setGravity(Gravity.CENTER);
        root.addView(sub, new LinearLayout.LayoutParams(-1, -2));

        usage = card("Today’s phone usage\nChecking…");
        root.addView(usage);
        suggestion = card("Suggestion\nChecking…");
        root.addView(suggestion);

        Button usageAccess = new Button(this); usageAccess.setText("Allow Usage Access");
        usageAccess.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
        root.addView(usageAccess, params());

        Button setWallpaper = new Button(this); setWallpaper.setText("Set Live Wallpaper");
        setWallpaper.setOnClickListener(v -> {
            Intent i = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, BetterMeWallpaperService.class));
            startActivity(i);
        });
        root.addView(setWallpaper, params());

        TextView privacy = new TextView(this);
        privacy.setText("Privacy: usage data stays on this phone. Nothing is uploaded by this app.");
        privacy.setTextColor(Color.GRAY); privacy.setTextSize(12); privacy.setPadding(0,24,0,0);
        root.addView(privacy);
        setContentView(root);
    }

    private TextView card(String s) {
        TextView t = new TextView(this); t.setText(s); t.setTextColor(Color.WHITE); t.setTextSize(16);
        t.setPadding(24,24,24,24); t.setBackgroundColor(Color.rgb(24,32,48));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,24,0,0); t.setLayoutParams(p); return t;
    }
    private LinearLayout.LayoutParams params(){ LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,14,0,0); return p; }
    private void refresh(){ UsageFeedback.Report r=feedback.getReport(); usage.setText(String.format(Locale.US,"Today’s phone usage\n%.1f hours total",r.totalMinutes/60f)); suggestion.setText("Suggestion\n"+r.message); }
    @Override protected void onResume(){ super.onResume(); if(usage!=null) refresh(); }
}
