command voor adb
cd "$env:LOCALAPPDATA\Android\Sdk\platform-tools"
.\adb.exe devices
.\adb.exe shell dpm set-device-owner com.example.timelock/.TimeLockDeviceAdminReceiver

controleren
.\adb.exe shell dpm list-owners

