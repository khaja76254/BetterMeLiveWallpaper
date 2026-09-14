# Build the APK from your phone

This project includes a GitHub Actions workflow. After the project files are uploaded to a GitHub repository, open the **Actions** tab and run **Build Better Me APK**. The finished `app-debug.apk` will appear as a downloadable artifact on the workflow run.

The workflow uses Java 17 and Gradle 8.7, then runs `assembleDebug`.
