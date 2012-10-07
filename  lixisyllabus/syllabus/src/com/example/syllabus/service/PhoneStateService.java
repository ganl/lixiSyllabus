package com.example.syllabus.service;

import java.lang.reflect.Method;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;
import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.utils.CommonConstants;

public class PhoneStateService extends Service
{
    
    private String callOutPhoneNum;
    
    private TelephonyManager telephonyManager;
    
    private NotificationManager nm;
    
    private Notification notification;
    
    private static boolean inCalling = false;
    
    private int lastState;
    
    private MyPhoneStateListener myPhoneStateListener;
    
    private Handler handler;
    
    String incomingNumber;
    
    private AudioManager mAudioManager;
    
    private boolean callEnded = false;
    
    private SharedPreferences share;
    
    boolean isShieldOpen;
    
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Log.i("STATE-ORIGINAL", telephonyManager.getCallState() + "");
        
        lastState = telephonyManager.getCallState();
        
        myPhoneStateListener = new MyPhoneStateListener();
        handler = new MyHandler();
        
        mAudioManager = (AudioManager)this.getSystemService(AUDIO_SERVICE);
        
        share = CommonConstants.getMyPreferences(this);
        
        isShieldOpen = share.getBoolean(CommonConstants.SHIELDED, false);
        
        // CommonConstants.defaultRingerMode = mAudioManager.getRingerMode();
        // mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        // Log.i("PhoneStateService", "RINGER MODE has been set to silent");
        
        // isShieldOpen = true;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("PhoneStateService", "service stated");
        if (isShieldOpen)
        {
            if (callEnded)
            {
                handler.sendEmptyMessage(2);
            }
            else
            {
                boolean isInClass = CommonConstants.isDuringCourseTime(new Date(System.currentTimeMillis()), this);
                System.out.println(isInClass);
                if (isInClass)
                {
                    telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
                }
                else
                {
                    handler.sendEmptyMessage(2);
                }
            }
        }
        else
        {
            handler.sendEmptyMessage(2);
        }
        
        return Service.START_NOT_STICKY;
    }
    
    // 电话状态监听
    private final class MyPhoneStateListener extends PhoneStateListener
    {
        
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            super.onCallStateChanged(state, incomingNumber);
            {// 如果应用启用，执行监听电话状态改变操作
                if (state == TelephonyManager.CALL_STATE_RINGING)// == 1
                {// 来电
                    Log.i("PhoneStateService", "------in --- CALL_STATE_RINGING isCallOut " + " incomingNumber "
                        + incomingNumber);
                    if (!inCalling)
                    {// 不在通话中
                        if (isShieldOpen)
                        {
                            endCall();
                            PhoneStateService.this.incomingNumber = incomingNumber;
                            handler.sendEmptyMessageDelayed(1, 4000);// (1);
                            callEnded = true;
                        }
                    }
                    lastState = TelephonyManager.CALL_STATE_RINGING;
                }
                else if (state == TelephonyManager.CALL_STATE_OFFHOOK) // == 2
                {// 根据newOutGoing判断是否是拨出电话
                    Log.i("PhoneStateService", "------in --- CALL_STATE_OFFHOOK isCallOut " + " incomingNumber "
                        + incomingNumber + ",callOutPhoneNum" + callOutPhoneNum);
                    lastState = TelephonyManager.CALL_STATE_OFFHOOK;
                }
                else if (state == TelephonyManager.CALL_STATE_IDLE) // == 0
                {// 挂断电话
                    Log.i("PhoneStateService", "------in --- CALL_STATE_IDLE isCallOut " + " incomingNumber "
                        + incomingNumber);
                    if (lastState != TelephonyManager.CALL_STATE_IDLE) // 拨出被挂断
                    {
                        Log.i("PhoneStateService", "------in --- CALL_STATE_IDLE isCallOut " + " incomingNumber "
                            + incomingNumber);
                        // handler.sendEmptyMessage(2);
                        // handler.sendEmptyMessage(2);
                    }
                }
            }
        }
    }
    
    /**
     * 结束通话
     * 
     * @param incomingNumber
     */
    private void endCall()
    {
        try
        {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder)method.invoke(null, new Object[] {Context.TELEPHONY_SERVICE});
            ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
            iTelephony.endCall();// 结束通话
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * 接听电话
     * 
     * @param incomingNumber
     */
    private void answerCall()
    {
        try
        {
            Method method = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            IBinder binder = (IBinder)method.invoke(null, new Object[] {Context.TELEPHONY_SERVICE});
            ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
            iTelephony.silenceRinger();
            iTelephony.answerRingingCall();// 接听通话
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // 2.3来电接听方法
    public synchronized void answerRingingCall(Context context)
    {// 据说该方法只能用于Android2.3及2.3以上的版本上
        try
        {
            Intent localIntent1 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent1.putExtra("state", 1);
            localIntent1.putExtra("microphone", 1);
            localIntent1.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent1, "android.permission.CALL_PRIVILEGED");
            Intent localIntent2 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent2.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent1);
            context.sendOrderedBroadcast(localIntent2, "android.permission.CALL_PRIVILEGED");
            Intent localIntent3 = new Intent(Intent.ACTION_MEDIA_BUTTON);
            KeyEvent localKeyEvent2 = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK);
            localIntent3.putExtra("android.intent.extra.KEY_EVENT", localKeyEvent2);
            context.sendOrderedBroadcast(localIntent3, "android.permission.CALL_PRIVILEGED");
            Intent localIntent4 = new Intent(Intent.ACTION_HEADSET_PLUG);
            localIntent4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            localIntent4.putExtra("state", 0);
            localIntent4.putExtra("microphone", 1);
            localIntent4.putExtra("name", "Headset");
            context.sendOrderedBroadcast(localIntent4, "android.permission.CALL_PRIVILEGED");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void notifyIncomingCallAndCallBack(Context context)
    {
        // SharedPreferences share = context.getSharedPreferences(CommonConstants.SHARED_NAME, Context.MODE_PRIVATE);
        nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.icon, "有电话进来", System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        CharSequence contentTitle = "贴心课表";
        CharSequence contentText = "来电:" + getContactName(incomingNumber) + "(" + incomingNumber + ")," + "点击回复电话";
        Intent notificationIntent = new Intent();
        notificationIntent.setAction(Intent.ACTION_CALL);
        notificationIntent.setData(Uri.parse("tel:" + incomingNumber));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        nm.notify(((SyllabusApplication)getApplication()).notificationID, notification);
        ((SyllabusApplication)getApplication()).notificationID++;
    }
    
    private String getContactName(String incomingNumber)
    {
        String[] projection =
            {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = this.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, // Which
                                                                                                                        // columns
                                                                                                                        // to
                                                                                                                        // return.
            ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + incomingNumber + "'", // WHERE clause.
            null, // WHERE clause value substitution
            null); // Sort order.
        if (cursor == null)
        {
            return "";
        }
        cursor.moveToPosition(0);
        
        // 取得联系人名字
        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
        String name = cursor.getString(nameFieldColumnIndex);
        // Log.i("Contacts", "" + name + " .... " + nameFieldColumnIndex); // 这里提示 force close
        return name;
    }
    
    class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    SmsManager manager = SmsManager.getDefault();
                    // PendingIntent intent1=PendingIntent.getBroadcast(MyShowActivity.this, 0, new Intent(), 0);
                    manager.sendTextMessage(incomingNumber, null, "我正在上课，请稍候联系。或者下课后等我给您回电话。", null, null);
                    Log.i("PhoneStateService", "message has been sent to " + incomingNumber);
                    notifyIncomingCallAndCallBack(PhoneStateService.this);
                    
                    break;
                case 2:
                    Log.i("PhoneStateService", "service self stopped");
                    if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT)
                    {
                        mAudioManager.setRingerMode(CommonConstants.defaultRingerMode);
                        Log.i("PhoneStateService", "RINGER MODE has been restored");
                    }
                    telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
                    PhoneStateService.this.stopSelf();
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    
}
