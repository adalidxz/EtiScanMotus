package com.example.etiscanmotus.Api
import android.text.Editable
import com.example.etiscanmotus.Response.*
import com.example.etiscanmotus.login.LoginResponse
import retrofit2.Call
import retrofit2.http.*
interface Api {
    @FormUrlEncoded
    @POST("api/auth/singin/")
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/newScan/")
    fun newScan(
        @Field("idUser") idUser: Int,
        @Field("idNumberPartF") idNumberPartF: Int
    ): Call<NewScan_Response>


    @GET("api/EtiScan/Scans/")
    fun getListScan():Call<List<EtiScan_ListScan_Response>>

    @GET("api/EtiScan/ScansbyID/{id}")
    fun getInfoScan(@Path("id") id:Int):Call<EtiScan_ListScan_Response>


    @GET("api/EtiScan/NumberPart_F/")
    fun getNumberPartF():Call<List<EtiScan_NewScan_Response>>

    @GET("api/EtiScan/NumberPartByID/{id}")
    fun getNumberPart(@Path ("id") id:Int): Call<EtiScan_NumberPartResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/updateCodeMaster/")
    fun saveCodeMaster(
            @Field("id") id:Int,
            @Field("lectura") lectura: String
    ): Call<SaveLecturaResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/updateCodeSerial/")
    fun saveCodeMasterSerial(
            @Field("id") id:Int,
            @Field("lectura") lectura: String
    ): Call<SaveLecturaResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/updateLabel/")
    fun saveCodeLabel(
            @Field("id") id:Int,
            @Field("lectura") lectura: String
    ): Call<SaveLecturaResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/finalizarNumberScan/")
    fun finalizarNumber(
            @Field("id") id:Int
    ): Call<SaveLecturaResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/finalizarScan/")
    fun finalizarScan(
            @Field("id") id:Int
    ): Call<UpdateResponse>

    @FormUrlEncoded
    @POST("api/EtiScan/updateDateInicio/")
    fun updateDateInicio(
        @Field("id") id:Int
    ): Call<UpdateResponse>


    @DELETE("api/EtiScan/deleteScan/{id}")
    fun deleteScan(
            @Path("id") id:Int
    ): Call<UpdateResponse>
}