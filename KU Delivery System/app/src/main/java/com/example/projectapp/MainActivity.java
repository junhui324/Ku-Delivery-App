package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 배송 예약 버튼 눌렀을 때
        Button btn_reservation = findViewById(R.id.btn_main_reservation);
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "배송 예약화면으로 이동합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Reservation.class);
                startActivity(intent);
            }
        });

        // 배송 조회 버튼 눌렀을 때
        Button btn_search = findViewById(R.id.btn_main_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"배송 조회를 시작합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),Tracking.class);
                startActivity(intent);
            }
        });

        // 예약 취소 버튼 눌렀을 때
        Button btn_delete = findViewById(R.id.btn_main_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "예약 취소화면으로 이동합니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Cancel.class);
                startActivity(intent);
            }
        });




    }
}