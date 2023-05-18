package com.example.projectapp;

import static com.example.projectapp.Cons.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;


import com.example.projectapp.retrofit.Robot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Tracking extends AppCompatActivity {

    // DB에 넘겨줄 Sener와 Receiver
    TextInputEditText et_sender;
    TextInputEditText et_receiver;

    // 버튼 선언
    Button btn_search;
    Button btn_loc;
    Button btn_confirm;


    TextView tv_result;
    TextView tv_coor;

    private GpsTracker gpsTracker;


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        ////////// Initialize Retrofit //////////
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceAPI serviceApi = retrofit.create(serviceAPI.class);
        ////////// Initialize Retrofit //////////


        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }



        et_sender =  findViewById(R.id.et_tracking_sender);
        et_receiver =  findViewById(R.id.et_tracking_receiver);

        tv_result = findViewById(R.id.tv_tracking_result);

        tv_coor = findViewById(R.id.tv_coor);


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


                gpsTracker = new GpsTracker(Tracking.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();


                String address = getCurrentAddress(latitude, longitude);


                String latitude2 = Double.toString(latitude);
                String longitude2 = Double.toString(longitude);

                // 로봇 좌표 업데이트 //
                Robot robot = new Robot("1", latitude2, longitude2);

                Call<Robot> call_robot = serviceApi.updateRobot(robot);

                call_robot.enqueue(new Callback<Robot>() {
                    @Override
                    public void onResponse(Call<Robot> call_robot, Response<Robot> response) {
                        if (!response.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(), "update 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }

                    @Override
                    public void onFailure(Call<Robot> call_robot, Throwable t) {
                        //Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
                    }
                });
                // 로봇 좌표 업데이트 //



                // 예약 정보, 목적지 좌표 받아오기 //
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
                            tv_coor.setText("");
                            return;
                        }

                        Object data = response.body();

                        HashMap<String, String> map_result = new HashMap<String, String>();
                        Gson gson = new Gson();
                        map_result = gson.fromJson(data.toString(), map_result.getClass());


                        tv_result.setText(map_result.get("send_name") + " 님이 " + map_result.get("rec_name") + " 님에게 \n" + map_result.get("type") + "을(를) " + map_result.get("send_loc") + " 에서 " + map_result.get("rec_loc") + " (으)로 보내셨습니다.");
                        //Toast.makeText(getApplicationContext(), "조회 성공", Toast.LENGTH_SHORT).show();

                        call_coor.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                if(!response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "좌표 조회 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Object coordinate = response.body();

                                HashMap<String, String> map_coor = new HashMap<String, String>();
                                Gson gson = new Gson();
                                map_coor = gson.fromJson(coordinate.toString(), map_coor.getClass());

                                String bd_name = map_coor.get("name");
                                String bd_latitude = String.valueOf(map_coor.get("latitude"));
                                String bd_longitude = String.valueOf(map_coor.get("longitude"));

                                Double lat1 = Double.parseDouble(bd_latitude);
                                Double long1 = Double.parseDouble(bd_longitude);
                                //Double lat2 = Double.parseDouble(latitude2);

                                double theta = longitude - long1;
                                double DT = Math.sin(Math.toRadians(latitude)) * Math.sin(Math.toRadians(lat1)) +
                                        Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(lat1)) *
                                                Math.cos(Math.toRadians(theta));

                                DT = Math.acos(DT);
                                DT = Math.toDegrees(DT);
                                DT = DT * 60 * 1.1515;
                                DT = DT * 1609.344;

                                double s = 1.5;     //만약 로봇의 속도가 1.5m/s 일때
                                double time;
                                time = DT / s;      //이동시간 구하는 식
                                time = time / 60;   //초 -> 분 변환
                                time = Math.round(time);    //소수점 반올림
                                int time1;
                                time1 = (int) time;

                                if(time1 < 2) {
                                    tv_coor.setText("택배가 곧 도착합니다");
                                } else {
                                    tv_coor.setText("택배가 " + time1 + " 분 후 도착합니다");
                                }


                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "조회 실패", Toast.LENGTH_SHORT).show();
                    }

                });
                // 예약 정보, 목적지 좌표 받아오기 //

            }

        });
        ////////////////////// 조회하기 버튼 /////////////////////




        ////////////////////// 실시간 위치 조회 버튼 /////////////////////
        btn_loc = findViewById(R.id.btn_loc);

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sender_name = et_sender.getText().toString();
                String receiver_name = et_receiver.getText().toString();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("send_name", sender_name);
                map.put("rec_name", receiver_name);

                Call<Object> call_coor = serviceApi.getCoordinate(map);

                call_coor.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(!response.isSuccessful()) {
                            return;
                        }

                        Object coordinate = response.body();

                        HashMap<String, String> map_coor = new HashMap<String ,String>();
                        Gson gson = new Gson();
                        map_coor = gson.fromJson(coordinate.toString(), map_coor.getClass());

                        String bd_name = map_coor.get("name");
                        String bd_latitude = String.valueOf(map_coor.get("latitude"));
                        String bd_longitude = String.valueOf(map_coor.get("longitude"));

                        Intent intent = new Intent(getApplicationContext(), Tracking2.class);
                        intent.putExtra("bd_name", bd_name);
                        intent.putExtra("bd_latitude", bd_latitude);
                        intent.putExtra("bd_longitude", bd_longitude);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        });
        ////////////////////// 실시간 위치 조회 버튼 /////////////////////


        ////////////////////// 수령확인 버튼 /////////////////////
        btn_confirm = findViewById(R.id.btn_tracking_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Tracking.this)
                        .setTitle("확인")
                        .setMessage("수령 확인하시겠습니까?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String send_name = et_sender.getText().toString();
                                String rec_name = et_receiver.getText().toString();
                                String status = "1";


                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("send_name", send_name);
                                map.put("rec_name", rec_name);
                                map.put("status", status);

                                Call<Object> call = serviceApi.updateUserStatus(map);

                                call.enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {
                                        if (!response.isSuccessful()) {
                                            return;
                                        }

                                        Toast.makeText(getApplicationContext(), "수령 확인 완료", Toast.LENGTH_SHORT).show();

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
        ////////////////////// 수령확인 버튼 /////////////////////


    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(Tracking.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_SHORT).show();
                    finish();


                } else {

                    Toast.makeText(Tracking.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(Tracking.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(Tracking.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(Tracking.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(Tracking.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Tracking.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(Tracking.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            //Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            //Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Tracking.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


}