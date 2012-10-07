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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.syllabus.R;
import com.example.syllabus.SyllabusApplication;
import com.example.syllabus.utils.CommonConstants;

/**
 * 设置学期第几周，和上课下课时间，还有屏蔽电话开关
 * 
 * @author Administrator
 * 
 */
public class SetUpActivity extends Activity implements OnClickListener
{
    
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
    
    private LinearLayout llCurrentWeek;
    
    private SharedPreferences preference;
    
    private TextView tvTitle;
    
    private TextView tvLeft;
    
    private TextView tvRightT;
    
    private List<TextView> tvStartTimeList;
    
    private List<TextView> tvEndTimeList;
    
    private TextView tvShieldOnOff;
    
    private ImageView ivShieldChecked;
    
    private LinearLayout llShield;
    
    private Handler handler;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setupactivity);
        
        preference = CommonConstants.getMyPreferences(this);
        tvStartTimeList = new ArrayList<TextView>();
        tvEndTimeList = new ArrayList<TextView>();
        
        initViews();
        
        initData();
    }
    
    private void initViews()
    {
        tvWeekNum = (TextView)findViewById(R.id.weeknumtv);
        weekOfSemister = preference.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKOFSEMISTER);
        String weekNum = weekOfSemister + "";
        // Integer.toString(preference.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKNUM));
        // System.out.println(preference.getInt(CommonConstants.WEEKOFSEMISTER, CommonConstants.DEFAULT_WEEKNUM));
        tvWeekNum.setText(weekNum);
        tvWeekNum.setOnClickListener(this);
        
        llCurrentWeek = (LinearLayout)findViewById(R.id.currentweek);
        llCurrentWeek.setOnClickListener(this);
        
        tvFirstClassStartTime = (TextView)findViewById(R.id.firstclassstart);
        tvStartTimeList.add(tvFirstClassStartTime);
        tvFirstClassStartTime.setText(preference.getString(CommonConstants.FIRST_CLASS_START,
            CommonConstants.FIRST_CLASS_START_TIME));
        tvFirstClassEndTime = (TextView)findViewById(R.id.firstclassend);
        tvEndTimeList.add(tvFirstClassEndTime);
        tvFirstClassEndTime.setText(preference.getString(CommonConstants.FIRST_CLASS_END,
            CommonConstants.FIRST_CLASS_END_TIME));
        
        tvSecondClassStartTime = (TextView)findViewById(R.id.secondclassstart);
        tvStartTimeList.add(tvSecondClassStartTime);
        tvSecondClassStartTime.setText(preference.getString(CommonConstants.SECOND_CLASS_START,
            CommonConstants.SECOND_CLASS_START_TIME));
        tvSecondClassEndTime = (TextView)findViewById(R.id.secondclassend);
        tvEndTimeList.add(tvSecondClassEndTime);
        tvSecondClassEndTime.setText(preference.getString(CommonConstants.SECOND_CLASS_END,
            CommonConstants.SECOND_CLASS_END_TIME));
        
        tvThirdClassStartTime = (TextView)findViewById(R.id.thirdclassstart);
        tvStartTimeList.add(tvThirdClassStartTime);
        tvThirdClassStartTime.setText(preference.getString(CommonConstants.THIRD_CLASS_START,
            CommonConstants.THIRD_CLASS_START_TIME));
        tvThirdClassEndTime = (TextView)findViewById(R.id.thirdclassend);
        tvEndTimeList.add(tvThirdClassEndTime);
        tvThirdClassEndTime.setText(preference.getString(CommonConstants.THIRD_CLASS_END,
            CommonConstants.THIRD_CLASS_END_TIME));
        
        tvFourthClassStartTime = (TextView)findViewById(R.id.fourthclassstart);
        tvStartTimeList.add(tvFourthClassStartTime);
        tvFourthClassStartTime.setText(preference.getString(CommonConstants.FOURTH_CLASS_START,
            CommonConstants.FOURTH_CLASS_START_TIME));
        tvFourthClassEndTime = (TextView)findViewById(R.id.fourthclassend);
        tvEndTimeList.add(tvFourthClassEndTime);
        tvFourthClassEndTime.setText(preference.getString(CommonConstants.FOURTH_CLASS_END,
            CommonConstants.FOURTH_CLASS_END_TIME));
        
        tvFifthClassStartTime = (TextView)findViewById(R.id.fifthclassstart);
        tvStartTimeList.add(tvFifthClassStartTime);
        tvFifthClassStartTime.setText(preference.getString(CommonConstants.FIFTH_CLASS_START,
            CommonConstants.FIFTH_CLASS_START_TIME));
        tvFifthClassEndTime = (TextView)findViewById(R.id.fifthclassend);
        tvEndTimeList.add(tvFifthClassEndTime);
        tvFifthClassEndTime.setText(preference.getString(CommonConstants.FIFTH_CLASS_END,
            CommonConstants.FIFTH_CLASS_END_TIME));
        
        tvSixthClassStartTime = (TextView)findViewById(R.id.sixthclassstart);
        tvStartTimeList.add(tvSixthClassStartTime);
        tvSixthClassStartTime.setText(preference.getString(CommonConstants.SIXTH_CLASS_START,
            CommonConstants.SIXTH_CLASS_START_TIME));
        
        tvSixthClassEndTime = (TextView)findViewById(R.id.sixthclassend);
        tvEndTimeList.add(tvSixthClassEndTime);
        tvSixthClassEndTime.setText(preference.getString(CommonConstants.SIXTH_CLASS_END,
            CommonConstants.SIXTH_CLASS_END_TIME));
        
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("全局设置");
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setText("完成");
        tvRightT.setOnClickListener(this);
        
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("返回");
        tvLeft.setOnClickListener(this);
        
        llFirstClass = (LinearLayout)findViewById(R.id.firstclass);
        llSecondClass = (LinearLayout)findViewById(R.id.secondclass);
        llThirdClass = (LinearLayout)findViewById(R.id.thirdclass);
        llFourthClass = (LinearLayout)findViewById(R.id.fourthclass);
        llFifthClass = (LinearLayout)findViewById(R.id.fifthclass);
        llSixthClass = (LinearLayout)findViewById(R.id.sixthclass);
        
        llFirstClass.setOnClickListener(this);
        llSecondClass.setOnClickListener(this);
        llThirdClass.setOnClickListener(this);
        llFourthClass.setOnClickListener(this);
        llFifthClass.setOnClickListener(this);
        llSixthClass.setOnClickListener(this);
        
        tvFirstClassEndTime.setOnClickListener(this);
        tvSecondClassEndTime.setOnClickListener(this);
        tvThirdClassEndTime.setOnClickListener(this);
        tvFourthClassEndTime.setOnClickListener(this);
        tvFifthClassEndTime.setOnClickListener(this);
        tvSixthClassEndTime.setOnClickListener(this);
        
        tvFirstClassStartTime.setOnClickListener(this);
        tvSecondClassStartTime.setOnClickListener(this);
        tvFourthClassStartTime.setOnClickListener(this);
        tvThirdClassStartTime.setOnClickListener(this);
        tvFifthClassStartTime.setOnClickListener(this);
        tvSixthClassStartTime.setOnClickListener(this);
        
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
                for (int i = 0; i < tvStartTimeList.size(); i++)
                {
                    // Log.i("SetUpActivity", CommonConstants.STR_START_TIME[i] + " = "
                    // + tvStartTimeList.get(i).getText().toString());
                    editor.putString(CommonConstants.STR_START_TIME[i], tvStartTimeList.get(i).getText().toString());
                }
                for (int i = 0; i < tvEndTimeList.size(); i++)
                {
                    // Log.i("SetUpActivity", CommonConstants.STR_END_TIME[i] + " = "
                    // + tvEndTimeList.get(i).getText().toString());
                    editor.putString(CommonConstants.STR_END_TIME[i], tvEndTimeList.get(i).getText().toString());
                }
                
                if (null != ivShieldChecked.getTag())
                {
                    editor.putBoolean(CommonConstants.SHIELDED, true);
                }
                else
                {
                    editor.putBoolean(CommonConstants.SHIELDED, false);
                }
                
                int weekInYear = CommonConstants.getCurrentWeekInYear();
                if (weekOfSemister != preference.getInt(CommonConstants.WEEKOFSEMISTER,
                    CommonConstants.DEFAULT_WEEKOFSEMISTER))
                {
                    ((SyllabusApplication)getApplication()).isWeekHasBeenChanged = true;
                }
                System.out.println("weekOfSemister:" + weekOfSemister);
                editor.putInt(CommonConstants.WEEKOFSEMISTER, weekOfSemister);
                editor.putInt(CommonConstants.WEEK_IN_YEAR, weekInYear);
                editor.putBoolean(CommonConstants.IS_SETUP_ALREADY, true);
                editor.commit();
                
                Intent intent = new Intent();
                intent.setClass(this, OneWeekCourseListActivity.class);
                startActivity(intent);
                
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
                Log.i("SetUpActivity", "first class activated: firstclass");
            case R.id.firstclassstart:
                Log.i("SetUpActivity", "first class activated: firstclassstart");
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
                if (null != ivShieldChecked.getTag())
                {
                    tvShieldOnOff.setText("点击打开上课来电屏蔽");
                    ivShieldChecked.setImageResource(R.drawable.checkbox);
                    ivShieldChecked.setTag(null);
                }
                else
                {
                    tvShieldOnOff.setText("点击关闭上课来电屏蔽");
                    ivShieldChecked.setImageResource(R.drawable.selected);// )
                    ivShieldChecked.setTag(SELECTED);
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
                    Log.i("SetUpActivity", "message received : set end time");
                    courseIndex = msg.arg1;
                    callBack = new MyTimePickerListener(courseIndex, false);
                    time =
                        preference.getString(CommonConstants.STR_END_TIME[courseIndex - 1],
                            CommonConstants.ENDTIME[courseIndex - 1]).split(":");
                    hourOfDay = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                    Log.i("SetUpActivity", "hour of Day is " + hourOfDay + ", and minute is " + minute);
                    dialog = new TimePickerDialog(SetUpActivity.this, callBack, hourOfDay, minute, true);
                    dialog.setTitle("请设置课程结束时间");
                    dialog.show();
                    break;
                case SET_START_TIME:
                    Log.i("SetUpActivity", "message received : set end time");
                    courseIndex = msg.arg1;
                    callBack = new MyTimePickerListener(courseIndex, true);
                    time =
                        preference.getString(CommonConstants.STR_START_TIME[courseIndex - 1],
                            CommonConstants.STARTTIME[courseIndex - 1]).split(":");
                    hourOfDay = Integer.parseInt(time[0]);
                    minute = Integer.parseInt(time[1]);
                    Log.i("SetUpActivity", "hour of Day is " + hourOfDay + ", and minute is " + minute);
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
