package com.example.etiscanmotus.NewScan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.etiscanmotus.Adapters.NewScan_Adapter
import com.example.etiscanmotus.Api.RetrofitClient
import com.example.etiscanmotus.R
import com.example.etiscanmotus.Response.EtiScan_NewScan_Response
import com.example.etiscanmotus.RetrofitRes.RetrofitRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewScan : AppCompatActivity() {
    var numberlist = mutableListOf<EtiScan_NewScan_Response>()
    private var listview_numbersF:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_scan)
        this.setTitle("Selecciona un Numero de parte")
        init()
        get_lista_numbersF()
    }
    private fun init(){
        listview_numbersF = findViewById(R.id.rvNumbersF);
    }

    private fun get_lista_numbersF(){
        RetrofitClient.instance.getNumberPartF()
            .enqueue(object : Callback<List<EtiScan_NewScan_Response>> {
                override fun onFailure(call: Call<List<EtiScan_NewScan_Response>>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                }

                override fun onResponse(call: Call<List<EtiScan_NewScan_Response>>, response: Response<List<EtiScan_NewScan_Response>>) {
                    if(response.code()==403){
                        RetrofitRes.tokentimeout(this@NewScan,this@NewScan)
                        return
                    }
                    val array = response.body()
                    array?.forEach {
                        numberlist?.add(EtiScan_NewScan_Response(it.ID_EtiScan_NumberPart_F,it.NumberPart_F))
                    }

                    listview_numbersF?.layoutManager = LinearLayoutManager(this@NewScan)
                    val adapter = NewScan_Adapter(numberlist,this@NewScan)
                    println(adapter)
                    listview_numbersF?.adapter = adapter
                }
            })
    }
}