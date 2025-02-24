package com.example.raovat_app.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.raovat_app.databinding.FragmentAddNewBinding
import com.example.raovat_app.classes.Image
import com.example.raovat_app.classes.Product
import com.example.raovat_app.classes.Response
import com.example.raovat_app.controllers.ProductController
import com.example.raovat_app.controllers.StoreController
import com.example.raovat_app.models.ProductModels
import com.example.raovat_app.interfaces.ResponseCallback
import com.example.raovat_app.models.StoreModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


class AddNewFragment : Fragment(), ResponseCallback {
    private lateinit var binding:FragmentAddNewBinding
    private var id = ""
    private val productModel = ProductModels()
    private val productController = ProductController(productModel, this)
    private var isLoading = false
    private lateinit var pickerMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var index = -1
    private val imageParts = mutableListOf<MultipartBody.Part>()
    private var storeModel : StoreModel? = null
    private var storeController : StoreController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                // Xử lý URI và cập nhật giao diện
                updateImageView(it)
            } ?: run {
                Log.e("TAG", "onViewCreated: null")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNewBinding.inflate(inflater, container, false)

        storeModel = StoreModel()
        storeController = StoreController(storeModel!!, this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutImgSelected0.setOnClickListener {
            selectImage(0)
        }
        binding.layoutImgSelected1.setOnClickListener {
            selectImage(1)
        }
        binding.layoutImgSelected2.setOnClickListener {
            selectImage(2)
        }
        binding.layoutImgSelected3.setOnClickListener {
            selectImage(3)
        }
        binding.layoutImgSelected4.setOnClickListener {
            selectImage(4)
        }


        id = arguments?.getString("id").toString()

        binding.layoutSave.setOnClickListener{
            saveProduct()
        }

    }

    private fun saveProduct(){
        Toast.makeText(requireContext(), "lưu", Toast.LENGTH_SHORT).show()
        if(!validate()){
            showMessenger("Điền đầy đủ thông tin")

        }else{
            lifecycleScope.launch {
                val product = Product(
                    binding.edNameProduct.text.toString(),
                    binding.edPriceProduct.text.toString().toInt(),
                    binding.edAmount.text.toString().toInt() or (0),
                    binding.edMaterialProduct.text.toString(),
                    binding.edOriginProduct.text.toString(),
                    binding.edTradeMarkProduct.text.toString(),
                    binding.edDescriptionProduct.text.toString(),
                    id.toInt()
                )
                imageParts.clear()
                if(binding.img01.tag != null){
                    addImageToParts(binding.img01.tag.toString(), imageParts)
                }
                if(binding.img11.tag != null){
                    addImageToParts(binding.img11.tag.toString(), imageParts)
                }
                if(binding.img21.tag != null){
                    addImageToParts(binding.img21.tag.toString(), imageParts)
                }
                if(binding.img31.tag != null){
                    addImageToParts(binding.img31.tag.toString(), imageParts)
                }
                if(binding.img41.tag != null){
                    addImageToParts(binding.img41.tag.toString(), imageParts)
                }

                val gson = Gson()
                val productJsonString = gson.toJson(product)
                val productRequestBody =
                    productJsonString.toRequestBody("application/json".toMediaTypeOrNull())
                productController.addProduct(productRequestBody, imageParts)
            }
        }

    }
    private fun validate():Boolean{
        if (binding.edNameProduct.text.toString() == ""){
            return false
        }
        if (binding.edPriceProduct.text.toString() == ""){
            return false
        }
        if (binding.edDescriptionProduct.text.toString() == ""){
            return false
        }
        if (binding.edMaterialProduct.text.toString() == ""){
            return false
        }
        if (binding.edOriginProduct.text.toString() == ""){
            return false
        }
        if (binding.edTradeMarkProduct.text.toString() == ""){
            return false
        }
        return true
    }

    private fun showMessenger(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    //TODO: chọn ảnh  và cập nhật giao diện hình ảnh
    private fun updateImageView(uri: Uri) {
        when (index) {
            0 -> {
                binding.img01.setImageURI(uri)
                binding.img01.visibility = View.VISIBLE
                binding.img01.tag = uri.toString()  // Lưu trữ URI vào tag của ImageView
            }
            1 -> {
                binding.img11.setImageURI(uri)
                binding.img11.visibility = View.VISIBLE
                binding.img11.tag = uri.toString()  // Lưu trữ URI vào tag của ImageView
            }
            2 -> {
                binding.img21.setImageURI(uri)
                binding.img21.visibility = View.VISIBLE
                binding.img21.tag = uri.toString()  // Lưu trữ URI vào tag của ImageView
            }
            3 -> {
                binding.img31.setImageURI(uri)
                binding.img31.visibility = View.VISIBLE
                binding.img31.tag = uri.toString()  // Lưu trữ URI vào tag của ImageView
            }
            4 -> {
                binding.img41.setImageURI(uri)
                binding.img41.visibility = View.VISIBLE
                binding.img41.tag = uri.toString()  // Lưu trữ URI vào tag của ImageView
            }
        }
    }

    private fun selectImage(index : Int) {
        this.index = index
        pickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun addImageToParts(uriString: String, imageParts: MutableList<MultipartBody.Part>) {
        val uri = Uri.parse(uriString)

        // Sử dụng ContentResolver để lấy InputStream
        val inputStream = context?.contentResolver?.openInputStream(uri)

        // Tạo một File tạm thời để lưu hình ảnh
        val tempFile = File(requireContext().cacheDir, "image_${System.currentTimeMillis()}.jpg") 

        inputStream.use { input ->
            if (input != null) {
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output) // Sao chép dữ liệu từ InputStream sang FileOutputStream
                }
            }
        }

        // Kiểm tra xem File có tồn tại không
        if (tempFile.exists()) {
            // Tạo RequestBody từ File tạm thời
            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

            // Tạo MultipartBody.Part
            val body = MultipartBody.Part.createFormData("images", tempFile.name, requestFile)

            // Thêm vào danh sách
            imageParts.add(body)
        } else {
            Log.e("TAG", "File does not exist: $uri")
        }
    }


    override fun onSuccess(response: Response<*>) {
        when (response){
            is Response.Success -> {
                showMessenger(response.value as String)
            }
            is Response.SuccessWithExtra<*,*> -> {
               fillData(response.value as Product, response.value1 as List<Image>)
            }
            is Response.SuccessWith3Params<*,*,*> -> TODO()
        }
    }

    override fun onError(error: String) {
        showMessenger(error)
    }

    private fun fillData(product: Product, images: List<Image>){
        binding.edNameProduct.setText(product.getName())
        binding.edPriceProduct.setText(product.getPrice().toString())
        binding.edMaterialProduct.setText(product.getMaterial())
        binding.edAmount.setText(product.getAmount().toString())
        binding.edOriginProduct.setText(product.getOrigin())
        binding.edTradeMarkProduct.setText(product.getTrademark())
        binding.edDescriptionProduct.setText(product.getDescription())
        for ( i in images.indices){
            when(i){
                0 -> {
                    Glide.with(requireContext()).load(images[i].getUrl()).into(binding.img01)
                    binding.img01.visibility = View.VISIBLE
                }
                1 -> {
                    Glide.with(requireContext()).load(images[i].getUrl()).into(binding.img11)
                    binding.img11.visibility = View.VISIBLE
                }
                2 -> {
                    Glide.with(requireContext()).load(images[i].getUrl()).into(binding.img21)
                    binding.img21.visibility = View.VISIBLE
                }
                3 -> {
                    Glide.with(requireContext()).load(images[i].getUrl()).into(binding.img31)
                    binding.img31.visibility = View.VISIBLE
                }
                4 -> {
                    binding.img41.visibility = View.VISIBLE
                    Glide.with(requireContext()).load(images[i].getUrl()).into(binding.img41)
                }
            }
        }
    }

    fun updateData(key: Int){
        isLoading = true
        lifecycleScope.launch {
            storeController!!.getInfoProduct(key)
        }
    }

    override fun onResume() {
        super.onResume()

        if (isLoading){
            isLoading = false
        }else{
            emptyForm()
        }
    }

    private fun emptyForm(){
        binding.edNameProduct.setText("")
        binding.edPriceProduct.setText("")
        binding.edMaterialProduct.setText("")
        binding.edAmount.setText("")
        binding.edOriginProduct.setText("")
        binding.edTradeMarkProduct.setText("")
        binding.edDescriptionProduct.setText("")
        binding.img01.visibility = View.GONE
        binding.img11.visibility = View.GONE
        binding.img21.visibility = View.GONE
        binding.img31.visibility = View.GONE
        binding.img41.visibility = View.GONE
    }

}

