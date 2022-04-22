package com.rnsecuritybridge;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;

import com.facebook.react.bridge.Callback;

import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import androidx.security.crypto.MasterKeys;


public class SecureSensitiveInfo extends ReactContextBaseJavaModule {

    private static ReactApplicationContext reactContext;

    SecureSensitiveInfo(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    public String getName() {
        return "SecureSensitiveInfo";
    }

    private MasterKey getMasterKey() {
        try {
            KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(
                    "_androidx_security_master_key_",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .build();

            return new MasterKey.Builder(reactContext)
                    .setKeyGenParameterSpec(spec)
                    .build();
        } catch (Exception e) {
            Log.e("SecureSensitiveInfo", "Error on getting master key", e);
        }
        return null;
    }

    private SharedPreferences getEncryptedSharedPreferences() {
        try {
            String sharedPrefsFile = "my_secured_file.txt";
            return EncryptedSharedPreferences.create(
                    reactContext,
                    sharedPrefsFile,
                    getMasterKey(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error on getting encrypted shared preferences", e);
        }
        return null;
    }

    @ReactMethod
    public void secureInfo(String key, String value) {
        try {
            System.out.println("Inside native secure info method");

            Log.d("SecureSensitiveInfo", "Secure info with key: " + key
                    + " and value: " + value);

            // Encrypted Shared Preferences implementation =====================
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();

            SharedPreferences.Editor sharedPrefsEditor = sharedPreferences.edit();

            // Edit the user's shared preferences..
            sharedPrefsEditor.putString(key, value).apply();

            Log.d("SecureSensitiveInfo", "SecureBridgeTesting -> Secure info with key: " + key
                    + " and value: " + value + " is successfully stored in " +
                    "EncryptedSharedPreference");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @ReactMethod
    public void getInfo(String key) {
        try {
            System.out.println("Inside native get info method");

            // EncryptedSharedPreferences get all info
            SharedPreferences sharedPreferences = getEncryptedSharedPreferences();

            String value = sharedPreferences.getString(key, "defaultValue");

            Log.d("SecureSensitiveInfo", "SecureBridgeTesting -> value for " + key + " is: " + value );
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}