Type=Activity
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
Dim t,t1 As Timer
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim su As StringUtils 
	Dim p As PhoneIntents 
	Dim lstOne As ListView 
	Dim abg As BitmapDrawable
	Dim b As AdView
	Dim i As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	Activity.Title = "About"
	abg.Initialize(LoadBitmap(File.DirAssets,"bg.jpg"))
	Activity.Background = abg
	
	Dim imvLogo As ImageView 
	imvLogo.Initialize ("imv")
	imvLogo.Bitmap = LoadBitmap(File.DirAssets , "icon.png")
	imvLogo.Gravity = Gravity.FILL 
	Activity.AddView ( imvLogo , 50%x - 50dip  , 20dip ,  100dip  ,  100dip )
	
	Dim lblName As  Label 
	Dim bg As ColorDrawable 
	bg.Initialize (Colors.DarkGray , 10dip)
	lblName.Initialize ("lbname")
	lblName.Background = bg
	lblName.Gravity = Gravity.CENTER 
	lblName.Text = "MIUI Font Installer"
	lblName.TextSize = 13
	lblName.TextColor = Colors.White 
	Activity.AddView (lblName , 100%x / 2 - 90dip , 130dip , 180dip , 50dip)
	lblName.Height = su.MeasureMultilineTextHeight (lblName, lblName.Text ) + 5dip
	
	
	Dim c As ColorDrawable 
	c.Initialize (Colors.White , 10dip )
	lstOne.Initialize ("lstOnes")
	lstOne.Background = c
	lstOne.SingleLineLayout .Label.TextSize = 12
	lstOne.SingleLineLayout .Label .TextColor = Colors.DarkGray 
	lstOne.SingleLineLayout .Label .Gravity = Gravity.CENTER 
	lstOne.SingleLineLayout .ItemHeight = 40dip
	lstOne.AddSingleLine2 ("App Name : MIUI Font Installer",1)
	lstOne.AddSingleLine2 ("Current Version : 1.0(Beta)",2)
	lstOne.AddSingleLine2 ("Developed By : Khun Htetz Naing",3)
	lstOne.AddSingleLine2 ("Powered By : Myanmar Android Apps",4)
	lstOne.AddSingleLine2 ("Website : www.HtetzNaing.com    ",5)
	lstOne.AddSingleLine2 ("Facebook : www.facebook.com/MgHtetzNaing ", 6)
	Activity.AddView ( lstOne, 30dip , 170dip , 100%x -  60dip, 200dip)
	
	Dim lblCredit As Label 
	lblCredit.Initialize ("lblCredit")
	lblCredit.TextColor = Colors.Black
	lblCredit.TextSize = 13
	lblCredit.Gravity = Gravity.CENTER 
	lblCredit.Text = "Credits & Thanks Ye Thuya Lin!"
	Activity.AddView (lblCredit, 10dip,(lstOne.Top+lstOne.Height)+2%y, 100%x - 20dip, 50dip)
	lblCredit.Height = su.MeasureMultilineTextHeight (lblCredit, lblCredit.Text )
		
	Activity.AddMenuItem3("Share App","share",LoadBitmap(File.DirAssets,"share.png"),True)
	
	b.Initialize2("b","ca-app-pub-4173348573252986/7159416950",b.SIZE_SMART_BANNER)
	Dim h As Int
	If GetDeviceLayoutValues.ApproximateScreenSize < 6 Then
		If 100%x > 100%y Then h = 32dip Else h = 50dip
	Else
		h = 90dip
	End If
	Activity.AddView(b,0dip,100%y - h,100%x,h)
	b.LoadAd
	
	i.Initialize("i","ca-app-pub-4173348573252986/1112883353")
	i.LoadAd
	
	t.Initialize("t",500)
	t.Enabled = False
	t1.Initialize("t1",30000)
	t1.Enabled = True
End Sub

Sub share_Click
		t.Enabled = True
	Dim ShareIt As Intent
	Dim copy As BClipboard
	copy.clrText
	copy.setText("MIUI Font Install can change font style for all MIUI version.	You can also use this app To change font style	without designer account Or rooting phone in MIUI version 8 Or upper.	This Application can support both ttf And mtz File format.	Download Free at : http://www.htetznaing.com/search?q=MIUI+Font+Installer")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","#COC_Myanmar_Tool")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub

 Sub t_Tick
	If	i.Ready Then i.Show Else i.LoadAd
t.Enabled = False
End Sub

Sub t1_Tick
	If	i.Ready Then i.Show Else i.LoadAd
End Sub

 Sub imv_Click
		t.Enabled = True
 	StartActivity(p.OpenBrowser("http://www.htetznaing.com/"))
End Sub

Sub lbname_Click
		t.Enabled = True
	StartActivity(p.OpenBrowser("http://www.htetznaing.com/"))
End Sub

Sub lblCredit_Click
		t.Enabled = True
	StartActivity(p.OpenBrowser ("https://www.facebook.com/profile.php?id=100005614698241"))
End Sub

Sub Activity_Resume
     
End Sub

Sub Activity_Pause (UserClosed As Boolean)
     
End Sub

Sub lstOnes_ItemClick (Position As Int, Value As Object)
		t.Enabled = True
     Select Value
	 	Case 1
			StartActivity(p.OpenBrowser("http://www.htetznaing.com/search?q=MIUI+Font+Installer"))
		Case 3
			Try
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://profile/100006126339714")
				StartActivity(Facebook)
			Catch
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "https://m.facebook.com/MgHtetzNaing")
				StartActivity(Facebook)
			End Try
		Case 4
			Try
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://page/627699334104477")
				StartActivity(Facebook)
			Catch
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "https://m.facebook.com/MmFreeAndroidApps")
				StartActivity(Facebook)
			End Try
			Case 5
			StartActivity(p.OpenBrowser("http://www.htetznaing.com"))
		Case 6
			Try
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "fb://profile/100006126339714")
				StartActivity(Facebook)
			Catch
				Dim Facebook As Intent
				Facebook.Initialize(Facebook.ACTION_VIEW, "https://m.facebook.com/MgHtetzNaing")
				StartActivity(Facebook)
			End Try
	 End Select
End Sub

Sub i_AdClosed
	i.LoadAd
End Sub