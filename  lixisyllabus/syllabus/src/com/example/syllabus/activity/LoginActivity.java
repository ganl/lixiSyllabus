package com.example.syllabus.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syllabus.R;
import com.example.syllabus.utils.CommonConstants;

/**
 * ��½���棬��������������֮��Ͳ������ڿα�
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends Activity implements OnClickListener
{
    
    private EditText etDepartmentName; // ѧԺ��
    
    private EditText etGradeNum; // ����
    
    private EditText etClassName; // �༶��
    
    private EditText etMajorName; // רҵ��
    
    private EditText etUniversityName; // ѧУ��
    
    private TextView tvLogin;
    
    private TextView tvLeft;
    
    private TextView tvTitle;
    
    private TextView tvRightT;
    
    private String departmentName;
    
    private String gradeNum;
    
    private String className;
    
    private String universityName;
    
    private String majorName;
    
    private SharedPreferences preferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        preferences = CommonConstants.getMyPreferences(this);
        
        initViews();
    }
    
    private void initViews()
    {
        tvLeft = (TextView)findViewById(R.id.tvLeft);
        tvLeft.setText("����");
        tvLeft.setOnClickListener(this);
        
        tvRightT = (TextView)findViewById(R.id.tvRightT);
        tvRightT.setOnClickListener(this);
        tvRightT.setText("����");
        
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("��¼");
        
        tvLogin = (TextView)findViewById(R.id.login);
        tvLogin.setOnClickListener(this);
        
        etDepartmentName = (EditText)findViewById(R.id.departmentname);
        etClassName = (EditText)findViewById(R.id.class_name);
        etGradeNum = (EditText)findViewById(R.id.gradenum);
        etUniversityName = (EditText)findViewById(R.id.universityname);
        etMajorName = (EditText)findViewById(R.id.majorname);
        
        // for test
        etDepartmentName.setText("�������ѧ�빤��ѧԺ");
        etUniversityName.setText("��������ѧ");
        etGradeNum.setText("2011");
        etMajorName.setText("�����Ӧ�ü���");
        etClassName.setText("1");
        
    }
    
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.tvLeft:
                intent = new Intent();
                // intent.setClass(this, MainActivity.class);
                // startActivity(intent);
                Log.i("LoginActivity", "tiaoguo ");
                // this.finish();
                break;
            case R.id.tvRightT:
                intent = new Intent();
                intent.setClass(this, SetUpActivity.class);
                // intent.putExtra("skipped", true);
                Editor editor2 = preferences.edit();
                editor2.putBoolean(CommonConstants.SKIPPED, true);
                editor2.commit();
                Log.i("LoginActivity", "tiaoguo ");
                startActivity(intent);
                this.finish();
                break;
            
            case R.id.login:
                departmentName = etDepartmentName.getText().toString();
                className = etClassName.getText().toString();
                gradeNum = etGradeNum.getText().toString();
                universityName = etUniversityName.getText().toString();
                majorName = etMajorName.getText().toString();
                if (null != departmentName && !"".equals(departmentName) && null != className && !"".equals(className)
                    && null != gradeNum && !"".equals(gradeNum) && null != universityName && !"".equals(universityName)
                    && null != majorName && !"".equals(majorName))
                {
                    Editor editor = preferences.edit();
                    editor.putString(CommonConstants.UNIVERSITY_NAME, universityName);
                    editor.putString(CommonConstants.DEPARTMENT_NAME, departmentName);
                    editor.putString(CommonConstants.GRADE_NUM, gradeNum);
                    editor.putString(CommonConstants.CLASS_NAME, className);
                    editor.putString(CommonConstants.MAJOR_NAME, majorName);
                    editor.putBoolean(CommonConstants.LOGINED, true);
                    editor.commit();
                    
                    intent = new Intent(this, SetUpActivity.class);
                    // intent.putExtra(CommonConstants.DEPARTMENT_NAME, departmentName);
                    // intent.putExtra(CommonConstants.GRADE_NUM, gradeNum);
                    // intent.putExtra(CommonConstants.CLASS_NAME, className);
                    // intent.putExtra(CommonConstants.UNIVERSITY_NAME, universityName);
                    // intent.putExtra(CommonConstants.MAJOR_NAME, majorName);
                    startActivity(intent);
                    this.finish();
                }
                else if (null == universityName || "".equals(universityName))
                {
                    Toast.makeText(this, "ѧУ������Ϊ��", Toast.LENGTH_SHORT);
                }
                else if (null == majorName || "".equals(majorName))
                {
                    Toast.makeText(this, "רҵ������Ϊ��", Toast.LENGTH_SHORT);
                }
                else if (null == departmentName || "".equals(departmentName))
                {
                    Toast.makeText(this, "ѧԺ������Ϊ��", Toast.LENGTH_SHORT).show();
                }
                else if (null == className || "".equals(className))
                {
                    Toast.makeText(this, "�༶����Ϊ�գ���ֻ��һ���༶������1", Toast.LENGTH_SHORT).show();
                }
                else if (null == gradeNum || "".equals(gradeNum))
                {
                    Toast.makeText(this, "�꼶����Ϊ��", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }
}
