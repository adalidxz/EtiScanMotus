package com.example.etiscanmotus.EtiScan

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.etiscanmotus.Adapters.ScanListAdapter
import com.example.etiscanmotus.Api.RetrofitClient
import com.example.etiscanmotus.R
import com.example.etiscanmotus.Response.EtiScan_ListScan_Response
import com.example.etiscanmotus.Response.NewScan_Response
import com.example.etiscanmotus.RetrofitRes.RetrofitRes
import com.example.etiscanmotus.Storage.ApplicationPrefs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EtiScan : AppCompatActivity() {
    var rvListScan:RecyclerView? = null
    var scanlist = mutableListOf<EtiScan_ListScan_Response>()
    var btn_new_scan: FloatingActionButton? = null
    var swiptorefresh: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eti_scan)
        this.setTitle("Lista de Escaneos")
        rvListScan = findViewById(R.id.rvListScan)
        btn_new_scan = findViewById(R.id.scanList_btn_new_scan)
        swiptorefresh = findViewById(R.id.swiptorefresh)
        setEvents()
        getScan()
    }

    override fun onRestart() {
        super.onRestart()
        getScan()
    }
    private fun add_new_scan(mContext: Context, idNumberPartF:Int, idUser:Int){
        RetrofitClient.instance.newScan(idUser,idNumberPartF)
            .enqueue(object : Callback<NewScan_Response> {
                override fun onFailure(call: Call<NewScan_Response>, t: Throwable) {
                    Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show();
                }

                override fun onResponse(call: Call<NewScan_Response>, response: Response<NewScan_Response>) {
                    if(response.code()==403){
                        RetrofitRes.tokentimeout(mContext, this@EtiScan)
                        return
                    }
                    val array = response.body()
                    if(array?.status == true){
                        getScan()
                    }


                }
            })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_etiscan,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opsalir->{
                RetrofitRes.tokentimeout(this,this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setEvents(){
        btn_new_scan?.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("1492613-F/1492614-F - 1492613-R/1492614-R")
            builder.setMessage("Deseas crear un nuevo escaneo con este número de parte?")
            builder.setPositiveButton("SI", { dialogInterface: DialogInterface, i: Int ->
                add_new_scan(this,1,ApplicationPrefs.prefs.get_idUser())
            })
            builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->

            })
            builder.show()
            /*val intent = Intent(this,NewScan::class.java)
            startActivity(intent)*/
        }

        swiptorefresh?.setOnRefreshListener {
            scanlist.clear()
            getScan()
            Toast.makeText(this,"Lista actualizada",Toast.LENGTH_LONG).show()
        }
    }
    private fun getScan(){
        RetrofitClient.instance.getListScan()
            .enqueue(object : Callback<List<EtiScan_ListScan_Response>> {
                override fun onFailure(call: Call<List<EtiScan_ListScan_Response>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    RetrofitRes.tokentimeout(this@EtiScan, this@EtiScan)
                    return
                }

                override fun onResponse(
                    call: Call<List<EtiScan_ListScan_Response>>,
                    response: Response<List<EtiScan_ListScan_Response>>
                ) {
                    if (response.code() == 403) {
                        RetrofitRes.tokentimeout(this@EtiScan, this@EtiScan)
                        return
                    }
                    scanlist.clear()
                    response.body()?.forEach { it ->
                        scanlist.add(
                            EtiScan_ListScan_Response(
                                it.ID_EtiScan_Scan,
                                it.ID_Usuario,
                                it.Nombre,
                                it.ID_EtiScan_NumberPart_F,
                                it.NumberPart_F,
                                it.Date,
                                it.Time,
                                it.Date_Start,
                                it.Date_End,
                                it.Status,
                                it.Active
                            )
                        )
                    }
                    rvListScan?.layoutManager = LinearLayoutManager(this@EtiScan)
                    val adapter = ScanListAdapter(scanlist)
                    rvListScan?.adapter = adapter
                    swiptorefresh?.isRefreshing = false
                }
            })
    }

}