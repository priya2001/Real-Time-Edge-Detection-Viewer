# 🧠 Real-Time Edge Detection Viewer

This project is a hybrid **Android + Web** application that demonstrates real-time **edge detection** using the device camera (via OpenCV in C++) and **OpenGL ES rendering** for smooth display.
It also includes a **TypeScript web viewer** that visualizes a processed frame demo with FPS and resolution stats.

---

## 📱 Android App Overview

### 🔧 Features

* Real-time camera feed capture (Camera2 API)
* Image frame processing with **OpenCV (C++)**
* JNI bridge between Kotlin ↔ C++
* OpenGL ES renderer for efficient frame display
* Toggle between normal and edge-detected modes

### 🧩 Tech Stack

| Layer             | Technology          |
| ----------------- | ------------------- |
| UI / Camera       | Kotlin + XML        |
| Native Processing | C++ (via NDK + JNI) |
| Image Processing  | OpenCV              |
| Rendering         | OpenGL ES 2.0       |
| Build Tools       | Gradle + CMake      |

---

## 🌍 Web Demo (TypeScript)

### Files

| File            | Description                                                   |
| --------------- | ------------------------------------------------------------- |
| `src/viewer.ts` | Defines the `EdgeViewer` interface and display logic          |
| `index.html`    | HTML page that uses compiled JS to display FPS + sample frame |
| `tsconfig.json` | TypeScript compiler configuration                             |
| `package.json`  | NPM dependencies and build scripts                            |

### How It Works

* `viewer.ts` exports a global object `EdgeViewer` with an `init()` method.
* The HTML page calls:

  ```js
  EdgeViewer.init({
    imgElementId: 'frame',
    fpsEl: 'fps',
    resEl: 'res',
    demoBase64: 'data:image/png;base64,...'
  });
  ```
* A static sample image is shown, with fake FPS updates every second.

---

## 🧱 Folder Structure

```
Real-Time-Edge-Detection-Viewer/
 ┣ app/
 ┃ ┣ src/main/java/com/example/edgedetection/
 ┃ ┃ ┣ MainActivity.kt
 ┃ ┃ ┣ Camera2Helper.kt
 ┃ ┃ ┣ GLRenderer.kt
 ┃ ┃ ┗ NativeBridge.kt
 ┃ ┣ src/main/cpp/native-lib.cpp
 ┃ ┣ src/main/res/layout/activity_main.xml
 ┃ ┗ CMakeLists.txt
 ┣ web/
 ┃ ┣ src/viewer.ts
 ┃ ┣ index.html
 ┃ ┣ package.json
 ┃ ┗ tsconfig.json
 ┗ README.md
```

---

## ⚙️ Setup Guide (Android + Web)

### 1️⃣ Prerequisites

* IntelliJ IDEA / Android Studio (latest)
* JDK 17 or 21 (LTS)
* Android SDK + NDK
* OpenCV SDK for Android
* Node.js (LTS) + npm

---

### 2️⃣ Android Build Steps

1. Clone or open the project in IntelliJ / Android Studio.
2. In **File → Project Structure → SDK Location**, set:

    * Android SDK path
    * NDK path
3. Download and link **OpenCV SDK**:

   ```
   app/src/main/cpp/opencv/sdk/native/jni/
   ```
4. Sync Gradle → Build → Run ▶️ on an emulator or USB-connected device.

---

### 3️⃣ Web Viewer Setup

1. Open terminal inside the `web` folder:

   ```bash
   cd web
   npm install
   npm run build
   npm start
   ```
2. This will compile TypeScript → JavaScript and open a live server in the browser.

---

## 🧠 Understanding the Flow

```
Camera Feed (Kotlin)
      ↓
JNI Bridge (NativeBridge)
      ↓
OpenCV (C++): Edge Detection
      ↓
OpenGL ES Renderer (GLRenderer)
      ↓
Display on Screen
```

For the web demo:

```
viewer.ts (EdgeViewer)
      ↓
index.html (script)
      ↓
Displays base64 image + FPS + Resolution
```

---

## 📦 Sample Commands

| Task          | Command          |
| ------------- | ---------------- |
| Build web     | `npm run build`  |
| Start web     | `npm start`      |
| Build Android | Use IDE “Run” ▶️ |
| Clean project | `gradlew clean`  |

---

## 🧰 Troubleshooting

| Issue                       | Cause                      | Fix                                             |
| --------------------------- | -------------------------- | ----------------------------------------------- |
| `EdgeViewer is not defined` | viewer.js not loaded       | Add `<script src="dist/viewer.js">` before init |
| `URI not registered`        | XML schema warning         | Ignore or register SDK schema                   |
| Red Kotlin / C++ file       | NDK or OpenCV path missing | Set correct paths in `CMakeLists.txt`           |
| `npm ERR! enoent`           | Missing package.json       | Create using provided template                  |
| `viewer.ts` red             | Wrong file type            | Right-click → Override File Type → TypeScript   |

---

## 🏁 Credits

Developed by **Priya Gautam**
Software Engineering (R&D) Intern Assignment
Demonstrating OpenCV + OpenGL ES + TypeScript integration.

---

## 📜 License

MIT License – Free to use and modify for educational and research purposes.
