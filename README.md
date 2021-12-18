# ApmCounter
Track actions per minute, second or other specified custom interval.

## Installation
Requires Java 11 (maybe 16, I'm not sure) and the OBS Websocket Plugin if you wish to use the OBS Integration (https://obsproject.com/forum/resources/obs-websocket-remote-control-obs-studio-from-websockets.466/)

## ??? 
For some reason, a "JNativeHook.dll" is created in the same directory from which the program is launched, when the program is launched. It annoys me, but the program makes use of JNativeHook to track mouse and keyboard events.

## OBS Integration 
If the OBS integration is enabled, you must have installed the OBS Websocket plugin for it to actually work.  
Your OBS Text source must be named the same thing as your group. That is how the program identifies which source to update in OBS.

## CSV Saves
The last value may be incorrect and values may be off by 1. I haven't tested this feature that much yet.

## Recommended usage
For best performance, it is recommneded to use the deprecated "FreeType 2" text rather than Pango or GDI+.  
  
When using the program with OBS, the groups you add (with the "Add" button in the bottom left) must match the source name you wish to update.  
If your source is named "my clicks", the group should also be named "my clicks". Remember to hit "Apply Settings" after changing the name.
