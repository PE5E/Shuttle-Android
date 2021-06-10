# Shuttle-Android

## This repo is about the X50V2 Plus on Android-x86  
  
There are 2 problems running Android on this hardware:
- The touch input is like a touchpad (relative position), while we want to have a touchscreen (absolute position)
- The brightness can not be adjusted
  
    
  
## Touchscreen:  
The device hardware used is a: eGalax Inc. USB TouchController  
This repo's folder touchscreen-fix contains a file *Vendor_0eef_Product_0001.idc*  
Copy this file to the Shuttle PC in the folder: */system/usr/idc/*  
Then reboot and run  the `Calibration` app  
The touchscreen should be fully operational now  
  
  
## Brightness:
The hardware can be controlled here: */sys/class/backlight/acpi_video0*  
If you open a terminal (attach a USB keyboard and press with Alt-F1) you can manually set the brightness:  
  `cd /sys/class/backlight/acpi_video0`  
  `cat max_brightness` will give you: `7`  
So we can set the brightness from 0-7  
Read the actual value with:  
  `cat actual_brightness` this gives me `4`  
Set the brighness to 2:  
  `echo 2 >> brightness`  
  
  Since it is very boresome to manually set the brightness every time, I created an app to do it for me. The app actually uses these commands in a shell. Down side is that it will need super-user privileges.  
  The app is in the folder: *brightness-fix*  
  The source code of this app is also included  
