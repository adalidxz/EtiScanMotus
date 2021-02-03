package com.example.etiscanmotus.RetrofitRes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Layout
import android.widget.Toast
import com.example.etiscanmotus.Api.RetrofitClient
import com.example.etiscanmotus.MainActivity
import com.example.etiscanmotus.Storage.ApplicationPrefs.Companion.prefs
import com.example.etiscanmotus.login.Login
import com.example.etiscanmotus.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

object RetrofitRes {
    fun tokentimeout(mContext: Context, view: Activity){
        prefs.save_apikey("",0)
        Toast.makeText(mContext, "Session expirada favor de ingresar nuevamente.", Toast.LENGTH_SHORT).show()
        val intent2 = Intent(mContext,Login::class.java)
        mContext.startActivity(intent2)
        view.finish()

    }
    fun validar_login(mContext:Context, username:String, password:String, view: Login){
        RetrofitClient.instance.userLogin(username,password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show();
                }

                override fun onResponse(call: Call<LoginResponse>,response: Response<LoginResponse>) {
                    val res = response.body()
                    if (res?.token != null) {
                        prefs.save_apikey(res.token, res.json.id)
                        val intent = Intent(mContext,MainActivity::class.java)
                        mContext.startActivity(intent)
                        view.finish()
                    } else {
                        Toast.makeText(mContext, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}

