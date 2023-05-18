package com.example.projectapp.retrofit;

import static com.example.projectapp.Cons.DELETE_USER_URL;
import static com.example.projectapp.Cons.GET_COORDINATE_URL;
import static com.example.projectapp.Cons.GET_DATA_URL;
import static com.example.projectapp.Cons.POST_RESERVATION_URL;
import static com.example.projectapp.Cons.POST_USER_URL;
import static com.example.projectapp.Cons.PUT_ROBOT_URL;
import static com.example.projectapp.Cons.PUT_USER_STATUS_URL;


import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface serviceAPI {
    // 예약 정보 DB에 저장
    @POST(POST_USER_URL)
    Call<Data> postUser(@Body Data data);
    // 예약 건물별 정보 DB에 저장
    @POST(POST_RESERVATION_URL)
    Call<ReservationData> postReservation(@Body ReservationData reservationData);


    // 예약 정보 받아오기
    @GET(GET_DATA_URL)
    Call<Object> getData(@QueryMap Map<String, String> option);
    // 목적지 좌표 받아오기
    @GET(GET_COORDINATE_URL)
    Call<Object> getCoordinate(@QueryMap Map<String, String> option);


    // 로봇 좌표 업데이트
    @PUT(PUT_ROBOT_URL)
    Call<Robot> updateRobot(@Body Robot robot);
    // 수령 확인 후 status 변경
    @PUT(PUT_USER_STATUS_URL)
    Call<Object> updateUserStatus(@Body Map<String, String> option);


    // 예약 정보 삭제
    @DELETE(DELETE_USER_URL)
    Call<Object> deleteUser(@QueryMap Map<String, String> option);

}
