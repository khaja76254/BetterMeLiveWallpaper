package com.khaja.betterme;

import android.graphics.*;
import android.graphics.drawable.*;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.util.Calendar;

public class BetterMeWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new BetterEngine();
    }

    private class BetterEngine extends Engine {

        private final Handler handler = new Handler();
        private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        private Bitmap wallpaper;
        private boolean visible;

        private final Runnable update = new Runnable() {
            @Override
            public void run() {
                draw();
                if (visible) {
                    handler.postDelayed(this, 60_000);
                }
            }
        };

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            wallpaper = BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.base_wallpaper
            );
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;

            if (visible) {
                draw();
                handler.removeCallbacks(update);
                handler.postDelayed(update, 60_000);
            } else {
                handler.removeCallbacks(update);
            }
        }

        @Override
        public void onSurfaceChanged(
                SurfaceHolder holder,
                int format,
                int width,
                int height) {

            super.onSurfaceChanged(holder, format, width, height);
            draw();
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;

            try {
                canvas = holder.lockCanvas();

                if (canvas == null || wallpaper == null) {
                    return;
                }

                float scale = Math.max(
                        (float) canvas.getWidth() / wallpaper.getWidth(),
                        (float) canvas.getHeight() / wallpaper.getHeight()
                );

                float scaledWidth = wallpaper.getWidth() * scale;
                float scaledHeight = wallpaper.getHeight() * scale;

                float left = (canvas.getWidth() - scaledWidth) / 2f;
                float top = (canvas.getHeight() - scaledHeight) / 2f;

                RectF destination = new RectF(
                        left,
                        top,
                        left + scaledWidth,
                        top + scaledHeight
                );

                canvas.drawColor(Color.BLACK);

                paint.setAlpha(255);
                canvas.drawBitmap(wallpaper, null, destination, paint);

                drawTimeLighting(canvas);
                drawMessage(canvas);

            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        private void drawTimeLighting(Canvas canvas) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            int color;
            int alpha;

            if (hour >= 6 && hour < 12) {
                // Morning
                color = Color.rgb(255, 190, 90);
                alpha = 20;
            } else if (hour >= 12 && hour < 17) {
                // Day
                color = Color.rgb(255, 255, 255);
                alpha = 8;
            } else if (hour >= 17 && hour < 20) {
                // Sunset
                color = Color.rgb(255, 110, 60);
                alpha = 28;
            } else {
                // Night
                color = Color.rgb(20, 30, 70);
                alpha = 65;
            }

            paint.setColor(color);
            paint.setAlpha(alpha);

            canvas.drawRect(
                    0,
                    0,
                    canvas.getWidth(),
                    canvas.getHeight(),
                    paint
            );

            paint.setAlpha(255);
        }

        private void drawMessage(Canvas canvas) {
            int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            String message;

            if (hour >= 6 && hour < 10) {
                message = "START STRONG";
            } else if (hour >= 10 && hour < 14) {
                message = "DO THE IMPORTANT THING";
            } else if (hour >= 14 && hour < 18) {
                message = "ONE MORE FOCUSED HOUR";
            } else if (hour >= 18 && hour < 22) {
                message = "FINISH THE DAY STRONG";
            } else {
                message = "REST. TOMORROW NEEDS YOU.";
            }

            float width = canvas.getWidth();
            float height = canvas.getHeight();

            float panelHeight = height * 0.105f;
            float panelTop = height * 0.765f;

            paint.setColor(Color.BLACK);
            paint.setAlpha(155);

            RectF panel = new RectF(
                    width * 0.055f,
                    panelTop,
                    width * 0.945f,
                    panelTop + panelHeight
            );

            canvas.drawRoundRect(
                    panel,
                    30f,
                    30f,
                    paint
            );

            paint.setAlpha(255);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(
                    Typeface.SANS_SERIF,
                    Typeface.BOLD
            ));

            float textSize = width * 0.045f;
            paint.setTextSize(textSize);

            canvas.drawText(
                    message,
                    width / 2f,
                    panelTop + panelHeight * 0.62f,
                    paint
            );

            paint.setTextSize(width * 0.026f);
            paint.setTypeface(Typeface.create(
                    Typeface.SANS_SERIF,
                    Typeface.NORMAL
            ));

            paint.setAlpha(210);

            canvas.drawText(
                    "DISCIPLINE • PROGRESS • FREEDOM",
                    width / 2f,
                    height * 0.94f,
                    paint
            );

            paint.setAlpha(255);
        }

        @Override
        public void onDestroy() {
            handler.removeCallbacksAndMessages(null);

            if (wallpaper != null && !wallpaper.isRecycled()) {
                wallpaper.recycle();
                wallpaper = null;
            }

            super.onDestroy();
        }
    }
}
