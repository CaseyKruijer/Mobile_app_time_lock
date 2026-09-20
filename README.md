command voor adb

controleer welk device connected is

.\adb.exe devices

maak app device owner

.\adb.exe shell dpm set-device-owner com.example.timelock/.TimeLockDeviceAdminReceiver

controleren

.\adb.exe shell dpm list-owners

remove owner

.\adb.exe shell dpm remove-active-admin com.example.timelock/.TimeLockDeviceAdminReceiver
