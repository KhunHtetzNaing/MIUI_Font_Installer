package com.htetznaing.mifontinstaller;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class install extends Activity implements B4AActivity{
	public static install mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mifontinstaller", "com.htetznaing.mifontinstaller.install");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (install).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mifontinstaller", "com.htetznaing.mifontinstaller.install");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mifontinstaller.install", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (install) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (install) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return install.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (install) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (install) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _ad1 = null;
public static anywheresoftware.b4a.objects.Timer _ad2 = null;
public static anywheresoftware.b4a.phone.Phone.ContentChooser _cc = null;
public static anywheresoftware.b4a.objects.Timer _t = null;
public com.AB.ABZipUnzip.ABZipUnzip _zip = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b2 = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _b = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _i = null;
public MLfiles.Fileslib.MLfiles _ml = null;
public static String _ss = "";
public anywheresoftware.b4a.objects.collections.List _filelist = null;
public static int _n = 0;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public com.htetznaing.mifontinstaller.main _main = null;
public com.htetznaing.mifontinstaller.about _about = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
int _h = 0;
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 28;BA.debugLine="lb.Initialize(\"\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 29;BA.debugLine="lb.Text = \"[Choose Font/Theme File .ttf Or .mtz]\"";
mostCurrent._lb.setText((Object)("[Choose Font/Theme File .ttf Or .mtz]"));
 //BA.debugLineNum = 30;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 31;BA.debugLine="Activity.AddMenuItem3(\"Share App\",\"share\",LoadBit";
mostCurrent._activity.AddMenuItem3((java.lang.CharSequence)("Share App"),"share",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 32;BA.debugLine="Activity.Title = \"Install Custom Font\"";
mostCurrent._activity.setTitle((Object)("Install Custom Font"));
 //BA.debugLineNum = 33;BA.debugLine="b1.Initialize(\"b1\")";
mostCurrent._b1.Initialize(mostCurrent.activityBA,"b1");
 //BA.debugLineNum = 34;BA.debugLine="b1.Text = \"Pick Font File\"";
mostCurrent._b1.setText((Object)("Pick Font File"));
 //BA.debugLineNum = 35;BA.debugLine="Activity.AddView(b1,20%x,20%y,60%x,10%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (20),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 37;BA.debugLine="Activity.AddView(lb,0%x,b1.Top+b1.Height,100%x,5%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (mostCurrent._b1.getTop()+mostCurrent._b1.getHeight()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (5),mostCurrent.activityBA));
 //BA.debugLineNum = 38;BA.debugLine="b2.Initialize(\"b2\")";
mostCurrent._b2.Initialize(mostCurrent.activityBA,"b2");
 //BA.debugLineNum = 39;BA.debugLine="b2.Text = \"Change Font\"";
mostCurrent._b2.setText((Object)("Change Font"));
 //BA.debugLineNum = 40;BA.debugLine="Activity.AddView(b2,20%x,lb.Top+lb.Height+1%y,60%";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) (mostCurrent._lb.getTop()+mostCurrent._lb.getHeight()+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 42;BA.debugLine="b.Initialize2(\"b\",\"ca-app-pub-4173348573252986/71";
mostCurrent._b.Initialize2(mostCurrent.activityBA,"b","ca-app-pub-4173348573252986/7159416950",mostCurrent._b.SIZE_SMART_BANNER);
 //BA.debugLineNum = 43;BA.debugLine="Dim h As Int";
_h = 0;
 //BA.debugLineNum = 44;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 45;BA.debugLine="If 100%x > 100%y Then h = 32dip Else h = 50dip";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 47;BA.debugLine="h = 90dip";
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 49;BA.debugLine="Activity.AddView(b,0dip,100%y - h,100%x,h)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_h),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_h);
 //BA.debugLineNum = 50;BA.debugLine="b.LoadAd";
mostCurrent._b.LoadAd();
 //BA.debugLineNum = 52;BA.debugLine="i.Initialize(\"i\",\"ca-app-pub-4173348573252986/111";
mostCurrent._i.Initialize(mostCurrent.activityBA,"i","ca-app-pub-4173348573252986/1112883353");
 //BA.debugLineNum = 53;BA.debugLine="i.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 55;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 56;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 58;BA.debugLine="ad2.Initialize(\"ad2\",60000)";
_ad2.Initialize(processBA,"ad2",(long) (60000));
 //BA.debugLineNum = 59;BA.debugLine="ad2.Enabled = True";
_ad2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 61;BA.debugLine="cc.Initialize(\"cc\")";
_cc.Initialize("cc");
 //BA.debugLineNum = 63;BA.debugLine="t.Initialize(\"t\",3000)";
_t.Initialize(processBA,"t",(long) (3000));
 //BA.debugLineNum = 64;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 171;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 173;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 167;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 169;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 158;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 159;BA.debugLine="If i.Ready Then i.Show Else i.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 160;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="End Sub";
return "";
}
public static String  _ad2_tick() throws Exception{
 //BA.debugLineNum = 163;BA.debugLine="Sub ad2_Tick";
 //BA.debugLineNum = 164;BA.debugLine="If i.Ready Then i.Show Else i.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 165;BA.debugLine="End Sub";
return "";
}
public static String  _b1_click() throws Exception{
 //BA.debugLineNum = 67;BA.debugLine="Sub b1_Click";
 //BA.debugLineNum = 68;BA.debugLine="cc.Show(\"*/*\",\"Choose Your Font\")";
_cc.Show(processBA,"*/*","Choose Your Font");
 //BA.debugLineNum = 69;BA.debugLine="End Sub";
return "";
}
public static String  _b2_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _ii = null;
 //BA.debugLineNum = 145;BA.debugLine="Sub b2_Click";
 //BA.debugLineNum = 146;BA.debugLine="Dim ii As Intent";
_ii = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 147;BA.debugLine="ii.Initialize(ii.Action_Main,\"\")";
_ii.Initialize(_ii.ACTION_MAIN,"");
 //BA.debugLineNum = 148;BA.debugLine="ii.SetComponent(\"com.android.settings/com.android";
_ii.SetComponent("com.android.settings/com.android.settings.Settings$FontSettingsActivity");
 //BA.debugLineNum = 149;BA.debugLine="Try";
try { //BA.debugLineNum = 150;BA.debugLine="StartActivity(ii)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_ii.getObject()));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 154;BA.debugLine="Msgbox(\"Missing Font.\"&CRLF&\"(or)\"&CRLF&\"Your Ph";
anywheresoftware.b4a.keywords.Common.Msgbox("Missing Font."+anywheresoftware.b4a.keywords.Common.CRLF+"(or)"+anywheresoftware.b4a.keywords.Common.CRLF+"Your Phone Is Not Xiaomi.","Error",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 156;BA.debugLine="End Sub";
return "";
}
public static String  _cc_result(boolean _success,String _dir,String _filename) throws Exception{
 //BA.debugLineNum = 79;BA.debugLine="Sub cc_Result (Success As Boolean, Dir As String,";
 //BA.debugLineNum = 80;BA.debugLine="Log(Dir & FileName)";
anywheresoftware.b4a.keywords.Common.Log(_dir+_filename);
 //BA.debugLineNum = 81;BA.debugLine="If Success Then";
if (_success) { 
 //BA.debugLineNum = 82;BA.debugLine="If FileName.EndsWith(\".ttf\") = True Then";
if (_filename.endsWith(".ttf")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 83;BA.debugLine="File.Copy(File.DirAssets,\"Fuck\",File.DirRootExt";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Fuck",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"d.zip");
 //BA.debugLineNum = 84;BA.debugLine="zip.ABUnzip(File.DirRootExternal & \"/d.zip\",Fil";
mostCurrent._zip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/d.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz");
 //BA.debugLineNum = 85;BA.debugLine="File.Delete(File.DirRootExternal,\"d.zip\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"d.zip");
 //BA.debugLineNum = 86;BA.debugLine="File.Delete(File.DirRootExternal & \"/.Htetz/.da";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc");
 //BA.debugLineNum = 87;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal &";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc");
 //BA.debugLineNum = 88;BA.debugLine="zip.ABZipDirectory(File.DirRootExternal & \"/.Ht";
mostCurrent._zip.ABZipDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MiFont.zip");
 //BA.debugLineNum = 90;BA.debugLine="ml.mkdir (\"/sdcard/MIUI/theme\")";
mostCurrent._ml.mkdir("/sdcard/MIUI/theme");
 //BA.debugLineNum = 91;BA.debugLine="Log(zip.ABUnzip(File.DirRootExternal & \"/MiFont";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._zip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MiFont.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme")));
 //BA.debugLineNum = 92;BA.debugLine="Log(File.ListFiles(File.DirrootExternal & \"/MIU";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.File.ListFiles(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme")));
 //BA.debugLineNum = 93;BA.debugLine="ml.RootCmd(\"dd if=\"&File.DirrootExternal &\"/MIU";
mostCurrent._ml.RootCmd("dd if="+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme/.data of="+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 94;BA.debugLine="ml.mkdir (\"/sdcard/MIUI/theme\")";
mostCurrent._ml.mkdir("/sdcard/MIUI/theme");
 //BA.debugLineNum = 95;BA.debugLine="ProgressDialogShow(\"Please Wait...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Please Wait...");
 //BA.debugLineNum = 96;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 98;BA.debugLine="If FileName.EndsWith(\".mtz\") = True Then";
if (_filename.endsWith(".mtz")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 99;BA.debugLine="File.Copy(Dir, FileName, File.DirRootExternal,";
anywheresoftware.b4a.keywords.Common.File.Copy(_dir,_filename,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"Htetz.zip");
 //BA.debugLineNum = 100;BA.debugLine="zip.ABUnzip(File.DirRootExternal & \"/Htetz.zip";
mostCurrent._zip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/Htetz.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Naing");
 //BA.debugLineNum = 102;BA.debugLine="File.Copy(File.DirAssets,\"Fuck\",File.DirRootEx";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"Fuck",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"d.zip");
 //BA.debugLineNum = 103;BA.debugLine="zip.ABUnzip(File.DirRootExternal & \"/d.zip\",Fi";
mostCurrent._zip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/d.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz");
 //BA.debugLineNum = 104;BA.debugLine="File.Delete(File.DirRootExternal,\"d.zip\")";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal(),"d.zip");
 //BA.debugLineNum = 105;BA.debugLine="File.Delete(File.DirRootExternal & \"/.Htetz/.d";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc");
 //BA.debugLineNum = 107;BA.debugLine="File.Delete(File.DirRootExternal & \"/.Htetz/.d";
anywheresoftware.b4a.keywords.Common.File.Delete(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc");
 //BA.debugLineNum = 108;BA.debugLine="ml.cp(\"/sdcard/.Naing/fonts/*.ttf\",\"/sdcard/.H";
mostCurrent._ml.cp("/sdcard/.Naing/fonts/*.ttf","/sdcard/.Htetz/.data/content/fonts/ZawgyiX1.mrc");
 //BA.debugLineNum = 109;BA.debugLine="If File.Exists(File.DirRootExternal & \"/.Htetz";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc")==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 110;BA.debugLine="Na";
_na();
 //BA.debugLineNum = 111;BA.debugLine="File.Copy(File.DirRootExternal & \"/.Naing/font";
anywheresoftware.b4a.keywords.Common.File.Copy(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Naing/fonts",mostCurrent._ss,anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz/.data/content/fonts","ZawgyiX1.mrc");
 };
 //BA.debugLineNum = 114;BA.debugLine="zip.ABZipDirectory(File.DirRootExternal & \"/.H";
mostCurrent._zip.ABZipDirectory(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Htetz",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MiFont.zip");
 //BA.debugLineNum = 115;BA.debugLine="ml.mkdir (\"/sdcard/MIUI/theme\")";
mostCurrent._ml.mkdir("/sdcard/MIUI/theme");
 //BA.debugLineNum = 116;BA.debugLine="Log(zip.ABUnzip(File.DirRootExternal & \"/MiFon";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(mostCurrent._zip.ABUnzip(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MiFont.zip",anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme")));
 //BA.debugLineNum = 117;BA.debugLine="Log(File.ListFiles(File.DirrootExternal & \"/MI";
anywheresoftware.b4a.keywords.Common.Log(BA.ObjectToString(anywheresoftware.b4a.keywords.Common.File.ListFiles(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme")));
 //BA.debugLineNum = 118;BA.debugLine="ml.RootCmd(\"dd if=\"&File.DirrootExternal &\"/MI";
mostCurrent._ml.RootCmd("dd if="+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme/.data of="+anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme","",(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(anywheresoftware.b4a.keywords.Common.Null),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 119;BA.debugLine="ml.mkdir (\"/sdcard/MIUI/theme\")";
mostCurrent._ml.mkdir("/sdcard/MIUI/theme");
 //BA.debugLineNum = 120;BA.debugLine="ProgressDialogShow(\"Please Wait...\")";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow(mostCurrent.activityBA,"Please Wait...");
 //BA.debugLineNum = 121;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 123;BA.debugLine="Msgbox(\"Please choose font/theme file\"&CRLF& \"";
anywheresoftware.b4a.keywords.Common.Msgbox("Please choose font/theme file"+anywheresoftware.b4a.keywords.Common.CRLF+".ttf Or .mtz","Incorrect  File",mostCurrent.activityBA);
 //BA.debugLineNum = 124;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 };
 };
 //BA.debugLineNum = 128;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim zip As ABZipUnzip";
mostCurrent._zip = new com.AB.ABZipUnzip.ABZipUnzip();
 //BA.debugLineNum = 16;BA.debugLine="Dim b1,b2 As Button";
mostCurrent._b1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._b2 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim b As AdView";
mostCurrent._b = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim i As InterstitialAd";
mostCurrent._i = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim ml As MLfiles";
mostCurrent._ml = new MLfiles.Fileslib.MLfiles();
 //BA.debugLineNum = 21;BA.debugLine="Dim ss As String";
mostCurrent._ss = "";
 //BA.debugLineNum = 22;BA.debugLine="Dim fileList As List";
mostCurrent._filelist = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 23;BA.debugLine="Dim n As Int";
_n = 0;
 //BA.debugLineNum = 24;BA.debugLine="Dim lb As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _i_adclosed() throws Exception{
 //BA.debugLineNum = 188;BA.debugLine="Sub i_AdClosed";
 //BA.debugLineNum = 189;BA.debugLine="i.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 190;BA.debugLine="End Sub";
return "";
}
public static String  _i_adopened() throws Exception{
 //BA.debugLineNum = 184;BA.debugLine="Sub i_adopened";
 //BA.debugLineNum = 185;BA.debugLine="Log(\"Opened\")";
anywheresoftware.b4a.keywords.Common.Log("Opened");
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _i_failedtoreceivead(String _errorcode) throws Exception{
 //BA.debugLineNum = 179;BA.debugLine="Sub i_FailedToReceiveAd (ErrorCode As String)";
 //BA.debugLineNum = 180;BA.debugLine="Log(\"not Received - \" &\"Error Code: \"&ErrorCode)";
anywheresoftware.b4a.keywords.Common.Log("not Received - "+"Error Code: "+_errorcode);
 //BA.debugLineNum = 181;BA.debugLine="i.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _i_receivead() throws Exception{
 //BA.debugLineNum = 175;BA.debugLine="Sub i_ReceiveAd";
 //BA.debugLineNum = 176;BA.debugLine="Log(\"Received\")";
anywheresoftware.b4a.keywords.Common.Log("Received");
 //BA.debugLineNum = 177;BA.debugLine="End Sub";
return "";
}
public static String  _na() throws Exception{
 //BA.debugLineNum = 71;BA.debugLine="Sub Na";
 //BA.debugLineNum = 72;BA.debugLine="fileList = File.ListFiles (File.DirRootExternal &";
mostCurrent._filelist = anywheresoftware.b4a.keywords.Common.File.ListFiles(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/.Naing/fonts");
 //BA.debugLineNum = 73;BA.debugLine="fileList.Sort (True)";
mostCurrent._filelist.Sort(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="n = 1";
_n = (int) (1);
 //BA.debugLineNum = 75;BA.debugLine="ss = fileList.Get(n)";
mostCurrent._ss = BA.ObjectToString(mostCurrent._filelist.Get(_n));
 //BA.debugLineNum = 76;BA.debugLine="Log(ss)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._ss);
 //BA.debugLineNum = 77;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ad1,ad2 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
_ad2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="Dim cc As ContentChooser";
_cc = new anywheresoftware.b4a.phone.Phone.ContentChooser();
 //BA.debugLineNum = 11;BA.debugLine="Dim t As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 191;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 192;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 193;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 194;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 195;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 196;BA.debugLine="copy.setText(\"MIUI Font Install can change font s";
_copy.setText(mostCurrent.activityBA,"MIUI Font Install can change font style for all MIUI version.	You can also use this app To change font style	without designer account Or rooting phone in MIUI version 8 Or upper.	This Application can support both ttf And mtz File format.	Download Free at : http://www.htetznaing.com/search?q=MIUI+Font+Installer");
 //BA.debugLineNum = 197;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 198;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 199;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 200;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("#COC_Myanmar_Tool"));
 //BA.debugLineNum = 201;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 202;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 130;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 131;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="ml.rm(\"/sdcard/MiFont.zip\")";
mostCurrent._ml.rm("/sdcard/MiFont.zip");
 //BA.debugLineNum = 133;BA.debugLine="ml.rm(\"/sdcard/Htetz.zip\")";
mostCurrent._ml.rm("/sdcard/Htetz.zip");
 //BA.debugLineNum = 134;BA.debugLine="ml.rmrf(\"/sdcard/.Htetz\")";
mostCurrent._ml.rmrf("/sdcard/.Htetz");
 //BA.debugLineNum = 135;BA.debugLine="ml.rmrf(\"/sdcard/.Naing\")";
mostCurrent._ml.rmrf("/sdcard/.Naing");
 //BA.debugLineNum = 136;BA.debugLine="ProgressDialogHide";
anywheresoftware.b4a.keywords.Common.ProgressDialogHide();
 //BA.debugLineNum = 137;BA.debugLine="If File.Exists(File.DirRootExternal & \"/MIUI/them";
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirRootExternal()+"/MIUI/theme/.data/content/fonts","ZawgyiX1.mrc")==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 138;BA.debugLine="Msgbox(\"Now! you can change font\",\"Completed\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Now! you can change font","Completed",mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 140;BA.debugLine="Msgbox(\"Error\",\"ERROR\")";
anywheresoftware.b4a.keywords.Common.Msgbox("Error","ERROR",mostCurrent.activityBA);
 };
 //BA.debugLineNum = 142;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 143;BA.debugLine="End Sub";
return "";
}
}
