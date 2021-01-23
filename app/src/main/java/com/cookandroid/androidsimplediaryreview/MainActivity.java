package com.cookandroid.androidsimplediaryreview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //객체들 정의
    String fileName;
    DatePicker datePicker;
    EditText editDiary;
    Button btnWrite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 다이어리");
        datePicker = findViewById(R.id.datePicker);
        editDiary = findViewById(R.id.editDiary);
        btnWrite = findViewById(R.id.btnWrite);
        //날짜 초기화
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                fileName = year + "_" + (monthOfYear + 1) + "_" + dayOfMonth + ".txt";
                Log.e("fileName", fileName);

                String str = fileRead(fileName);
                editDiary.setText(str);
                btnWrite.setEnabled(true);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    String str = editDiary.getText().toString();

                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.close();
                    Toast.makeText(getApplicationContext(), fileName + " 가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String fileRead(String fileName) {

        String diaryStr = null;

        try {
            //일기가 있을 때
            FileInputStream fileInputStream = openFileInput(fileName);
            byte[] txt = new byte[500];
            int length = fileInputStream.read(txt);
            Log.e("file length", String.valueOf(length));

            fileInputStream.close();
            diaryStr = new String(txt).trim();
            btnWrite.setText("수정하기");
        } catch (IOException e) {
            //일기가 없을 때 예외 처리
            editDiary.setHint("일기 없음");
            btnWrite.setText("새로 저장");
        }
        return diaryStr;
    }
}