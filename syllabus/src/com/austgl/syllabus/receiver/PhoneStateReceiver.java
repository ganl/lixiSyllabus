package com.austgl.syllabus.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.austgl.syllabus.service.PhoneStateService;
import com.austgl.syllabus.utils.CommonConstants;
import com.austgl.syllabus.utils.Lunar;

public class PhoneStateReceiver extends BroadcastReceiver {

	private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";

	private String incomingNumber;

	private AudioManager mAudioManager;

	private boolean isHoliday;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("PhoneStateReceiver", "监听到电话状态改变" + intent.getAction());
		Bundle bundle = intent.getExtras();
		incomingNumber = bundle.getString("incoming_number");

		Log.i("PhoneStateReceiver", "incomingNumber: " + incomingNumber);

		// preserve the state of AudioManager
		mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		Calendar today = Calendar.getInstance();
		today.setTime(new Date(System.currentTimeMillis()));
		Lunar lunar = new Lunar(today);

		Log.i("PhoneStateReceiver", "lunar to string:" + lunar.toString());
		for (int i = 0; i < Lunar.CHINA_HOLIDAYS.length; i++) {
			if (lunar.toString().equals(Lunar.CHINA_HOLIDAYS[i])) {
				isHoliday = true;
			}
		}
		if (!isHoliday) {
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
			String current = formatter.format(date);
			Log.i("PhoneStateReceiver", "current is " + current);
			for (int i = 0; i < Lunar.NATIONAL_HOLIDAYS.length; i++) {
				if (current.equals(Lunar.NATIONAL_HOLIDAYS[i])) {
					isHoliday = true;
				}
			}
		}

		if (PHONE_STATE.equals(intent.getAction()) && null != incomingNumber
				&& !isHoliday) {// 电话状态改变
			if (AudioManager.RINGER_MODE_SILENT != mAudioManager
					.getRingerMode()) {
				// mute the phone until the ring is stopped
				CommonConstants.defaultRingerMode = mAudioManager
						.getRingerMode();
				mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
				Log.i(context.toString(), "RINGER MODE has been set to silent");
			}
			Intent phoneintent = new Intent(context, PhoneStateService.class);
			context.startService(phoneintent);
		}
	}
}
