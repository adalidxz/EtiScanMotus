package com.example.etiscanmotus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.etiscanmotus.EtiScan.EtiScan
import com.example.etiscanmotus.RetrofitRes.RetrofitRes
import com.example.etiscanmotus.Storage.ApplicationPrefs.Companion.prefs
import com.example.etiscanmotus.login.Login

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println(prefs.get_apikey())
        validar_login()
    }

    private fun validar_login(){

        if(prefs.get_apikey() == null){
            RetrofitRes.tokentimeout(this,this)
        }else{
            gotomenu()
        }
    }
    private fun gotomenu(){
        val intent = Intent(this,EtiScan::class.java)
        startActivity(intent)
        this.finish()

    }



}