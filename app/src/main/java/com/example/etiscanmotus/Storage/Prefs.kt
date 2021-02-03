package com.example.etiscanmotus.Storage

import android.content.Context

class Prefs(val context: Context) {
    val SHARED_NAME = "Mydtb"
    val API_KEY = "apikey"
    val ID_USUARIO =  "idUser"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun save_apikey (key: String, idUser: Int){
        storage.edit().putString(API_KEY,key).apply();
        storage.edit().putInt(ID_USUARIO,idUser).apply()
    }
    fun get_apikey(): String{
        return storage.getString(API_KEY,"")!!
    }
    fun get_idUser(): Int{
        return storage.getInt(ID_USUARIO,0)
    }
    fun delete_apikey(){
        storage.edit().remove(API_KEY)
        storage.edit().remove(ID_USUARIO)
    }


}
