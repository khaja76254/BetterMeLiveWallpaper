# Better Me Live Wallpaper

An Android live wallpaper based on the generated motivational valley artwork. It changes its color/lighting treatment by time of day and displays local phone-usage feedback.

## What it does
- 06:00–11:59: warmer morning treatment
- 12:00–16:59: brighter daytime treatment
- 17:00–19:59: sunset treatment
- 20:00–05:59: darker night treatment
- Updates the wallpaper once per minute.
- Reads Android UsageStats locally to estimate today's total foreground usage and selected high-distraction apps.
- Gives blunt, short productivity suggestions on the wallpaper.
- No network permission; usage data is not uploaded.

## Android Studio
Open this folder as a project in Android Studio and let Gradle sync. Build the debug APK.

## First run
1. Install the APK.
2. Open Better Me Live Wallpaper.
3. Tap **Allow Usage Access** and enable access for Better Me Live Wallpaper.
4. Return to the app and tap **Set Live Wallpaper**.
5. Choose the live wallpaper for your lock screen/home screen as supported by your phone.

## Important
Android's UsageStats API requires special Usage Access approval in Settings; declaring the permission alone is not enough. The app therefore cannot give personalized usage feedback until you grant that access.
