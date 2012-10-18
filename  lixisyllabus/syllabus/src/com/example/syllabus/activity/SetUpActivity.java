package com.example.syllabus.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.utils.CommonConstants;
import com.example.syllabus.utils.LogUtil;

/**
 * 设置学期第几周，和上课下课时间，还有屏蔽电话开关
 * 
 * @author Administrator
 * 
 */
public class SetUpActivity extends Activity implements OnClickListener
{
    private static final String LOGTAG = LogUtil.makeLogTag(OneWeekCourseListActivity.class);
    
    private static final String SELECTED = "selected";
    
    private static final int SET_END_TIME = 1;
    
    private static final int SET_START_TIME = 2;
    
    private TextView tvWeekNum;
    
    private int weekOfSemister;
    
    /**
     * 
     */
    private LinearLayout llFirstClass;
    
    private TextView tvFirstClassStartTime;
    
    private TextView tvFirstClassEndTime;
    
    /**
     * 
     */
    private LinearLayout llSecondClass;
    
    private TextView tvSecondClassStartTime;
    
    private TextView tvSecondClassEndTime;
    
    /**
     * 
     */
    private LinearLayout llThirdClass;
    
    private TextView tvThirdClassStartTime;
    
    private TextView tvThirdClassEndTime;
    
    /**
     * 
     */
    private LinearLayout llFourthClass;
    
    private TextView tvFourthClassStartTime;
    
    private TextView tvFourthClassEndTime;
    
    /**
     * 
     */
    private LinearLayout llFifthClass;
    
    private TextView tvFifthClassStartTime;
    
    private TextView tvFifthClassEndTime;
    
    /**
     * 
     */
    private LinearLayout llSixthClass;
    
    private TextView tvSixthClassStartTime;
    
    private TextView tvSixthClassEndTime;
    
    private LinearLayout[] linearLayouts = {llFirstClass, llSecondClass, llThirdClass, llFourthClass, llFifthClass,
        llSixthClass};
    
    private TextView[] tViewsStartTime = {tvFirstClassStartTime, tvSecondClassStartTime, tvThirdClassStartTime,
        tvFourthClassStartTime, tvFifthClassStartTime, tvSixthClassStartTime};
    
    private TextView[] tViewsEndTime = {tvFirstClassEndTime, tvSecondClassEndTime, tvThirdClassEndTime,
        tvFourthClassEndTime, tvFifthClassEndTime, tvSixthClassEndTime};
    
    private int[] llviewResources = {R.id.firstclass, R.id.secondclass, R.id.thirdclass, R.id.fourthclass,
        R.id.fifthclass, R.id.sixthclass};
    
    private int[] tvViewResourcesStart = {R.id.firstclassstart, R.id.secondclassstart, R.id.thirdclassstart,
        R.id.fourthclassstart, R.id.fifthclassstart, R.id.sixthclassstart};
    
    private int[] tvViewResourcesEnd = {R.id.firstclassend, R.id.secondclassend, R.id.thirdclassend,
        R.id.fourthclassend, R.id.fifthclassend, R.id.sixthclassend};
    
    private LinearLayout llCurrentWeek;
    
    private SharedPreferences preference;
    
    private TextView tvTitle;
    
    private TextView tvLeft;
    
    private TextView tvRightT;
    
    private List<TextView> tvStartTimeList;
    
    private List<TextView> tvEndTimeList;
    
    private TextView tvShieldOnOff;
    
    private ImageView ivShieldChecked;
    
    private EditText etSmsText;
    
    private LinearLayout llShield;
    
    private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupactivity);
        
        SyllabusApplication.getInstance().addActivity(this);
        preference = CommonConstants.getMyPreferences(this);
        
        tvStartTimeList = new ArrayList<TextView>();
        tvEndTimeList = new ArrayList<TextView>();
        
        initViews();
        
        initData();
        
        if (!preference.getBoolean(CommonConstants.IS_SETUP_ALREADY, false))
        {
            Toast.makeText(this, "请先设置周数及上下课时间。", Toast.LENGTH_LONG).show();
        }
    }
    
    private void initViews()
    {
        tvWeekNum = (TextView)findViewById(R.id.weeknumtv);
        weekOfSemister = preference.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        String weekNum = weekOfSemister + "";
        tvWeekNum.setText(weekNum);
        tvWeekNum.setOnClickListener(this);
        
        llCurrentWeek = (LinearLayout)findViewById(R.id.currentweek);
        llCurrentWeek.setOnClickListener(this);
        
        for (int i = 0; i < linearLayouts.length; i++)
        {
            linearLayouts[i] = (LinearLayout)findViewById(llviewResources[i]);
            linearLayouts[i].setOnClickListener(this);
            
            tViewsStartTime[i] = (TextView)findViewById(tvViewResourcesStart[i]);
            tViewsEndTime[i] = (TextView)findViewById(tvViewResourcesEnd[i]);
            
            tViewsStartTime[i].setText(preference.getString(CommonConstants.STR_START_TIME[i],
                CommonConstants.STARTTIME[i]));
            tViewsEndTime[i].setText(preference.getString(CommonConstants.STR_END_TIME[i], CommonConstants.ENDTIME[i]));
            
            tViewsStartTime[i].setOnClickListener(this);
            tViewsEndTime[i].setOnClickListener(this);
            
            tvStartTimeList.add(tViewsStartTime[i]);
            tvEndTimeList.add(tViewsEndTime[i]);
            
        }
        
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("全局设置");
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setText("完成");
        tvRightT.setOnClickListener(this);
        
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("返回");
        tvLeft.setOnClickListener(this);
        
        tvShieldOnOff = (TextView)findViewById(R.id.shieldonoff);
        ivShieldChecked = (ImageView)findViewById(R.id.shield);
        
        if (preference.getBoolean(CommonConstants.SHIELDED, false))
        {
            tvShieldOnOff.setText("点击关闭上课来电屏蔽");
            ivShieldChecked.setImageResource(R.drawable.selected);// )
            ivShieldChecked.setTag(SELECTED);
        }
        else
        {
            tvShieldOnOff.setText("点击打开上课来电屏蔽");
            ivShieldChecked.setImageResource(R.drawable.checkbox);
            ivShieldChecked.setTag(null);
        }
        
        tvShieldOnOff.setOnClickListener(this);
        ivShieldChecked.setOnClickListener(this);
        
        llShield = (LinearLayout)findViewById(R.id.llshield);
        llShield.setOnClickListener(this);
        
        etSmsText = (EditText)findViewById(R.id.smstext);
        if (null == preference.getString(CommonConstants.SMS_TEXT, null)
            || "".equals(preference.getString(CommonConstants.SMS_TEXT, null)))
        {
            etSmsText.setText((CommonConstants.DEFAULT_SMSTEXT));
        }
        else
        {
            etSmsText.setText(preference.getString(CommonConstants.SMS_TEXT, null));
        }
        if (null == ivShieldChecked.getTag())
        {
            etSmsText.setEnabled(false);
        }
        else
        {
            etSmsText.setEnabled(true);
        }
        
    }
    
    private void initData()
    {
        handler = new MyHandler();
    }
    
    public void onClick(View view)
    {
        int courseIndex;
        switch (view.getId())
        {
            case R.id.tvRightT:
                Editor editor = preference.edit();
                for (int i = 0; i < tViewsStartTime.length; i++)
                {
                    editor.putString(CommonConstants.STR_START_TIME[i], tViewsStartTime[i].getText().toString());
                }
                for (int i = 0; i < tViewsEndTime.length; i++)
                {
                    editor.putString(CommonConstants.STR_END_TIME[i], tViewsEndTime[i].getText().toString());
                }
                
                // 如果屏蔽打开，那么需要保存短信内容，如果短信内容为空，那么保存默认内容
                if (null != ivShieldChecked.getTag())
                {
                    editor.putBoolean(CommonConstants.SHIELDED, true);
                    if (null == etSmsText.getText() || "".equals(etSmsText.getText().toString()))
                    {
                        editor.putString(CommonConstants.SMS_TEXT, CommonConstants.DEFAULT_SMSTEXT);
                    }
                    editor.putString(CommonConstants.SMS_TEXT, etSmsText.getText().toString());
                }
                else
                {
                    editor.putBoolean(CommonConstants.SHIELDED, false);
                }
                
                // 保存当前周次及当前星期数（在一年中的数目），每次程序启动，都要更新
                int weekInYear = CommonConstants.getCurrentWeekInYear();
                if (weekOfSemister != preference.getInt(CommonConstants.WEEKOFSEMISTER,
                    CommonConstants.DEFAULT_WEEKOFSEMISTER))
                {
                    ((SyllabusApplication)getApplication()).isWeekHasBeenChanged = true;
                }
                editor.putInt(CommonConstants.WEEKOFSEMISTER, weekOfSemister);
                editor.putInt(CommonConstants.WEEK_IN_YEAR, weekInYear);
                editor.putBoolean(CommonConstants.IS_SETUP_ALREADY, true);
                editor.commit();
                if (!getIntent().getBooleanExtra("fromMainActivity", false))
                {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                }
                this.finish();
                break;
            case R.id.tvLeft:
                this.finish();
                break;
            case R.id.weeknumtv:
            case R.id.currentweek:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请选择本周周次");
                builder.setItems(CommonConstants.WEEKOFSEMISTER_INNUMBER, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        tvWeekNum.setText(CommonConstants.WEEKOFSEMISTER_INNUMBER[which]);
                        weekOfSemister = which;
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                
                break;
            case R.id.firstclass:
            case R.id.firstclassstart:
                courseIndex = 1;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.firstclassend:
                courseIndex = 1;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.secondclass:
            case R.id.secondclassstart:
                courseIndex = 2;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.secondclassend:
                courseIndex = 2;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.thirdclass:
            case R.id.thirdclassstart:
                courseIndex = 3;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.thirdclassend:
                courseIndex = 3;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.fourthclass:
            case R.id.fourthclassstart:
                courseIndex = 4;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.fourthclassend:
                courseIndex = 4;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.fifthclassstart:
                courseIndex = 5;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.fifthclass:
            case R.id.fifthclassend:
                courseIndex = 5;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.sixthclass:
            case R.id.sixthclassstart:
                courseIndex = 6;
                showTimePickerDialog(courseIndex, SET_START_TIME);
                break;
            case R.id.sixthclassend:
                courseIndex = 6;
                showTimePickerDialog(courseIndex, SET_END_TIME);
                break;
            case R.id.llshield:
            case R.id.shieldonoff:
            case R.id.shield:
                // 屏蔽是否被打开，在于ImageView的Tag是否被设置为SELECTED。
                if (null != ivShieldChecked.getTag())
                {
                    tvShieldOnOff.setText("点击打开上课来电屏蔽");
                    ivShieldChecked.setImageResource(R.drawable.checkbox);
                    ivShieldChecked.setTag(null);
                    etSmsText.setEnabled(false);
                }
                else
                {
                    tvShieldOnOff.setText("点击关闭上课来电屏蔽");
                    ivShieldChecked.setImageResource(R.drawable.selected);// )
                    ivShieldChecked.setTag(SELECTED);
                    etSmsText.setEnabled(true);
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 
     */
    private void showTimePickerDialog(int courseIndex, int END_OR_START)
    {
        Message message;
        message = Message.obtain();
        message.arg1 = courseIndex;
        message.what = END_OR_START;
        handler.sendMessage(message);
    }
    
    class MyTimePickerListener implements TimePickerDialog.OnTimeSetListener
    {
        private int courseIndex;
        
        private boolean isStart;
        
        public MyTimePickerListener(int courseIndex, boolean isStart)
        {
            this.courseIndex = courseIndex;
            this.isStart = isStart;
        }
        
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {
            String hour = Integer.toString(hourOfDay);
            StringBuilder sb = new StringBuilder(hour);
            if (1 == hour.length())
            {
                sb.insert(0, "0");
            }
            
            sb.append(":");
            if (1 == Integer.toString(minute).length())
            {
                sb.append("0");
            }
            sb.append(Integer.toString(minute));
            
            if (isStart)
            {
                tvStartTimeList.get(courseIndex - 1).setText(sb.toString());
                Message message = Message.obtain();
                message.arg1 = courseIndex;
                message.what = 1;
                handler.sendMessage(message);
            }
            else
            {
                tvEndTimeList.get(courseIndex - 1).setText(sb.toString());
            }
        }
        
    }
    
    public class MyHandler extends Handler
    {
        
        @Override
        public void handleMessage(Message msg)
        {
            MyTimePickerListener callBack;
            int courseIndex;
            int hourOfDay;
            int minute;
            String[] time;
            TimePickerDialog dialog;
            switch (msg.what)
            {
                case SET_END_TIME:
                    courseIndex = msg.arg1;
                    callBack = new MyTimePickerListener(courseIndex, false);
                    time =
                        preference.getString(CommonConstants.STR_END_TIME[courseIndex - 1],
                            CommonConstants.ENDTIME[courseIndex - 1]).split(":");
                    hourOfDay = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                    Log.i(LOGTAG, "hour of Day is " + hourOfDay + ", and minute is " + minute);
                    dialog = new TimePickerDialog(SetUpActivity.this, callBack, hourOfDay, minute, true);
                    dialog.setTitle("请设置课程结束时间");
                    dialog.show();
                    break;
                case SET_START_TIME:
                    courseIndex = msg.arg1;
                    callBack = new MyTimePickerListener(courseIndex, true);
                    time =
                        preference.getString(CommonConstants.STR_START_TIME[courseIndex - 1],
                            CommonConstants.STARTTIME[courseIndex - 1]).split(":");
                    hourOfDay = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                    Log.i(LOGTAG, "hour of Day is " + hourOfDay + ", and minute is " + minute);
                    dialog = new TimePickerDialog(SetUpActivity.this, callBack, hourOfDay, minute, true);
                    dialog.setTitle("请设置课程开始时间");
                    dialog.show();
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }
    
}
