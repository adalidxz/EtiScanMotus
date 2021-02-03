package com.example.etiscanmotus.Adapters

import android.annotation.SuppressLint

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.etiscanmotus.EtiScan.EtiScan
import com.example.etiscanmotus.EtiScan.EtiScan_Scan
import com.example.etiscanmotus.R
import com.example.etiscanmotus.Response.EtiScan_ListScan_Response


class ScanListAdapter(private val scan: List<EtiScan_ListScan_Response>): RecyclerView.Adapter<ScanListAdapter.ScanHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int,): ScanHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ScanHolder(layoutInflater.inflate(R.layout.scan_list_item,parent,false))
    }
    override fun getItemCount(): Int {
        return scan.size

    }

    override fun onBindViewHolder(holder: ScanHolder, position: Int) {
        holder.render(scan[position],position)
    }
    class ScanHolder(val view:View):RecyclerView.ViewHolder(view){

        @SuppressLint("SetTextI18n")
        fun render(item:EtiScan_ListScan_Response, position: Int){

            val status = view.findViewById<TextView>(R.id.EtiScan_List_Item_Status)
            val layitem: LinearLayout = view.findViewById(R.id.linear_layout_main_item)
            val date = view.findViewById<TextView>(R.id.EtiScan_List_Item_Date)
            val time = view.findViewById<TextView>(R.id.EtiScan_List_Item_Time)
            view.findViewById<TextView>(R.id.listscan_count).text = item.ID_EtiScan_Scan.toString()
            view.findViewById<TextView>(R.id.EtiScan_List_Item_NumberPart).text = item.NumberPart_F


            date.text = item.Date
            time.text = item.Time
            if(item.Status){
                status.text = "Completed"
                status.setTextColor(Color.parseColor("#007E33"))
                status.setTypeface(status.typeface, Typeface.BOLD_ITALIC)
                layitem.setBackgroundColor(Color.parseColor("#cfd8dc"))
            }else{
                status.text = "--"
                view.setOnClickListener {
                    val intent = Intent(view.context,EtiScan_Scan::class.java)
                    intent.putExtra("idScan",item.ID_EtiScan_Scan)
                    view.context.startActivity(intent)
                }
                
                view.setOnLongClickListener {
                    /*val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(item.NumberPart_F)
                    builder.setMessage("Deseas crear un nuevo escaneo con este número de parte?")
                    builder.setPositiveButton("SI", { dialogInterface: DialogInterface, i: Int ->
                        Toast.makeText(view.context, "Click en este item ${position}", Toast.LENGTH_SHORT).show()

                    })
                    builder.setNegativeButton("NO", { dialogInterface: DialogInterface, i: Int ->

                    })
                    builder.show()*/
                    true }
            }
            view.findViewById<TextView>(R.id.EtiScan_List_Item_Usuario).text = item.Nombre

        }
    }
}


