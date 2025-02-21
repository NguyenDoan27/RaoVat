package com.example.raovat_app.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.raovat_app.MainActivity
import com.example.raovat_app.classes.Response
import com.example.raovat_app.databinding.ActivityLoginBinding
import com.example.raovat_app.controllers.AccountController
import com.example.raovat_app.models.AccountModel
import com.example.raovat_app.others.PreferenceDataStore
import com.example.raovat_app.interfaces.ResponseCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(), ResponseCallback {
    private lateinit var binding: ActivityLoginBinding
    private var loginFlag:Boolean = false
    private val preferenceDataStore = PreferenceDataStore(this)
    private val loginModel = AccountModel(preferenceDataStore)
    private val accountController = AccountController(loginModel, this)

    @SuppressLint("SuspiciousIndentation", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvChangeLayout.setOnClickListener {
            if(!loginFlag){
                loginFlag = true
                binding.tvChangeLayout.text = "Bạn đã có tài khoản? Đăng nhập ngay"
                binding.layoutLogin.visibility = View.GONE
                binding.layoutSignup.visibility = View.VISIBLE
            }else{
                loginFlag = false
                binding.tvChangeLayout.text = "Bạn chưa có tài khoản? Đăng ký ngay"
                binding.layoutLogin.visibility = View.VISIBLE
                binding.layoutSignup.visibility = View.GONE
                binding.layoutSignup1.visibility = View.VISIBLE
                binding.layoutSignup2.visibility = View.GONE
                binding.edNameSignUp.setText("")
                binding.edPasswordSignUp.setText("")
                binding.edRePasswordSignUp.setText("")
                binding.edPhoneNumSignUp.setText("")
            }
        }
        binding.btnNextSignUp.setOnClickListener {
            if(binding.edPhoneNumSignUp.text?.isEmpty() == true){
                showMessenger("Vui lòng nhập số điện thoại")
            }else if(regexPhoneNum(binding.edPhoneNumSignUp.text.toString())){
                binding.layoutSignup1.visibility = View.GONE
                binding.layoutSignup2.visibility = View.VISIBLE
            }else{
                showMessenger("Số điện thoại không hợp lệ")
            }
        }
        binding.tvBackSingUp.setOnClickListener {
            binding.layoutSignup1.visibility = View.VISIBLE
            binding.layoutSignup2.visibility = View.GONE
            binding.edNameSignUp.setText("")
            binding.edPasswordSignUp.setText("")
            binding.edRePasswordSignUp.setText("")
        }

        binding.btnRegisUser.setOnClickListener {
            if (checkEmptySignUp()){
                if(binding.edPasswordSignUp.text.toString() == binding.edRePasswordSignUp.text.toString()){
                    lifecycleScope.launch {
                        accountController.signUp(binding.edPhoneNumSignUp.text.toString(), binding.edPasswordSignUp.text.toString())
                    }
                }else{
                    showMessenger("2 mật khẩu không trùng khớp")
                }
            }else{
                showMessenger("Vui lòng nhập đầy đủ thông tin!!")
            }
        }

        binding.btnLogIn.setOnClickListener {
            if (checkEmptySigIn()) {
                lifecycleScope.launch {
                     accountController.signIn(
                        binding.edPhoneNumLogIn.text.toString(),
                        binding.edPasswordLogIn.text.toString()
                    )
                }
            }
        }
    }

    private fun regexPhoneNum(number: String): Boolean {
        val regex = Regex("""(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b""")
        return regex.matches(number)
    }

    private fun checkEmptySignUp():Boolean {
        if(binding.edNameSignUp.text.isNullOrBlank()){
            return false
        }
        if (binding.edPasswordSignUp.text.isNullOrBlank()){
            return false
        }
        return !binding.edRePasswordSignUp.text.isNullOrBlank()
    }
    private fun checkEmptySigIn():Boolean{
        if(binding.edPhoneNumLogIn.text.isNullOrBlank()){
            return false
        }
        return !binding.edPasswordLogIn.text.isNullOrBlank()
    }

     private fun navigateMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun showMessenger(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }

    override fun onSuccess(response: Response<*>) {
        when(response){
            is Response.Success -> {
                if (response.value as Boolean){
                    navigateMainActivity()
                }
            }
            is Response.SuccessWithExtra<*,*> -> TODO()
            is Response.SuccessWith3Params<*,*,*> -> TODO()
        }
    }

    override fun onError(error: String) {
       showMessenger(error)
    }

}