package com.example.blue_tooth4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_layout extends AppCompatActivity {
    TextView textView1;
    Button button1;
    //String[] names = title.split(" ");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

//        //setText via java file
//        textView1=findViewById(R.id.textview);
//        //textView1.setText("");

        //button
        button1=findViewById(R.id.bt);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this,
//                        "Bạn vừa nhấn vào bt1",
//                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity_layout.this,MainActivity.class);
                startActivity(i);
                //finish(); neu khong muon quay lai activity truoc do bang nut back


            }
        });



    }


}