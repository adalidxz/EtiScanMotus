package com.example.etiscanmotus.Adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.etiscanmotus.Api.RetrofitClient
import com.example.etiscanmotus.EtiScan.EtiScan_Scan
import com.example.etiscanmotus.NewScan.NewScan
import com.example.etiscanmotus.R
import com.example.etiscanmotus.Response.EtiScan_NewScan_Response
import com.example.etiscanmotus.Response.NewScan_Response
import com.example.etiscanmotus.RetrofitRes.RetrofitRes
import com.example.etiscanmotus.Storage.ApplicationPrefs.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewScan_Adapter(private val newscan:List<EtiScan_NewScan_Response>,private val activity: Activity): RecyclerView.Adapter<NewScan_Adapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewScan_Adapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.new_scan_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.render(newscan[position],activity)
    }

    override fun getItemCount(): Int {
        return newscan.size
    }

    class ViewHolder(val view:View):RecyclerView.ViewHolder(view){
        private fun add_new_scan(mContext:Context, idNumberPartF:Int, idUser:Int, view: Activity){
            RetrofitClient.instance.newScan(idUser,idNumberPartF)
                .enqueue(object : Callback<NewScan_Response> {
                    override fun onFailure(call: Call<NewScan_Response>, t: Throwable) {
                        Toast.makeText(mContext, t.message, Toast.LENGTH_LONG).show();
                        RetrofitRes.tokentimeout(mContext,view)
                    }

                    override fun onResponse(call: Call<NewScan_Response>, response: Response<NewScan_Response>) {
                        if(response.code()==403){
                            RetrofitRes.tokentimeout(mContext, view)
                            return
                        }
                        val array = response.body()
                        if(array?.status == true){
                            array.data
                            val intent = Intent(mContext, EtiScan_Scan::class.java)
                            intent.putExtra("idScan",array?.data.id)
                            mContext.startActivity(intent)
                        }


                    }
                })
        }
        fun render(item: EtiScan_NewScan_Response,activity:Activity){
            view.findViewById<TextView>(R.id.newScan_id).setText(item.ID_EtiScan_NumberPart_F.toString())
            view.findViewById<TextView>(R.id.newscan_numberpartF).setText(item.NumberPart_F)


            view.setOnClickListener {
                Toast.makeText(view.context, "Click ${item.ID_EtiScan_NumberPart_F}", Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(item.NumberPart_F)
                builder.setMessage("Deseas crear un nuevo escaneo con este número de parte?")
                builder.setPositiveButton("SI", { dialogInterface: DialogInterface, i: Int ->
                    add_new_scan(view.context,item.ID_EtiScan_NumberPart_F,prefs.get_idUser(),activity)
                })
                builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->

                })
                builder.show()
            }
        }
    }

}