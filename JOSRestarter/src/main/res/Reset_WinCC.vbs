'**************************************************************************
'*                                                                        *
'*                                                                        *
'*                            Reset WinCC                                 *
'*                                                                        *
'*                                                                        *
'**************************************************************************
'*              Copyright Siemens AG 2003   All rights reserved           *
'*                                                                        *
'*   The transmission and reproduction of this software and the           *
'*   exploitation and communication of its contence are not permitted     *
'*   without express authority. Offenders will be liable for compensation *
'*   for damage. All rights reserved, especially in the case of the       *
'*   granting of a patent or registrationof an utility model or design.   *
'*                                                                        *
'*                                                                        *
'*                        Siemens AG                                      *
'*                        Bereich Automation & Drives                     *
'*                        Geschäftsgebiet SIMATIC HMI                     *
'*                        Postfach 4848, D-90327 Nürnberg                 *
'*                                                                        *
'*                                                                        *
'*                                                                        *
'**************************************************************************


'**********************************
'Clean processes and services
'**********************************
Dim lvoWshShell 

Set lvoWshShell = CreateObject("Wscript.Shell")

lvowshShell.run "CCCleaner.exe -terminate",0,TRUE


'**********************************
'Create eventlog entry
'**********************************
'Create entry in application eventlog

Const EVENT_SUCCESS = 0

Dim objUser
Dim strUser
Dim objShell

For Each objUser In GetObject("winmgmts:{impersonationLevel=impersonate}").InstancesOf("Win32_ComputerSystem")
  strUser = objUser.UserName
Next

Set objShell = CreateObject("WScript.Shell")
objShell.LogEvent EVENT_SUCCESS, "ResetWinCC.vbs called by " & strUser


'**********************************
'Finish
'**********************************

msgbox "ready"