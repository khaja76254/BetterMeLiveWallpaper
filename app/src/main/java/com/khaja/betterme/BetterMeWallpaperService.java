package com.khaja.betterme;

import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import java.util.Calendar;

public class BetterMeWallpaperService extends WallpaperService {
    @Override public Engine onCreateEngine(){ return new EngineImpl(); }
    private class EngineImpl extends Engine {
        private final Handler h=new Handler(); private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); private Bitmap bg; private UsageFeedback feedback; private String lastFeedback="Use the phone with intention."; private long lastCheck=0;
        private final Runnable tick=()->{ draw(); schedule(); };
        @Override public void onCreate(SurfaceHolder sh){ super.onCreate(sh); bg=BitmapFactory.decodeResource(getResources(),R.drawable.base_wallpaper); feedback=new UsageFeedback(BetterMeWallpaperService.this); }
        @Override public void onVisibilityChanged(boolean v){ if(v){draw();schedule();} else h.removeCallbacks(tick); }
        @Override public void onSurfaceChanged(SurfaceHolder sh,int f,int w,int he){ super.onSurfaceChanged(sh,f,w,he); draw(); }
        private void schedule(){ h.removeCallbacks(tick); h.postDelayed(tick,60_000); }
        private void draw(){ SurfaceHolder sh=getSurfaceHolder(); Canvas c=null; try{c=sh.lockCanvas(); if(c==null)return; int w=c.getWidth(), he=c.getHeight(); Calendar now=Calendar.getInstance(); int hour=now.get(Calendar.HOUR_OF_DAY); float t=(hour+now.get(Calendar.MINUTE)/60f)/24f;
                int overlay; if(hour>=6&&hour<12) overlay=Color.argb(25,255,190,90); else if(hour>=12&&hour<17) overlay=Color.argb(10,40,110,180); else if(hour>=17&&hour<20) overlay=Color.argb(40,255,110,35); else overlay=Color.argb(85,5,12,45);
                drawCover(c,bg,w,he); c.drawColor(overlay);
                if(System.currentTimeMillis()-lastCheck>10*60_000){ UsageFeedback.Report r=feedback.getReport(); lastFeedback=r.message; lastCheck=System.currentTimeMillis(); }
                drawText(c,w,he,lastFeedback);
            }finally{if(c!=null)sh.unlockCanvasAndPost(c);} }
        private void drawCover(Canvas c,Bitmap b,int w,int he){ float s=Math.max((float)w/b.getWidth(),(float)he/b.getHeight()); float bw=b.getWidth()*s,bh=b.getHeight()*s; RectF d=new RectF((w-bw)/2,(he-bh)/2,(w+bw)/2,(he+bh)/2); p.setAlpha(255); c.drawBitmap(b,null,d,p); }
        private void drawText(Canvas c,int w,int he,String msg){ p.setColor(Color.WHITE); p.setTextAlign(Paint.Align.CENTER); p.setTypeface(Typeface.create("sans",Typeface.BOLD)); p.setTextSize(Math.max(26,w*.055f)); c.drawText("A BETTER ME EVERYDAY",w*.5f,he*.79f,p); p.setTypeface(Typeface.create("sans",Typeface.NORMAL)); p.setTextSize(Math.max(13,w*.028f)); String[] lines=wrap(msg,34); for(int i=0;i<lines.length;i++)c.drawText(lines[i],w*.5f,he*.84f+i*Math.max(18,w*.038f),p); p.setTextSize(Math.max(12,w*.026f)); p.setColor(Color.rgb(235,190,90)); c.drawText("DISCIPLINE • PROGRESS • FREEDOM",w*.5f,he*.96f,p); }
        private String[] wrap(String s,int n){ java.util.ArrayList<String>a=new java.util.ArrayList<>(); String cur=""; for(String x:s.split(" ")){if(cur.length()+x.length()+1>n){a.add(cur);cur=x;}else cur+=(cur.isEmpty()?"":" ")+x;} if(!cur.isEmpty())a.add(cur); return a.toArray(new String[0]); }
        @Override public void onDestroy(){h.removeCallbacksAndMessages(null);super.onDestroy();}
    }
}
