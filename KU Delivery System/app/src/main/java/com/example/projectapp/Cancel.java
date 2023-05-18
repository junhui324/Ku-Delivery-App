package com.example.projectapp;

import static com.example.projectapp.Cons.BASE_URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectapp.retrofit.serviceAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Cancel extends AppCompatActivity {

    // DB에 넘겨줄 Sener와 Receiver
    TextInputEditText et_sender;
    TextInputEditText et_receiver;

    // 버튼 선언
    Button btn_search;
    Button btn_delete;

    TextView tv_result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        ////////// Initialize Retrofit //////////
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceAPI serviceApi = retrofit.create(serviceAPI.class);
        ////////// Initialize Retrofit //////////


        et_sender =  findViewById(R.id.et_tracking_sender);
        et_receiver =  findViewById(R.id.et_tracking_receiver);

        tv_result = findViewById(R.id.tv_tracking_result);



        ////////////////////// 조회하기 버튼 /////////////////////
        btn_search = findViewById(R.id.btn_tracking_search);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sender_name = et_sender.getText().toString();
                String receiver_name = et_receiver.getText().toString();

                // 비어있는 값 체크 //
                if(sender_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "보내는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_sender.requestFocus();
                    return;
                }

                if(receiver_name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "받는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                    et_receiver.requestFocus();
                    return;
                }
                // 비어있는 값 체크 //


                // 예약 정보 받아오기 //
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("send_name", sender_name);
                map.put("rec_name", receiver_name);

                Call<Object> call = serviceApi.getData(map);

                Call<Object> call_coor = serviceApi.getCoordinate(map);

                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "예약 정보가 없습니다", Toast.LENGTH_SHORT).show();
                            tv_result.setText("");
                            return;
                        }

                        Object data = response.body();

                        HashMap<String, String> map_result = new HashMap<String, String>();
                        Gson gson = new Gson();
                        map_result = gson.fromJson(data.toString(), map_result.getClass());


                        tv_result.setText(map_result.get("send_name") + " 님이 " + map_result.get("rec_name") + " 님에게 \n" + map_result.get("type") + "을(를) " + map_result.get("send_loc") + " 에서 " + map_result.get("rec_loc") + " (으)로 보내셨습니다.");
                        //Toast.makeText(getApplicationContext(), "조회 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "조회 실패", Toast.LENGTH_SHORT).show();
                    }

                });
                // 예약 정보 받아오기 //

            }

        });
        ////////////////////// 조회하기 버튼 /////////////////////




        ////////////////////// 예약 취소 버튼 /////////////////////
        btn_delete = findViewById(R.id.btn_tracking_confirm);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Cancel.this)
                        .setTitle("확인")
                        .setMessage("예약을 취소하시겠습니까?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String send_name = et_sender.getText().toString();
                                String rec_name = et_receiver.getText().toString();

                                // 비어있는 값 체크 //
                                if(send_name.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "보내는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                                    et_sender.requestFocus();
                                    return;
                                }

                                if(rec_name.length() == 0) {
                                    Toast.makeText(getApplicationContext(), "받는 분 성함을 입력하세요", Toast.LENGTH_SHORT).show();
                                    et_receiver.requestFocus();
                                    return;
                                }
                                // 비어있는 값 체크 //


                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("send_name", send_name);
                                map.put("rec_name", rec_name);

                                Call<Object> call = serviceApi.deleteUser(map);

                                call.enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (!response.isSuccessful()) {
                                            return;
                                        }

                                        Toast.makeText(getApplicationContext(), "예약 취소 완료", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ////////////////////// 예약 취소 버튼 /////////////////////

    }


}