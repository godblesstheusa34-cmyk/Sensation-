# Rosie Home

Rosie Home is a personal, sideloadable Android home app inspired by HTC Sense 3.0's Rosie launcher. It uses current Android APIs rather than HTC's obsolete Fusion runtime and native libraries.

## Implemented

* A real `HOME` activity that can be selected as the default launcher.
* Installed-app discovery and launch, a four-column all-apps surface, seven wraparound home pages, readable labels, and density-independent 56/64 dp icons.
* A Sense-style arcing dock, page pips, edit tray, seven-page Leap overview, persistent current page/grid/icon preferences, and cutout-aware system-bar presentation.
* Swipe-driven depth: gesture progress drives a sine-shaped sink and perspective scale, while release uses Rosie's recovered 400 ms fifth-degree easing curve. Rendering is hardware accelerated and invalidated on Android's animation clock rather than an old fixed 16 ms loop.
* A live local flip-clock/date face with an animated, always-partly-cloudy illustration. It is explicitly labeled as a static weather display rather than presenting the decorative condition as live data.
* Android's standard widget picker and `AppWidgetHost`, so selected widgets stay interactive.

## Reference basis

Development used the two supplied recovery archives extracted together under `reference/HTC-Sense3-decoded/`. In particular, the implementation follows the seven-screen/default-screen/grid findings, `PageSinkControl`'s sinusoidal depth excursion, `RingSlideAnimator`'s gesture-driven progression, and the 400 ms polynomial snap recovered in `SettingUtil`/`EaseOutCubic`. The extracted tree is ignored because the checked-in ZIP files are the canonical, byte-identical inputs; run:

```sh
mkdir -p reference
unzip HTC-Sense3-code-and-scenes.zip -d reference
unzip HTC-Sense3-textures.zip -d reference
```

## Build

```sh
gradle assembleDebug
```

The GitHub Actions workflow builds on pushes, pull requests, and manual runs and publishes `rosie-home-debug-apk`.

## Install on a phone

1. In this repository's GitHub page, open **Actions**, open the newest successful **Build Rosie Home** run, and download **rosie-home-debug-apk**.
2. Unzip it, tap `app-debug.apk`, allow installs from your browser/files app when Android asks, and install.
3. Open **Settings → Apps → Choose default apps → Home app**, then select **Rosie Home**. Press Home.

## Known limitations

This is an independent modern rendering, not HTC's proprietary Fusion renderer. The clock flips are represented visually but do not yet animate individual digit leaves; the partly-cloudy weather is intentionally decorative and has no live provider; folder creation, drag/drop shortcut placement, page reordering, widget persistence/resizing/removal, app-drawer scrolling, wallpaper selection, and exact sampled quaternion carousel tracks remain to be completed. Widget placement currently supports one hosted widget. On-device profiling on an S24 Ultra is still required before claiming sustained 120 Hz.
