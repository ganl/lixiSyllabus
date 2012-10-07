package com.example.syllabus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

import com.example.syllabus.service.PhoneStateService;
import com.example.syllabus.utils.CommonConstants;

public class PhoneStateReceiver extends BroadcastReceiver
{
    
    private static final String PHONE_STATE = "android.intent.action.PHONE_STATE";
    
    private String incomingNumber;
    
    private AudioManager mAudioManager;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("PhoneStateReceiver", "监听到电话状态改变" + intent.getAction());
        Bundle bundle = intent.getExtras();
        incomingNumber = bundle.getString("incoming_number");
        
        Log.i("PhoneStateReceiver", "incomingNumber: " + incomingNumber);
        
        // preserve the state of AudioManager
        mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        
        if (PHONE_STATE.equals(intent.getAction()) && null != incomingNumber)
        {// 电话状态改变
         // notifyIncomingCallAndCallBack(context);
            if (AudioManager.RINGER_MODE_SILENT != mAudioManager.getRingerMode())
            {
                // mute the phone until the ring is stopped
                CommonConstants.defaultRingerMode = mAudioManager.getRingerMode();
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Log.i(context.toString(), "RINGER MODE has been set to silent");
            }
            // Log.i("PhoneStateReceiver", "-------in PHONESTATE--------");
            Intent phoneintent = new Intent(context, PhoneStateService.class);
            context.startService(phoneintent);
        }
    }
}
