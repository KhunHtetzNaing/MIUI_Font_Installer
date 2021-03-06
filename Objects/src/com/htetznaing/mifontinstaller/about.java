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

public class about extends Activity implements B4AActivity{
	public static about mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mifontinstaller", "com.htetznaing.mifontinstaller.about");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (about).");
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
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mifontinstaller", "com.htetznaing.mifontinstaller.about");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mifontinstaller.about", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (about) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (about) Resume **");
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
		return about.class;
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
        BA.LogInfo("** Activity (about) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            BA.LogInfo("** Activity (about) Resume **");
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
public static anywheresoftware.b4a.objects.Timer _t = null;
public static anywheresoftware.b4a.objects.Timer _t1 = null;
public anywheresoftware.b4a.objects.StringUtils _su = null;
public anywheresoftware.b4a.phone.Phone.PhoneIntents _p = null;
public anywheresoftware.b4a.objects.ListViewWrapper _lstone = null;
public anywheresoftware.b4a.objects.drawable.BitmapDrawable _abg = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _b = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _i = null;
public com.htetznaing.mifontinstaller.main _main = null;
public com.htetznaing.mifontinstaller.install _install = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
anywheresoftware.b4a.objects.ImageViewWrapper _imvlogo = null;
anywheresoftware.b4a.objects.LabelWrapper _lblname = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _bg = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _c = null;
anywheresoftware.b4a.objects.LabelWrapper _lblcredit = null;
int _h = 0;
 //BA.debugLineNum = 23;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 25;BA.debugLine="Activity.Title = \"About\"";
mostCurrent._activity.setTitle((Object)("About"));
 //BA.debugLineNum = 26;BA.debugLine="abg.Initialize(LoadBitmap(File.DirAssets,\"bg.jpg\"";
mostCurrent._abg.Initialize((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"bg.jpg").getObject()));
 //BA.debugLineNum = 27;BA.debugLine="Activity.Background = abg";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(mostCurrent._abg.getObject()));
 //BA.debugLineNum = 29;BA.debugLine="Dim imvLogo As ImageView";
_imvlogo = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 30;BA.debugLine="imvLogo.Initialize (\"imv\")";
_imvlogo.Initialize(mostCurrent.activityBA,"imv");
 //BA.debugLineNum = 31;BA.debugLine="imvLogo.Bitmap = LoadBitmap(File.DirAssets , \"ico";
_imvlogo.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"icon.png").getObject()));
 //BA.debugLineNum = 32;BA.debugLine="imvLogo.Gravity = Gravity.FILL";
_imvlogo.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 33;BA.debugLine="Activity.AddView ( imvLogo , 50%x - 50dip  , 20di";
mostCurrent._activity.AddView((android.view.View)(_imvlogo.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 35;BA.debugLine="Dim lblName As  Label";
_lblname = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Dim bg As ColorDrawable";
_bg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 37;BA.debugLine="bg.Initialize (Colors.DarkGray , 10dip)";
_bg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.DarkGray,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 38;BA.debugLine="lblName.Initialize (\"lbname\")";
_lblname.Initialize(mostCurrent.activityBA,"lbname");
 //BA.debugLineNum = 39;BA.debugLine="lblName.Background = bg";
_lblname.setBackground((android.graphics.drawable.Drawable)(_bg.getObject()));
 //BA.debugLineNum = 40;BA.debugLine="lblName.Gravity = Gravity.CENTER";
_lblname.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 41;BA.debugLine="lblName.Text = \"MIUI Font Installer\"";
_lblname.setText((Object)("MIUI Font Installer"));
 //BA.debugLineNum = 42;BA.debugLine="lblName.TextSize = 13";
_lblname.setTextSize((float) (13));
 //BA.debugLineNum = 43;BA.debugLine="lblName.TextColor = Colors.White";
_lblname.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 44;BA.debugLine="Activity.AddView (lblName , 100%x / 2 - 90dip , 1";
mostCurrent._activity.AddView((android.view.View)(_lblname.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)/(double)2-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (130)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (180)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 45;BA.debugLine="lblName.Height = su.MeasureMultilineTextHeight (l";
_lblname.setHeight((int) (mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(_lblname.getObject()),_lblname.getText())+anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5))));
 //BA.debugLineNum = 48;BA.debugLine="Dim c As ColorDrawable";
_c = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 49;BA.debugLine="c.Initialize (Colors.White , 10dip )";
_c.Initialize(anywheresoftware.b4a.keywords.Common.Colors.White,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 50;BA.debugLine="lstOne.Initialize (\"lstOnes\")";
mostCurrent._lstone.Initialize(mostCurrent.activityBA,"lstOnes");
 //BA.debugLineNum = 51;BA.debugLine="lstOne.Background = c";
mostCurrent._lstone.setBackground((android.graphics.drawable.Drawable)(_c.getObject()));
 //BA.debugLineNum = 52;BA.debugLine="lstOne.SingleLineLayout .Label.TextSize = 12";
mostCurrent._lstone.getSingleLineLayout().Label.setTextSize((float) (12));
 //BA.debugLineNum = 53;BA.debugLine="lstOne.SingleLineLayout .Label .TextColor = Color";
mostCurrent._lstone.getSingleLineLayout().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 54;BA.debugLine="lstOne.SingleLineLayout .Label .Gravity = Gravity";
mostCurrent._lstone.getSingleLineLayout().Label.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 55;BA.debugLine="lstOne.SingleLineLayout .ItemHeight = 40dip";
mostCurrent._lstone.getSingleLineLayout().setItemHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (40)));
 //BA.debugLineNum = 56;BA.debugLine="lstOne.AddSingleLine2 (\"App Name : MIUI Font Inst";
mostCurrent._lstone.AddSingleLine2("App Name : MIUI Font Installer",(Object)(1));
 //BA.debugLineNum = 57;BA.debugLine="lstOne.AddSingleLine2 (\"Current Version : 1.0(Bet";
mostCurrent._lstone.AddSingleLine2("Current Version : 1.0(Beta)",(Object)(2));
 //BA.debugLineNum = 58;BA.debugLine="lstOne.AddSingleLine2 (\"Developed By : Khun Htetz";
mostCurrent._lstone.AddSingleLine2("Developed By : Khun Htetz Naing",(Object)(3));
 //BA.debugLineNum = 59;BA.debugLine="lstOne.AddSingleLine2 (\"Powered By : Myanmar Andr";
mostCurrent._lstone.AddSingleLine2("Powered By : Myanmar Android Apps",(Object)(4));
 //BA.debugLineNum = 60;BA.debugLine="lstOne.AddSingleLine2 (\"Website : www.HtetzNaing.";
mostCurrent._lstone.AddSingleLine2("Website : www.HtetzNaing.com    ",(Object)(5));
 //BA.debugLineNum = 61;BA.debugLine="lstOne.AddSingleLine2 (\"Facebook : www.facebook.c";
mostCurrent._lstone.AddSingleLine2("Facebook : www.facebook.com/MgHtetzNaing ",(Object)(6));
 //BA.debugLineNum = 62;BA.debugLine="Activity.AddView ( lstOne, 30dip , 170dip , 100%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lstone.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (30)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (170)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (60))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (200)));
 //BA.debugLineNum = 64;BA.debugLine="Dim lblCredit As Label";
_lblcredit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 65;BA.debugLine="lblCredit.Initialize (\"lblCredit\")";
_lblcredit.Initialize(mostCurrent.activityBA,"lblCredit");
 //BA.debugLineNum = 66;BA.debugLine="lblCredit.TextColor = Colors.Black";
_lblcredit.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 67;BA.debugLine="lblCredit.TextSize = 13";
_lblcredit.setTextSize((float) (13));
 //BA.debugLineNum = 68;BA.debugLine="lblCredit.Gravity = Gravity.CENTER";
_lblcredit.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 69;BA.debugLine="lblCredit.Text = \"Credits & Thanks Ye Thuya Lin!\"";
_lblcredit.setText((Object)("Credits & Thanks Ye Thuya Lin!"));
 //BA.debugLineNum = 70;BA.debugLine="Activity.AddView (lblCredit, 10dip,(lstOne.Top+ls";
mostCurrent._activity.AddView((android.view.View)(_lblcredit.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),(int) ((mostCurrent._lstone.getTop()+mostCurrent._lstone.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (20))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 71;BA.debugLine="lblCredit.Height = su.MeasureMultilineTextHeight";
_lblcredit.setHeight(mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(_lblcredit.getObject()),_lblcredit.getText()));
 //BA.debugLineNum = 73;BA.debugLine="Activity.AddMenuItem3(\"Share App\",\"share\",LoadBit";
mostCurrent._activity.AddMenuItem3((java.lang.CharSequence)("Share App"),"share",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"share.png").getObject()),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 75;BA.debugLine="b.Initialize2(\"b\",\"ca-app-pub-4173348573252986/71";
mostCurrent._b.Initialize2(mostCurrent.activityBA,"b","ca-app-pub-4173348573252986/7159416950",mostCurrent._b.SIZE_SMART_BANNER);
 //BA.debugLineNum = 76;BA.debugLine="Dim h As Int";
_h = 0;
 //BA.debugLineNum = 77;BA.debugLine="If GetDeviceLayoutValues.ApproximateScreenSize <";
if (anywheresoftware.b4a.keywords.Common.GetDeviceLayoutValues(mostCurrent.activityBA).getApproximateScreenSize()<6) { 
 //BA.debugLineNum = 78;BA.debugLine="If 100%x > 100%y Then h = 32dip Else h = 50dip";
if (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)>anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)) { 
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (32));}
else {
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50));};
 }else {
 //BA.debugLineNum = 80;BA.debugLine="h = 90dip";
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (90));
 };
 //BA.debugLineNum = 82;BA.debugLine="Activity.AddView(b,0dip,100%y - h,100%x,h)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (0)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_h),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),_h);
 //BA.debugLineNum = 83;BA.debugLine="b.LoadAd";
mostCurrent._b.LoadAd();
 //BA.debugLineNum = 85;BA.debugLine="i.Initialize(\"i\",\"ca-app-pub-4173348573252986/111";
mostCurrent._i.Initialize(mostCurrent.activityBA,"i","ca-app-pub-4173348573252986/1112883353");
 //BA.debugLineNum = 86;BA.debugLine="i.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 88;BA.debugLine="t.Initialize(\"t\",500)";
_t.Initialize(processBA,"t",(long) (500));
 //BA.debugLineNum = 89;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 90;BA.debugLine="t1.Initialize(\"t1\",30000)";
_t1.Initialize(processBA,"t1",(long) (30000));
 //BA.debugLineNum = 91;BA.debugLine="t1.Enabled = True";
_t1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 136;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 132;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 134;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 15;BA.debugLine="Dim su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 16;BA.debugLine="Dim p As PhoneIntents";
mostCurrent._p = new anywheresoftware.b4a.phone.Phone.PhoneIntents();
 //BA.debugLineNum = 17;BA.debugLine="Dim lstOne As ListView";
mostCurrent._lstone = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Dim abg As BitmapDrawable";
mostCurrent._abg = new anywheresoftware.b4a.objects.drawable.BitmapDrawable();
 //BA.debugLineNum = 19;BA.debugLine="Dim b As AdView";
mostCurrent._b = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Dim i As InterstitialAd";
mostCurrent._i = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _i_adclosed() throws Exception{
 //BA.debugLineNum = 180;BA.debugLine="Sub i_AdClosed";
 //BA.debugLineNum = 181;BA.debugLine="i.LoadAd";
mostCurrent._i.LoadAd();
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _imv_click() throws Exception{
 //BA.debugLineNum = 117;BA.debugLine="Sub imv_Click";
 //BA.debugLineNum = 118;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 119;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetznai";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.htetznaing.com/")));
 //BA.debugLineNum = 120;BA.debugLine="End Sub";
return "";
}
public static String  _lblcredit_click() throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub lblCredit_Click";
 //BA.debugLineNum = 128;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 129;BA.debugLine="StartActivity(p.OpenBrowser (\"https://www.faceboo";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("https://www.facebook.com/profile.php?id=100005614698241")));
 //BA.debugLineNum = 130;BA.debugLine="End Sub";
return "";
}
public static String  _lbname_click() throws Exception{
 //BA.debugLineNum = 122;BA.debugLine="Sub lbname_Click";
 //BA.debugLineNum = 123;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 124;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetznain";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.htetznaing.com/")));
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
return "";
}
public static String  _lstones_itemclick(int _position,Object _value) throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
 //BA.debugLineNum = 140;BA.debugLine="Sub lstOnes_ItemClick (Position As Int, Value As O";
 //BA.debugLineNum = 141;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 142;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)(1),(Object)(3),(Object)(4),(Object)(5),(Object)(6))) {
case 0: {
 //BA.debugLineNum = 144;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetzna";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.htetznaing.com/search?q=MIUI+Font+Installer")));
 break; }
case 1: {
 //BA.debugLineNum = 146;BA.debugLine="Try";
try { //BA.debugLineNum = 147;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 148;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://profile/100006126339714");
 //BA.debugLineNum = 149;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e11) {
			processBA.setLastException(e11); //BA.debugLineNum = 151;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 152;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"htt";
_facebook.Initialize(_facebook.ACTION_VIEW,"https://m.facebook.com/MgHtetzNaing");
 //BA.debugLineNum = 153;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 };
 break; }
case 2: {
 //BA.debugLineNum = 156;BA.debugLine="Try";
try { //BA.debugLineNum = 157;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 158;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 159;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e21) {
			processBA.setLastException(e21); //BA.debugLineNum = 161;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 162;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"htt";
_facebook.Initialize(_facebook.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 163;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 };
 break; }
case 3: {
 //BA.debugLineNum = 166;BA.debugLine="StartActivity(p.OpenBrowser(\"http://www.htetzna";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(mostCurrent._p.OpenBrowser("http://www.htetznaing.com")));
 break; }
case 4: {
 //BA.debugLineNum = 168;BA.debugLine="Try";
try { //BA.debugLineNum = 169;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 170;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb:";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://profile/100006126339714");
 //BA.debugLineNum = 171;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e33) {
			processBA.setLastException(e33); //BA.debugLineNum = 173;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 174;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"htt";
_facebook.Initialize(_facebook.ACTION_VIEW,"https://m.facebook.com/MgHtetzNaing");
 //BA.debugLineNum = 175;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 };
 break; }
}
;
 //BA.debugLineNum = 178;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim t,t1 As Timer";
_t = new anywheresoftware.b4a.objects.Timer();
_t1 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
public static String  _share_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
b4a.util.BClipboard _copy = null;
 //BA.debugLineNum = 94;BA.debugLine="Sub share_Click";
 //BA.debugLineNum = 95;BA.debugLine="t.Enabled = True";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 96;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 97;BA.debugLine="Dim copy As BClipboard";
_copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 98;BA.debugLine="copy.clrText";
_copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 99;BA.debugLine="copy.setText(\"MIUI Font Install can change font s";
_copy.setText(mostCurrent.activityBA,"MIUI Font Install can change font style for all MIUI version.	You can also use this app To change font style	without designer account Or rooting phone in MIUI version 8 Or upper.	This Application can support both ttf And mtz File format.	Download Free at : http://www.htetznaing.com/search?q=MIUI+Font+Installer");
 //BA.debugLineNum = 100;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 101;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 102;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(_copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 103;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("#COC_Myanmar_Tool"));
 //BA.debugLineNum = 104;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 105;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 106;BA.debugLine="End Sub";
return "";
}
public static String  _t_tick() throws Exception{
 //BA.debugLineNum = 108;BA.debugLine="Sub t_Tick";
 //BA.debugLineNum = 109;BA.debugLine="If	i.Ready Then i.Show Else i.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 110;BA.debugLine="t.Enabled = False";
_t.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _t1_tick() throws Exception{
 //BA.debugLineNum = 113;BA.debugLine="Sub t1_Tick";
 //BA.debugLineNum = 114;BA.debugLine="If	i.Ready Then i.Show Else i.LoadAd";
if (mostCurrent._i.getReady()) { 
mostCurrent._i.Show();}
else {
mostCurrent._i.LoadAd();};
 //BA.debugLineNum = 115;BA.debugLine="End Sub";
return "";
}
}
