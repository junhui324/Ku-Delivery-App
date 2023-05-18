package com.example.projectapp;

import static com.example.projectapp.Cons.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.projectapp.retrofit.Data;
import com.example.projectapp.retrofit.ReservationData;
import com.example.projectapp.retrofit.serviceAPI;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Reservation extends AppCompatActivity{

    ////// 변수 선언 ////////////////////////

    // 보내는 건물 변수 //
    RadioGroup rg_send_loc;

    String send_loc;
    String loc_send;
    // 보내는 건물 변수 //


    // 받는 건물 변수 //
    RadioGroup rg_rec_loc;

    String rec_loc;
    String loc_rec;
    // 받는 건물 변수 //


    // 날짜 및 시간 입력받는 변수 선언
    TextView tv_date;
    TextView tv_time;

    DatePickerDialog.OnDateSetListener callbackMethod;
    TimePickerDialog.OnTimeSetListener callbackMethod2;

    String rv_year, rv_month, rv_day, rv_hour, rv_minute;
    // 날짜 및 시간 입력받는 변수 선언



    // 보내는 사람 이름과 학번, 받는 사람 이름 입력 //
    TextInputEditText et_send_name;
    TextInputEditText et_rec_name;
    TextInputEditText et_send_id;
    TextInputEditText et_type;
    // 보내는 사람 이름과 학번, 받는 사람 이름, 물건 종류 입력 //


    String st_id;
    String send_name;
    String rec_name;
    String type;


    Button btn_reservation; // 예약하기 버튼

    /////// 변수 선언 //////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        rg_send_loc = findViewById(R.id.rg_send_loc);
        rg_rec_loc = findViewById(R.id.rg_rec_loc);


        // 메서드 실행
        this.InitializeView_date();
        this.InitializeListener_date();
        this.InitializeView_time();
        this.InitializeListener_time();
        // 메서드 실행

        ////////////// 입력 값 받아오기 /////////////
        et_send_name = findViewById(R.id.et_send_name);
        et_rec_name = findViewById(R.id.et_rec_name);
        et_send_id =  findViewById(R.id.et_send_id);
        et_type = findViewById(R.id.et_type);
        ////////////// 입력 값 받아오기 /////////////



        ///////// 보내는 건몰 Radio Group /////////
        rg_send_loc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_send_loc_dormitory:
                        send_loc = "기숙사";
                        loc_send = "dormitory";
                        break;
                    case R.id.rb_send_loc_library:
                        send_loc = "도서관";
                        loc_send = "library";
                        break;
                    case R.id.rb_send_loc_nongsim:
                        send_loc = "농심관";
                        loc_send = "nongsim";
                        break;
                    case R.id.rb_send_loc_union:
                        send_loc = "학생회관";
                        loc_send = "student_union";
                        break;
                }
            }
        });
        ///////// 보내는 건몰 Radio Group /////////



        ///////// 받는 건몰 Radio Group /////////
        rg_rec_loc.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_rec_loc_dormitory:
                        rec_loc = "기숙사";
                        loc_rec = "dormitory";
                        break;
                    case R.id.rb_rec_loc_library:
                        rec_loc = "도서관";
                        loc_rec = "library";
                        break;
                    case R.id.rb_rec_loc_nongsim:
                        rec_loc = "농심관";
                        loc_rec = "nongsim";
                        break;
                    case R.id.rb_rec_loc_union:
                        rec_loc = "학생회관";
                        loc_rec = "student_union";
                        break;
                }
            }
        });
        ///////// 받는 건몰 Radio Group /////////



        /////////////////////// 예약하기 버튼 누르면 DB에 정보 입력 ///////////////////////
        btn_reservation = findViewById(R.id.btn_reservation);

        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_name = et_send_name.getText().toString();
                rec_name = et_rec_name.getText().toString();
                st_id = et_send_id.getText().toString();
                type = et_type.getText().toString();
                /// send_loc, rec_loc는 전역변수 ///


                String time_info = rv_year + "." + rv_month + "." + rv_day + "." + rv_hour + "." + rv_minute;

                //////////// 비어있는 입력 값 체크하기 ////////////
                if(send_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "보내는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_send_name.requestFocus();
                    return;
                }

                if(rec_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "받는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_rec_name.requestFocus();
                    return;
                }

                if(st_id.length() == 0) {
                    Toast.makeText(getApplicationContext(), "보내는 분 학번을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_send_id.requestFocus();
                    return;
                }

                if(type.length() == 0) {
                    Toast.makeText(getApplicationContext(), "보내는 물건을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_type.requestFocus();
                    return;
                }

                if(send_loc == null) {
                    Toast.makeText(getApplicationContext(), "보내는 건물을 입력하세요", Toast.LENGTH_SHORT).show();
                    rg_send_loc.requestFocus();
                    return;
                }

                if(rec_loc == null) {
                    Toast.makeText(getApplicationContext(), "도착하는 건물을 입력하세요", Toast.LENGTH_SHORT).show();
                    rg_rec_loc.requestFocus();
                    return;
                }

                if(rv_day == null) {
                    Toast.makeText(getApplicationContext(), "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                    tv_date.requestFocus();
                    return;
                }

                if(rv_hour == null) {
                    Toast.makeText(getApplicationContext(), "시간을 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                //////////// 비어있는 입력 값 체크하기 ////////////


                Data data = new Data(st_id, send_name, rec_name, type, send_loc, rec_loc, time_info);
                ReservationData reservationData_send = new ReservationData(st_id, loc_send, type, time_info, "load");
                ReservationData reservationData_rec = new ReservationData(st_id, loc_rec, type, time_info, "unload");


                post_reservation(reservationData_send);
                post_reservation(reservationData_rec);
                post_user(data);

            }
        });
        /////////////////////// 예약하기 버튼 누르면 DB에 정보 입력 ///////////////////////

    }



    // 사용자가 지정한 날짜정보를 TextView에 표시하기 위해 해당 id의 참조 객체를 얻어옴
    public void InitializeView_date()
    {
        tv_date = (TextView)findViewById(R.id.textView_date);
    }
    public void InitializeView_time()
    {
        tv_time = (TextView)findViewById(R.id.textView_time);
    }


    // OnDateSetListener를 구현하는 함수
    public void InitializeListener_date()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                tv_date.setText(year + "년 " + (month+1) + "월 " + day + "일");

                rv_year = String.valueOf(year);
                rv_month = String.valueOf(month+1);
                rv_day = String.valueOf(day);
            }
        };

    }

    // OnDateSetListener를 구현하는 함수
    public void InitializeListener_time()
    {
        callbackMethod2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                tv_time.setText(hour + "시 " + minute + "분");

                rv_hour = String.valueOf(hour);
                rv_minute = String.valueOf(minute);
            }
        };
    }


    // 버튼이 클릭되었을 때 DatePickerDialog 객체를 생성하며
    // 첫째 인자로서 Context정보가, 두번째로는 앞서 구현한 OnDateSetListener 구현체가 넘어감
    // 나머지 인자는 다이얼로그 창을 처음 띄울 시 날짜 정보
    public void OnClickHandler(View view)
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat_year = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat_month = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormat_day = new SimpleDateFormat("dd");

        String year = dateFormat_year.format(date);
        String month = dateFormat_month.format(date);
        String day = dateFormat_day.format(date);

        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        dialog.show();
    }

    public void OnClickHandler2(View view)
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat dateFormat_hour = new SimpleDateFormat("hh");
        SimpleDateFormat dateFormat_minute = new SimpleDateFormat("mm");


        CustomTimePickerDialog dialog = new CustomTimePickerDialog(this, callbackMethod2, 00, 00, true);
        dialog.show();
    }


    //////////////// 건물별 예약정보 DB에 입력 ////////////////
    public void post_reservation(ReservationData reservationData) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceAPI serviceApi = retrofit.create(serviceAPI.class);

        Call<ReservationData> call =serviceApi.postReservation(reservationData);

        call.enqueue(new Callback<ReservationData>() {
            @Override
            public void onResponse(Call<ReservationData> call, Response<ReservationData> response) {
                if(!response.isSuccessful()) {
                    Log.v("Reservation 오류", response.message());
                    return;
                }
            }

            @Override
            public void onFailure(Call<ReservationData> call, Throwable t) {
                Log.v("Reservation 오류", "Fail");
            }
        });
    }
    //////////////// 건물별 예약정보 DB에 입력 ////////////////


    //////////////// 예약 정보 DB에 입력 ////////////////
    public void post_user(Data data) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceAPI serviceApi = retrofit.create(serviceAPI.class);

        Call<Data> call = serviceApi.postUser(data);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(!response.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Insert 오류", Toast.LENGTH_SHORT).show();
                    Log.v("오류", response.message());
                    return;
                }

                Toast.makeText(getApplicationContext(), "예약 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "DB 입력 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //////////////// 예약 정보 DB에 입력 ////////////////

}