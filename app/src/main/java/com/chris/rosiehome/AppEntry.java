package com.chris.rosiehome;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;

final class AppEntry {
    final String label; final ComponentName component; final Drawable icon;
    AppEntry(String l, ComponentName c, Drawable i) { label=l; component=c; icon=i; }
}
