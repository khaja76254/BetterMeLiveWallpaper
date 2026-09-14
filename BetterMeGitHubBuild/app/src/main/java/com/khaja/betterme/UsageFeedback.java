package com.khaja.betterme;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import java.util.*;

public class UsageFeedback {
    private final Context context;
    private final Map<String,String> distracting = new HashMap<>();
    public UsageFeedback(Context c){ context=c.getApplicationContext();
        distracting.put("com.instagram.android","Instagram");
        distracting.put("com.google.android.youtube","YouTube");
        distracting.put("com.facebook.katana","Facebook");
        distracting.put("com.twitter.android","X");
        distracting.put("com.android.chrome","Chrome");
        distracting.put("com.google.android.apps.youtube.music","YouTube Music");
    }
    public boolean hasAccess(){
        UsageStatsManager u=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        long now=System.currentTimeMillis();
        List<UsageStats> x=u.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now-60_000, now);
        return x!=null && !x.isEmpty();
    }
    public Report getReport(){
        if(!hasAccess()) return new Report(0,"Enable Usage Access once so I can give you real phone-usage feedback.");
        UsageStatsManager u=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar c=Calendar.getInstance(); c.set(Calendar.HOUR_OF_DAY,0); c.set(Calendar.MINUTE,0); c.set(Calendar.SECOND,0); c.set(Calendar.MILLISECOND,0);
        long start=c.getTimeInMillis(), now=System.currentTimeMillis();
        List<UsageStats> stats=u.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,start,now);
        long total=0, distractingMin=0; String top=""; long topMs=0;
        if(stats!=null) for(UsageStats s:stats){ long ms=Math.max(0,s.getTotalTimeInForeground()); total+=ms; String n=distracting.get(s.getPackageName()); if(n!=null)distractingMin+=ms/60000; if(ms>topMs){topMs=ms;top=n==null?s.getPackageName():n;} }
        long mins=total/60000;
        String msg;
        if(distractingMin>=120) msg="You’ve spent "+distractingMin+" min on high-distraction apps. Stop scrolling and do one 25-minute focused block.";
        else if(distractingMin>=60) msg="Distraction is already "+distractingMin+" min today. Put the phone away for the next 30 minutes.";
        else if(mins>=240) msg="You’re at "+(mins/60)+"h "+(mins%60)+"m today. Your phone is taking a serious chunk of the day. Protect one phone-free hour.";
        else if(mins>=120) msg="You’re at "+(mins/60)+"h "+(mins%60)+"m. Keep the next hour intentional—don’t open apps by reflex.";
        else msg="Usage is under control so far. Use the phone for a purpose, then put it down.";
        return new Report(mins,msg);
    }
    public static class Report { public final long totalMinutes; public final String message; Report(long m,String s){totalMinutes=m;message=s;} }
}
