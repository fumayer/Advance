package com.quduquxie.util;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class OpenUDID {

	private static String user_device_id;
	private static long effective_time;

	private final static String PREF_KEY = "openudid";
	private final static String TIME = "time";
	private static final String COMMON_PREFS = "common_prefs";
	private static final String GENERATE_UDID = "generate_udid";

	public static void syncContext(Context context) {
		if (user_device_id == null) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_PREFS, Context.MODE_PRIVATE);
			boolean new_uuid = sharedPreferences.getBoolean(GENERATE_UDID, false);
			String key_preference = sharedPreferences.getString(PREF_KEY, null);
			long preference_time = sharedPreferences.getLong(TIME, 0);
			if (!new_uuid || key_preference == null || preference_time == 0) {
				user_device_id = getUniqueId(context);
				effective_time = System.currentTimeMillis();
				Editor editor = sharedPreferences.edit();
				editor.putString(PREF_KEY, user_device_id);
				editor.putLong(TIME, effective_time);
				editor.putBoolean(GENERATE_UDID, true);
				editor.apply();
			} else {
				user_device_id = key_preference;
				effective_time = preference_time;
			}
		}
	}

	private static String getOpenUDIDInContext() {
		return user_device_id;
	}

	public static String getCorpUDID(String corpIdentifier) {
		return Md5(String.format("%s.%s", corpIdentifier, getOpenUDIDInContext()));
	}

	public static long getTime() {
		return effective_time;
	}

	private static String Md5(String input) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "null";
		}
		messageDigest.update(input.getBytes(), 0, input.length());
		byte digest[] = messageDigest.digest();

		String mOutput = "";
		for (int i = 0; i < digest.length; i++) {
			int b = (0xFF & digest[i]);
			if (b <= 0xF)
				mOutput += "0";
			mOutput += Integer.toHexString(b);
		}
		return mOutput.toUpperCase();
	}

	private static String getUniqueId(Context context) {
		final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceID = null, androidID, macAddress, serialNumber = null;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE, context.getPackageName())) {
			deviceID = telephonyManager.getDeviceId();
		}
		androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		macAddress = wifiManager.getConnectionInfo().getMacAddress();
		try {
			Class<?> classObject = Class.forName("android.os.SystemProperties");
			Method method = classObject.getMethod("get", String.class, String.class);
			serialNumber = (String) method.invoke(classObject, "ro.serialno", "unknown");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (deviceID == null || TextUtils.isEmpty(deviceID) && androidID == null || TextUtils.isEmpty(androidID) && macAddress == null || TextUtils.isEmpty(macAddress) && serialNumber == null || TextUtils.isEmpty(serialNumber)) {
			Logger.d(UUID.randomUUID().toString());
			return UUID.randomUUID().toString();
		}
		deviceID = "IEMI:" + deviceID;
		androidID = "ANDROID_ID:" + androidID;
		macAddress = "MAC:" + macAddress;
		serialNumber = "SERIAL:" + serialNumber;
		UUID deviceUuid = new UUID(androidID.hashCode(), deviceID.hashCode() | macAddress.hashCode() | serialNumber.hashCode());
		String uuid = deviceUuid.toString();
		Logger.d(uuid);
		return uuid;
	}

}
