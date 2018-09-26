package com.austgl.zxing;

import com.austgl.syllabus.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QcodeResult extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qcoderesult);
        String intent=getIntent().getStringExtra("code");
        TextView txt=(TextView)findViewById(R.id.txtcode);
        txt.setText(intent);
        
    }
  
  
}
