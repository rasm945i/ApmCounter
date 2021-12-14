# ApmCounter
Track actions per minute, second or other specified custom interval.

## ??? 
For some reason, a "JNativeHook.dll" is created in the same directory from which the program is launched, when the program is launched. It annoys me, but the program makes use of JNativeHook to track mouse and keyboard events.

## OBS Integration 
If the OBS integration is enabled, you must have installed the OBS Websocket plugin for it to actually work.  
Your OBS Text source must be named the same thing as your group. That is how the program identifies which source to update in OBS.

## CSV Saves
The last value may be incorrect and values may be off by 1. I haven't tested this feature that much yet.
