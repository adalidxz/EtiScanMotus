package com.example.etiscanmotus.EtiScan

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.etiscanmotus.Api.RetrofitClient
import com.example.etiscanmotus.R
import com.example.etiscanmotus.Response.EtiScan_ListScan_Response
import com.example.etiscanmotus.Response.EtiScan_NumberPartResponse
import com.example.etiscanmotus.Response.SaveLecturaResponse
import com.example.etiscanmotus.Response.UpdateResponse
import com.example.etiscanmotus.RetrofitRes.RetrofitRes
import com.example.etiscanmotus.Storage.ApplicationPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EtiScan_Scan : AppCompatActivity() {
    private var scan_title:TextView? = null
    private var scan_container:TextView? = null
    private var scan_numberpart:TextView? = null
    private var scan_codemaster:EditText? = null
    private var scan_codemaster_serial:EditText? = null
    private var scan_codelabel:EditText? = null
    private var id_scan: Int? = null
    private  var infoScan  = mutableListOf<EtiScan_ListScan_Response>()
    private  var infoNumberPart : EtiScan_NumberPartResponse? = null
    private var btn_next: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eti_scan_scan)
        Init()
        hideSoftKeyboard()
        getInfoScan()
        startScan()
        setEvents()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_scan,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.deleteScan->{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar Escaneo")
                builder.setMessage("Estas seguro de eliminar este escaneo?")
                builder.setPositiveButton("SI", { dialogInterface: DialogInterface, i: Int ->
                    delteScan()
                })
                builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->

                })
                builder.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun delteScan(){
        RetrofitClient.instance.deleteScan(infoScan[0].ID_EtiScan_Scan)
                .enqueue(object : Callback<UpdateResponse> {
                    override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }

                    override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                        val res = response.body()
                        if (res?.status == true) {
                            this@EtiScan_Scan.finish()
                        } else {
                            Toast.makeText(this@EtiScan_Scan, "Error: ${res?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }
    private fun updateDateInicio(id: Int){
        RetrofitClient.instance.updateDateInicio(id)
            .enqueue(object : Callback<UpdateResponse> {
                override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                }

                override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                    println(response)
                }
            })
    }
    private fun finalizarNumberScan() {
        val id = infoNumberPart?.ID_EtiScan_ScanNumbers!!
        RetrofitClient.instance.finalizarNumber(id)
        .enqueue(object : Callback<SaveLecturaResponse> {
            override fun onFailure(call: Call<SaveLecturaResponse>, t: Throwable) {
                println(t.message)
                Toast.makeText(this@EtiScan_Scan, t.message, Toast.LENGTH_LONG).show();
            }

            override fun onResponse(call: Call<SaveLecturaResponse>, response: Response<SaveLecturaResponse>) {
                if (response.code() == 403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                if(response.body()?.status == true){
                    cleanform()
                    startScan()
                }else{
                    Toast.makeText(this@EtiScan_Scan, response.body()?.message, Toast.LENGTH_LONG).show();
                }
            }
        })
    }
    private fun cleanform(){
        scan_codemaster?.setText("")
        scan_codemaster_serial?.setText("")
        scan_codelabel?.setText("")
        btn_next?.isEnabled=false
    }
    private fun saveLabelCode(){
        val id = infoNumberPart?.ID_EtiScan_ScanNumbers!!
        val lectura = scan_codelabel?.text.toString()
        if(!lectura.equals(infoNumberPart?.CodeLabel)){
            scan_codelabel?.setText("")
            scan_codelabel?.setError("Los númeors de parte no coinciden")
            scan_codelabel?.requestFocus()
        }else{
            RetrofitClient.instance.saveCodeLabel(id, lectura)
                    .enqueue(object : Callback<SaveLecturaResponse> {
                        override fun onFailure(call: Call<SaveLecturaResponse>, t: Throwable) {
                            println(t.message)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                        }

                        override fun onResponse(call: Call<SaveLecturaResponse>, response: Response<SaveLecturaResponse>) {
                            if (response.code() == 403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                            val res = response.body()
                            if (res?.status == true) {
                                startScan()
                            } else {
                                scan_codelabel?.setError(res?.message)
                                scan_codelabel?.setText("")
                                scan_codelabel?.requestFocus()
                            }
                        }
                    })
        }
    }
    private fun saveMasterCodeSerial(){
        val id = infoNumberPart?.ID_EtiScan_ScanNumbers!!
        val lectura = scan_codemaster_serial?.text.toString()
        if(!lectura.first().toString().equals("S")){
            scan_codemaster_serial?.setText("")
            scan_codemaster_serial?.setError("El Serial debe de empezar con una 'S'")
            scan_codemaster_serial?.requestFocus()
        }else{
            RetrofitClient.instance.saveCodeMasterSerial(id, lectura)
                    .enqueue(object : Callback<SaveLecturaResponse> {
                        override fun onFailure(call: Call<SaveLecturaResponse>, t: Throwable) {
                            println(t.message)
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                        }

                        override fun onResponse(call: Call<SaveLecturaResponse>, response: Response<SaveLecturaResponse>) {
                            if (response.code() == 403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                            val res = response.body()

                            if (res?.status == true) {
                                startScan()
                            } else {
                                scan_codemaster_serial?.setError(res?.message)
                                scan_codemaster_serial?.setText("")
                                scan_codemaster_serial?.requestFocus()
                            }
                        }
                    })
        }
    }
    private fun saveMasterCode(){
        if(!scan_codemaster?.text?.first().toString().equals("P")){
            scan_codemaster?.setText("")
            scan_codemaster?.requestFocus()
            scan_codemaster?.setError("El númer de parte debe Iniciar con 'P'")
        } else {
            if (!scan_codemaster?.text.toString().equals(infoNumberPart?.CodeMaster)) {
                    scan_codemaster?.setText("")
                    scan_codemaster?.requestFocus()
                    scan_codemaster?.setError("Los números de parte no coinciden")
                }else{
                    RetrofitClient.instance.saveCodeMaster(infoNumberPart?.ID_EtiScan_ScanNumbers!!, scan_codemaster?.text.toString())
                        .enqueue(object : Callback<SaveLecturaResponse> {
                            override fun onFailure(call: Call<SaveLecturaResponse>, t: Throwable) {
                                println(t.message)
                                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                            }

                            override fun onResponse(call: Call<SaveLecturaResponse>, response: Response<SaveLecturaResponse>) {
                                if (response.code() == 403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                                val res = response.body()

                                if (res?.status == true) {
                                    startScan()
                                } else {
                                    scan_codemaster?.setError(res?.message)
                                    scan_codemaster?.setText("")
                                    scan_codemaster?.requestFocus()
                                }
                            }
                        })
                }

            }
    }
    private fun finalizarScan(){
        RetrofitClient.instance.finalizarScan(infoScan[0].ID_EtiScan_Scan)
                .enqueue(object : Callback<UpdateResponse> {
                    override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }
                    override fun onResponse(call: Call<UpdateResponse>, response: Response<UpdateResponse>) {
                        if(response.code()==403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                        println(response.body())
                        if(response.body()?.status == true){
                            finish()
                        }else{
                            Toast.makeText(this@EtiScan_Scan, response.body()?.message, Toast.LENGTH_LONG).show();
                        }
                    }
                })
    }
    private fun startScan(){
        RetrofitClient.instance.getNumberPart(id_scan!!)
                .enqueue(object : Callback<EtiScan_NumberPartResponse> {
                    override fun onFailure(call: Call<EtiScan_NumberPartResponse>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(this@EtiScan_Scan, t.message, Toast.LENGTH_LONG).show();
                    }

                    override fun onResponse(call: Call<EtiScan_NumberPartResponse>, response: Response<EtiScan_NumberPartResponse>) {
                        if (response.code() == 403) RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                        infoNumberPart = response.body()
                        if(infoNumberPart?.ID_EtiScan_Scan == 0){
                            finalizarScan()
                        }else{
                            scan_container?.text = infoNumberPart?.Name_Container
                            scan_numberpart?.text = infoNumberPart?.NumeroParte

                            if (infoNumberPart?.CodeMaster_Checked == null) {
                                scan_codemaster?.isEnabled = true
                                scan_codemaster?.requestFocus()
                            } else {

                                scan_codemaster?.isEnabled = false
                                scan_codemaster?.setText(infoNumberPart?.CodeMaster_Checked)
                                if (infoNumberPart?.CodeMaster_Serial == null) {
                                    scan_codemaster_serial?.isEnabled = true
                                    scan_codemaster_serial?.requestFocus()
                                } else {
                                    scan_codemaster_serial?.isEnabled = false
                                    scan_codemaster_serial?.setText(infoNumberPart?.CodeMaster_Serial)
                                    if (infoNumberPart?.CodeLabel_Checked == null) {
                                        scan_codelabel?.isEnabled = true
                                        scan_codelabel?.requestFocus()
                                    } else {
                                        scan_codelabel?.isEnabled = false
                                        scan_codelabel?.setText(infoNumberPart?.CodeLabel_Checked)
                                        btn_next?.isEnabled = true
                                    }
                                }
                            }
                        }

                    }
                })
    }
    private fun getInfoScan(){
        if(id_scan == 0) RetrofitRes.tokentimeout(this,this)
        RetrofitClient.instance.getInfoScan(id_scan!!)
                .enqueue(object : Callback<EtiScan_ListScan_Response> {
                    override fun onFailure(call: Call<EtiScan_ListScan_Response>, t: Throwable) {
                        println(t.message)
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show();
                    }

                    override fun onResponse(call: Call<EtiScan_ListScan_Response>, response: Response<EtiScan_ListScan_Response>) {
                        if (response.code() == 403) {
                            RetrofitRes.tokentimeout(this@EtiScan_Scan,this@EtiScan_Scan)
                            return
                        }
                        val info = response.body()
                        println(info)
                        if (info != null) {
                            infoScan.add(EtiScan_ListScan_Response(info.ID_EtiScan_Scan, info.ID_Usuario, info.Nombre, info.ID_EtiScan_NumberPart_F, info.NumberPart_F, info.Date, info.Time, info.Date_Start, info.Date_End, info.Status, info.Active))
                            updateDateInicio(info.ID_EtiScan_Scan)
                        }
                        println(infoScan)
                        scan_title?.text = infoScan[0].NumberPart_F
                    }
                })

    }
    private fun setEvents(){
        val handler = Handler()
        scan_codemaster?.doOnTextChanged { text, start, before, count ->
            if (scan_codemaster?.isEnabled == true && text?.length!! > 0) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ saveMasterCode() }, 200)
            }
        }
        scan_codemaster_serial?.doOnTextChanged { text, start, before, count ->
            if(scan_codemaster_serial?.isEnabled  == true &&  text?.length!! > 0){
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ saveMasterCodeSerial() }, 200)
            }
        }
        scan_codelabel?.doOnTextChanged { text, start, before, count ->
            if(scan_codelabel?.isEnabled  == true &&  text?.length!! > 0){
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ saveLabelCode() }, 200)
            }
        }
        btn_next?.setOnClickListener {
            if(btn_next?.isEnabled == true){
                finalizarNumberScan()
            }else{
                println("No esta habilitado el boton")
            }
        }
    }
    fun Init(){
        this.title = "Escaneo"
        id_scan = intent.getIntExtra("idScan", 0)
        scan_title = findViewById(R.id.scan_title)
        scan_container = findViewById(R.id.scan_container)
        scan_numberpart = findViewById(R.id.scan_numberpart)
        scan_codemaster = findViewById(R.id.scan_codemaster)
        scan_codemaster_serial = findViewById(R.id.scan_codemaster_serial)
        scan_codelabel = findViewById(R.id.scan_codelabel)
        btn_next = findViewById(R.id.scan_btn)

    }
    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
}