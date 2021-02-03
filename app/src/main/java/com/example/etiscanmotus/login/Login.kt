package com.example.etiscanmotus.login
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.etiscanmotus.R
import com.example.etiscanmotus.RetrofitRes.RetrofitRes

class Login : AppCompatActivity() {
    var username:EditText? = null
    var password:EditText? = null
    var btn_login: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Iniciar();
        setEvents()

    }

    private fun login(){
        val user = username?.text.toString()
        val pass = password?.text.toString()

        if(validar_input()){
            RetrofitRes.validar_login(this,user,pass,this)

        }
    }
    private fun validar_input(): Boolean{
        var status: Boolean = true
        if(username?.text?.isEmpty() == true){
            username!!.setError("Se requiere un usuario");
            username!!.requestFocus()
            status = false
        }
        if(password?.text?.isEmpty() == true){
            password!!.setError("Se requiere ingresa password");
            password!!.requestFocus()
            status = false
        }
        return status
    }
    private fun setEvents(){
        btn_login?.setOnClickListener {
            login()
        }
    }
    private fun Iniciar(){
        username = findViewById(R.id.login_username)
        password = findViewById(R.id.login_password)
        btn_login = findViewById(R.id.login_button)
    }
}