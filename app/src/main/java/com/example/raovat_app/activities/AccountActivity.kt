package com.example.raovat_app.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.raovat_app.R
import com.example.raovat_app.adapters.AddressAdapter
import com.example.raovat_app.classes.Address
import com.example.raovat_app.classes.Response
import com.example.raovat_app.classes.User
import com.example.raovat_app.controllers.AccountController
import com.example.raovat_app.controllers.AddressController
import com.example.raovat_app.databinding.ActivityAccountBinding
import com.example.raovat_app.interfaces.IAddressItemClick
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.AccountModel
import com.example.raovat_app.models.AddressModel
import com.example.raovat_app.others.PreferenceDataStore
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class AccountActivity : AppCompatActivity(), ResponseCallback, IAddressItemClick, AdapterView.OnItemSelectedListener{
    private lateinit var binding: ActivityAccountBinding
    private var preferencesDataStore: PreferenceDataStore? = null
    private var accountController: AccountController? = null
    private var accountModel: AccountModel? = null
    private var addressAdapter: AddressAdapter? = null
    private var addressDialog: Dialog? = null
    private var image: MultipartBody.Part? = null
    private var checkAvt: Boolean = true
    private var nameKind: String? = null
    private var addressModel: AddressModel? = null
    private var addressController: AddressController? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferencesDataStore = PreferenceDataStore(this)
        accountModel = AccountModel(preferencesDataStore!!)
        accountController = AccountController(accountModel!!, this)
        addressDialog = Dialog(binding.root.context)
        addressModel = AddressModel(preferencesDataStore!!)
        addressController = AddressController(addressModel!!, this)

        binding.btnSave.setOnClickListener {
            if (validateUserForm()){
                updateAccount()
            }
        }
        val pick = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                checkAvt = false
                convertImage(it)
                binding.imgAvt.setImageURI(it)
            } ?: run {
                Log.e("TAG", "onViewCreated: null")
            }
        }

        binding.imgAvt.setOnClickListener {
            pick.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            checkAvt = true
        }

        binding.layoutAddAddress.setOnClickListener{
            val address = Address()

            showAddressDialog(address)
        }
    }


    override fun onResume() {
        super.onResume()

        getDetailAccount()
        getAddresses()
    }

    private fun showMessage(message: String){
        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getDetailAccount(){
        lifecycleScope.launch {
            accountController!!.getUser()
        }
    }

    private fun updateAccount(){
        val user = User(binding.edPhone.text.toString(), binding.edName.text.toString(), "")
        val gson = Gson()
        val json = gson.toJson(user)
        val body =
            json.toRequestBody("application/json".toMediaTypeOrNull())
        lifecycleScope.launch {
            accountController!!.updateAccount(body, image)
        }
    }

    private fun getAddresses(){
        lifecycleScope.launch {
            accountController!!.getAddressList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onSuccess(response: Response<*>) {
        when (response){
            is Response.Success -> {
                fillDate(response.value as User)
            }
            is Response.SuccessWithExtra<*,*> -> {
                addressAdapter = AddressAdapter(response.value as List<Address>, this)
                binding.rcvAddress.layoutManager = LinearLayoutManager(binding.root.context)
                binding.rcvAddress.adapter = addressAdapter
            }
            is Response.SuccessWith3Params<*,*,*> -> {
                showMessage(response.value.toString())
            }
        }
    }

    private fun fillDate(user: User){
        if (user.getAvatar() != "null"){
            Glide.with(binding.root.context).load(user.getAvatar()).into(binding.imgAvt)
        }else if (checkAvt){
            binding.imgAvt.setImageResource(R.drawable.avatar)
        }
        binding.edName.setText(user.getName())
        binding.edPhone.setText(user.getPhone())
    }

    override fun onError(error: String) {
        showMessage(error)
    }

    override fun onItemClick(address: Address) {
        showAddressDialog(address)
    }
    private fun showAddressDialog(address: Address){

        addressDialog!!.setContentView(R.layout.address_dialog)
        val spinner = addressDialog!!.findViewById<Spinner>(R.id.spKindName)
        val edId = addressDialog!!.findViewById<EditText>(R.id.edId)
        val edName = addressDialog!!.findViewById<EditText>(R.id.edNameAddress)
        val edPhone = addressDialog!!.findViewById<EditText>(R.id.edPhoneAddress)
        val edProvince = addressDialog!!.findViewById<EditText>(R.id.edProvince)
        val edDistrict = addressDialog!!.findViewById<EditText>(R.id.edDistrict)
        val edWard = addressDialog!!.findViewById<EditText>(R.id.edWard)
        val edDetail = addressDialog!!.findViewById<EditText>(R.id.edDetail)
        val layoutParams = addressDialog!!.window?.attributes
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        addressDialog!!.window?.attributes = layoutParams
        edId.setText(address.getIdAddress().toString())
        edName.setText(address.getName())
        edPhone.setText(address.getPhone())
        edProvince.setText(address.getCity())
        edDistrict.setText(address.getDistrict())
        edWard.setText(address.getWard())
        edDetail.setText(address.getVillage())
        nameKind = address.getNameKind()
        ArrayAdapter.createFromResource(binding.root.context, R.array.kindName, android.R.layout.simple_spinner_item).also {
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            if (address.getNameKind().equals("Nhà riêng")){
                spinner.setSelection(0)
            }else if (address.getNameKind().equals("Cơ quan")){
                spinner.setSelection(1)
            }else{
                spinner.setSelection(0)
            }
            spinner.onItemSelectedListener = this
        }
        addressDialog!!.findViewById<Button>(R.id.btnSaveAddress).setOnClickListener {
            val address1 = Address(
                edName.text.toString(),
                edPhone.text.toString(),
                edProvince.text.toString(),
                edDistrict.text.toString(),
                edWard.text.toString(),
                edDetail.text.toString(),
                nameKind
            )
            if (address.getIdAddress() != null && validateAddress(address1)){
                //cập nhật địa chỉ
                lifecycleScope.launch {
                    addressController!!.updateAddress(edId.text.toString().toInt(), address1)
                }
            }else if (validateAddress(address1)){
                //thêm địa chỉ mới
                lifecycleScope.launch {
                    addressController!!.addAddress(address1)
                }
            }else{
                showMessage("Vui lòng kiểm tra lại thông tin nhập vào")
                return@setOnClickListener
            }
            //đóng dialog và cập nhật dữ liệu trên màn hình
            addressDialog!!.dismiss()
            onResume()
        }
        addressDialog!!.show()
    }

    private fun validateUserForm(): Boolean{
        var isValid = true
        if (binding.edName.text.toString().isEmpty() || binding.edPhone.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }


    private fun convertImage(uri: Uri){
        val inputStream = binding.root.context.contentResolver?.openInputStream(uri)

        val tempFile = File(binding.root.context.cacheDir, "image_${System.currentTimeMillis()}.jpg")

        inputStream.use { input ->
            if (input != null) {
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
        }

        if (tempFile.exists()) {
            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

            image = MultipartBody.Part.createFormData("images", tempFile.name, requestFile)
        } else {
            Log.e("TAG", "File does not exist: $uri")
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        nameKind = p0?.getItemAtPosition(p2).toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
     private fun validateAddress( address: Address): Boolean{
         val regex = Regex("""(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b""")
         if(address.getName().isNullOrBlank() || address.getPhone().isNullOrBlank() || address.getCity().isNullOrBlank() ||
             address.getDistrict().isNullOrBlank() || address.getVillage().isNullOrBlank() || address.getWard().isNullOrBlank()
             ){
             return false
         }
         if (!regexPhoneNum(address.getPhone()!!)){
             return false
         }
         return true
     }

    private fun regexPhoneNum(number: String): Boolean {
        val regex = Regex("""(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\b""")
        return regex.matches(number)
    }

}