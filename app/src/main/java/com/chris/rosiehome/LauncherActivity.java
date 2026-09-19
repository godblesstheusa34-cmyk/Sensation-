package com.chris.rosiehome;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.content.pm.*;
import android.os.Bundle;
import android.provider.Settings;
import android.view.*;
import java.util.*;

public class LauncherActivity extends Activity {
    static final int PICK_WIDGET=40, CONFIG_WIDGET=41;
    private RosieView rosie; private AppWidgetHost widgetHost; private AppWidgetManager widgetManager;
    @Override public void onCreate(Bundle b) { super.onCreate(b);
        getWindow().setStatusBarColor(0x00000000); getWindow().setNavigationBarColor(0xff050505);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        widgetHost=new AppWidgetHost(this, 3010); widgetManager=AppWidgetManager.getInstance(this);
        rosie=new RosieView(this); setContentView(rosie);
    }
    List<AppEntry> apps() { List<AppEntry> out=new ArrayList<>(); Intent q=new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);
        for (ResolveInfo r:getPackageManager().queryIntentActivities(q, PackageManager.MATCH_ALL)) {
            if (r.activityInfo.packageName.equals(getPackageName())) continue;
            out.add(new AppEntry(r.loadLabel(getPackageManager()).toString(),new ComponentName(r.activityInfo.packageName,r.activityInfo.name),r.loadIcon(getPackageManager())));
        } Collections.sort(out,(a,b)->a.label.compareToIgnoreCase(b.label)); return out;
    }
    void launch(AppEntry a) { try { startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(a.component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); } catch(Exception ignored){} }
    void chooseWidget() { int id=widgetHost.allocateAppWidgetId(); Intent i=new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK); i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id); startActivityForResult(i,PICK_WIDGET); }
    @Override protected void onActivityResult(int request,int result,Intent data) { super.onActivityResult(request,result,data); if(result!=RESULT_OK||data==null)return; int id=data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,-1); if(request==PICK_WIDGET){ AppWidgetProviderInfo p=widgetManager.getAppWidgetInfo(id); if(p!=null&&p.configure!=null){ Intent i=new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).setComponent(p.configure);i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,id);startActivityForResult(i,CONFIG_WIDGET);}else addWidget(id); } else if(request==CONFIG_WIDGET)addWidget(id); }
    private void addWidget(int id){ AppWidgetProviderInfo p=widgetManager.getAppWidgetInfo(id); if(p!=null)rosie.addWidget(widgetHost.createView(this,id,p),id); }
    @Override protected void onStart(){super.onStart();widgetHost.startListening();} @Override protected void onStop(){super.onStop();widgetHost.stopListening();}
    @Override public void onBackPressed(){ if(rosie.closeLayer())return; super.onBackPressed(); }
}
