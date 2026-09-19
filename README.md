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

### Why the APK is small

The APK deliberately does not contain copies of every installed application or
the 91 MB research archive. Android supplies each installed app's label and icon
at runtime, and the original HTC Java/DEX/native files cannot execute on a
modern 64-bit Samsung phone. The runtime package contains the launcher code and
text-based Android vector recreations of Rosie's phone, app-drawer, and
personalize controls. The supplied binary archives remain reference inputs and
are not duplicated into the APK.

The archives do contain HTC's original binaries. They are intentionally not
packaged as Android runtime libraries: the recovered Fusion `.so` files are
32-bit ARM (`ELFCLASS32`, `EM_ARM`) binaries tied to HTC's obsolete framework,
whereas the Galaxy S24 Ultra uses a modern 64-bit Android userspace. Including
them under `jniLibs` would increase the APK while risking an incompatible-ABI
installation failure; it would not enable the original renderer. Their decoded
scene tracks and behavior are used as implementation references instead.

## Install on a phone

1. In this repository's GitHub page, open **Actions**, open the newest successful **Build Rosie Home** run, and download **rosie-home-debug-apk**.
2. Uninstall an older Rosie Home debug build first (GitHub debug builds can have
   different test signatures). Unzip the artifact, tap `app-debug.apk`, allow
   installs from your browser/files app when Android asks, and install.
3. Open **Settings → Apps → Choose default apps → Home app**, then select **Rosie Home**. Press Home.

If Android returns to One UI, open **Settings → Apps → Rosie Home**, confirm the
installed version is **1.1**, then choose Rosie Home again under **Home app**.
Version 1.1 loads the installed-app catalog away from the UI thread so a large
catalog cannot stall Home at startup, tolerates broken third-party icon
providers, and always populates the center/default screen first.

## Known limitations

This is an independent modern rendering, not HTC's proprietary Fusion renderer. The clock flips are represented visually but do not yet animate individual digit leaves; the partly-cloudy weather is intentionally decorative and has no live provider; folder creation, drag/drop shortcut placement, page reordering, widget persistence/resizing/removal, app-drawer scrolling, wallpaper selection, and exact sampled quaternion carousel tracks remain to be completed. Widget placement currently supports one hosted widget. On-device profiling on an S24 Ultra is still required before claiming sustained 120 Hz.
