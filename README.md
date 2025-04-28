# Android Keystore safe memory PoC

This simple app creates an RSA key pair and show its security level.

## Prerequisites

- Android Studio 2024.3.1

## How to run

Open the project with Android Studio, create a virtual device (or connect a device) and run the
application.
Clicking the floating button should display a notification with the security level used for the key.

The possible values for security level are:

- SECURITY_LEVEL_SOFTWARE: no secure hardware used.
- SECURITY_LEVEL_STRONGBOX: the Google Strongbox secure hardware has been used. 
- SECURITY_LEVEL_TRUSTED_ENVIRONMENT: the ARM TrustZone secure hardware has been used.
- SECURITY_LEVEL_UNKNOWN: unknown security level.
- SECURITY_LEVEL_UNKNOWN_SECURE: unknown secure hardware used.

More information about Android security levels:

- [KeyInfo.getSecurityLevel()](https://developer.android.com/reference/android/security/keystore/KeyInfo#getSecurityLevel())
- [KeyProperies.SECURITY_LEVEL_*](https://developer.android.com/reference/android/security/keystore/KeyProperties#SECURITY_LEVEL_SOFTWARE)
- [Android keystore](https://developer.android.com/privacy-and-security/keystore)