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
	Dim ad1,ad2 As Timer
	Dim cc As ContentChooser
	Dim t As Timer
End Sub

Sub Globals
	Dim zip As ABZipUnzip
	Dim b1,b2 As Button
	Dim b As AdView
	Dim i As InterstitialAd
	Dim ml As MLfiles
	
	Dim ss As String
	Dim fileList As List
	Dim n As Int
	Dim lb As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	lb.Initialize("")
	lb.Text = "[Choose Font/Theme File .ttf Or .mtz]"
	lb.Gravity = Gravity.CENTER
	Activity.AddMenuItem3("Share App","share",LoadBitmap(File.DirAssets,"share.png"),True)
	Activity.Title = "Install Custom Font"
b1.Initialize("b1")
b1.Text = "Pick Font File"
Activity.AddView(b1,20%x,20%y,60%x,10%y)

Activity.AddView(lb,0%x,b1.Top+b1.Height,100%x,5%y)
b2.Initialize("b2")
b2.Text = "Change Font"
	Activity.AddView(b2,20%x,lb.Top+lb.Height+1%y,60%x,10%y)

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
	
	ad1.Initialize("ad1",100)
	ad1.Enabled = False
	
	ad2.Initialize("ad2",60000)
	ad2.Enabled = True
	
	cc.Initialize("cc")

t.Initialize("t",3000)
t.Enabled = False
End Sub

Sub b1_Click
	cc.Show("*/*","Choose Your Font")
End Sub

Sub Na
	fileList = File.ListFiles (File.DirRootExternal & "/.Naing/fonts" )
	fileList.Sort (True)
	n = 1
	ss = fileList.Get(n)
	Log(ss)
End Sub

Sub cc_Result (Success As Boolean, Dir As String, FileName As String)
	Log(Dir & FileName)
	If Success Then
		If FileName.EndsWith(".ttf") = True Then
			File.Copy(File.DirAssets,"Fuck",File.DirRootExternal,"d.zip")
			zip.ABUnzip(File.DirRootExternal & "/d.zip",File.DirRootExternal & "/.Htetz")
			File.Delete(File.DirRootExternal,"d.zip")
			File.Delete(File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc")
			File.Copy(Dir, FileName, File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc")
			zip.ABZipDirectory(File.DirRootExternal & "/.Htetz",File.DirRootExternal & "/MiFont.zip")
			
			ml.mkdir ("/sdcard/MIUI/theme")
			Log(zip.ABUnzip(File.DirRootExternal & "/MiFont.zip", File.DirRootExternal & "/MIUI/theme"))
			Log(File.ListFiles(File.DirrootExternal & "/MIUI/theme"))
			ml.RootCmd("dd if="&File.DirrootExternal &"/MIUI/theme/.data of="&File.DirRootExternal&"/MIUI/theme","",Null,Null,False)
			ml.mkdir ("/sdcard/MIUI/theme")
			ProgressDialogShow("Please Wait...")
			t.Enabled = True
		Else
			If FileName.EndsWith(".mtz") = True Then
				File.Copy(Dir, FileName, File.DirRootExternal,"Htetz.zip")
				zip.ABUnzip(File.DirRootExternal & "/Htetz.zip",File.DirRootExternal & "/.Naing")
				
				File.Copy(File.DirAssets,"Fuck",File.DirRootExternal,"d.zip")
				zip.ABUnzip(File.DirRootExternal & "/d.zip",File.DirRootExternal & "/.Htetz")
				File.Delete(File.DirRootExternal,"d.zip")
				File.Delete(File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc")
				
				File.Delete(File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc")
				ml.cp("/sdcard/.Naing/fonts/*.ttf","/sdcard/.Htetz/.data/content/fonts/ZawgyiX1.mrc")
				If File.Exists(File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc") = False Then
				Na
				File.Copy(File.DirRootExternal & "/.Naing/fonts", ss, File.DirRootExternal & "/.Htetz/.data/content/fonts","ZawgyiX1.mrc")
				End If
		
				zip.ABZipDirectory(File.DirRootExternal & "/.Htetz",File.DirRootExternal & "/MiFont.zip")
				ml.mkdir ("/sdcard/MIUI/theme")
				Log(zip.ABUnzip(File.DirRootExternal & "/MiFont.zip", File.DirRootExternal & "/MIUI/theme"))
				Log(File.ListFiles(File.DirrootExternal & "/MIUI/theme"))
				ml.RootCmd("dd if="&File.DirrootExternal &"/MIUI/theme/.data of="&File.DirRootExternal&"/MIUI/theme","",Null,Null,False)
				ml.mkdir ("/sdcard/MIUI/theme")
				ProgressDialogShow("Please Wait...")
				t.Enabled = True
				Else
				Msgbox("Please choose font/theme file"&CRLF& ".ttf Or .mtz","Incorrect  File")
				ad1.Enabled = True
				End If
				End If
		End If
End Sub

Sub t_Tick
	ad1.Enabled = True
	ml.rm("/sdcard/MiFont.zip")
	ml.rm("/sdcard/Htetz.zip")
	ml.rmrf("/sdcard/.Htetz")
	ml.rmrf("/sdcard/.Naing")
	ProgressDialogHide
	If File.Exists(File.DirRootExternal & "/MIUI/theme/.data/content/fonts","ZawgyiX1.mrc") = True Then
		Msgbox("Now! you can change font","Completed")
	Else
		Msgbox("Error","ERROR")
	End If
	t.Enabled = False
End Sub

Sub b2_Click
	Dim ii As Intent
	ii.Initialize(ii.Action_Main,"")
	ii.SetComponent("com.android.settings/com.android.settings.Settings$FontSettingsActivity")
	Try
		StartActivity(ii)
	
	
	Catch
		Msgbox("Missing Font."&CRLF&"(or)"&CRLF&"Your Phone Is Not Xiaomi.","Error")
	End Try
End Sub

Sub ad1_Tick
	If i.Ready Then i.Show Else i.LoadAd
	ad1.Enabled = False
End Sub

Sub ad2_Tick
	If i.Ready Then i.Show Else i.LoadAd
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub i_ReceiveAd
	Log("Received")
End Sub

Sub i_FailedToReceiveAd (ErrorCode As String)
	Log("not Received - " &"Error Code: "&ErrorCode)
	i.LoadAd
End Sub

Sub i_adopened
	Log("Opened")
End Sub

Sub i_AdClosed
	i.LoadAd
End Sub
Sub share_Click
	ad1.Enabled = True
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