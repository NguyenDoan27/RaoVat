    package com.example.raovat_app.models

    import android.util.Log
    import com.example.raovat_app.classes.Address
    import com.example.raovat_app.classes.User
    import com.example.raovat_app.others.PreferenceDataStore
    import com.example.raovat_app.utils.RetrofitClient
    import com.google.gson.Gson
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.flow.firstOrNull
    import kotlinx.coroutines.withContext
    import okhttp3.MultipartBody
    import okhttp3.RequestBody
    import org.json.JSONObject

    sealed class AccountResult<T, E> {
        data class Success<T, E>(val value: T) : AccountResult<T, E>()
        data class Failure<T, E>(val error: E) : AccountResult<T, E>()
    }

    class AccountModel(private val preferenceDataStore: PreferenceDataStore) {

        suspend fun signIn(phone: String, password: String): AccountResult<Boolean, String> {
            return withContext(Dispatchers.IO) {
                try {
                    val user = User(phone, password)
                    val response = RetrofitClient.getInstance().login(user).execute()
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string() ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                        val jsonObject = JSONObject(responseBody)
                        val token = jsonObject.getString("token")
                        preferenceDataStore.saveToken(token)
                        Log.d("token", token)
                        return@withContext AccountResult.Success(true)
                    } else {
                        val errorBody = response.errorBody()?.string() ?: return@withContext AccountResult.Failure("Lỗi không xác định")
                        val errorJson = JSONObject(errorBody)
                        val errorMessage = errorJson.getString("msg")
                        return@withContext AccountResult.Failure(errorMessage)
                    }
                } catch (e: Exception) {
                    return@withContext AccountResult.Failure("Đã xảy ra lỗi không mong muốn")
                }
            }
        }
        suspend fun signUp(phone: String, password: String): AccountResult<Boolean, String> {
            return withContext(Dispatchers.IO){
                try {
                    val user = User(phone, password)
                    val response = RetrofitClient.getInstance().regisUser(user).execute()
                    if(response.isSuccessful){
                        return@withContext AccountResult.Success(true)
                    }else{
                        val errorBody = response.errorBody()?.string() ?: return@withContext AccountResult.Failure("Lỗi không xác định")
                        val errorJson = JSONObject(errorBody)
                        val errorMessage = errorJson.getString("msg")
                        return@withContext AccountResult.Failure(errorMessage)
                    }
                }catch (e: Exception){
                    return@withContext AccountResult.Failure("Đã xảy ra lỗi không mong muốn")
                }
            }
        }

        suspend fun getUser(): AccountResult<User, String> {
            return preferenceDataStore.readToken().firstOrNull()?.let { token ->
                withContext(Dispatchers.IO) {
                    try {
                        val response = RetrofitClient.getInstance().getProfile(token).execute()
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string() ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                            val jsonObject = JSONObject(responseBody)
                            val user = jsonObject.getJSONObject("user")
                            val user1 = User(
                                user.getString("url"),
                                user.getString("phone"),
                                user.getString("name"),
                                "0"
                            )
                            AccountResult.Success(user1)
                        } else {
                            AccountResult.Failure("Không thể lấy thông tin người dùng")
                        }
                    } catch (e: Exception) {
                        AccountResult.Failure(e.message ?: "Lỗi không xác định")
                    }
                }
            } ?: AccountResult.Failure("Không tìm thấy token")
        }

        suspend fun getAddressDefault(): AccountResult<Address, String> {
            return preferenceDataStore.readToken().firstOrNull()?.let { token ->
                withContext(Dispatchers.IO) {
                    try {
                        val response = RetrofitClient.getInstance().getAddressDefault(token).execute()
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string() ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                            val jsonObject = JSONObject(responseBody)
                            val data = jsonObject.getJSONObject("data")
                            val gson = Gson()
                            val address = gson.fromJson(data.toString(), Address::class.java)
                            AccountResult.Success(address)
                        } else {
                            AccountResult.Failure("Không thể lấy thông tin người dùng")
                        }
                    }catch (e: Exception){
                        AccountResult.Failure( "Lỗi không xác định")
                    }
                }
            }?: AccountResult.Failure("Không tìm thấy token")
        }
        suspend fun getAddressList(): AccountResult<List<Address>,String>{
            return preferenceDataStore.readToken().firstOrNull()?.let { token ->
                withContext(Dispatchers.IO){
                    try {
                        val response = RetrofitClient.getInstance().getAddressList(token).execute()
                        if(response.isSuccessful){
                            val responseBody = response.body()?.string()
                            val jsonObject = JSONObject(responseBody!!)
                            val data = jsonObject.getJSONArray("data")
                            run {
                                val gson = Gson()
                                val address = gson.fromJson(data.toString(), Array<Address>::class.java).toList()
                                AccountResult.Success(address)
                            }

                        }else{
                            val responseBody = response.errorBody()?.string() ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                            val jsonObject = JSONObject(responseBody)
                            val msg = jsonObject.getString("msg")
                            AccountResult.Failure(msg)
                        }
                    }catch (_:Exception){
                        AccountResult.Failure("Lỗi không xác định")
                    }
                }
            }?: AccountResult.Failure("Không tìm thấy token")
        }

        suspend fun updateAccount(user: RequestBody, image: MultipartBody.Part?): AccountResult<String, String>{
            return preferenceDataStore.readToken().firstOrNull()?.let { token ->
                withContext(Dispatchers.IO) {
                    try {
                        val response =
                            RetrofitClient.getInstance().updateAccount(token, user, image).execute()
                        if (response.isSuccessful) {
                            val responseBody = response.body()?.string()
                                ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                            val jsonObject = JSONObject(responseBody)
                            val msg = jsonObject.getString("msg")
                            AccountResult.Success(msg)

                        } else {
                            val responseBody = response.errorBody()?.string()
                                ?: return@withContext AccountResult.Failure("Phản hồi rỗng")
                            val jsonObject = JSONObject(responseBody)
                            val msg = jsonObject.getString("msg")
                            AccountResult.Failure(msg)
                        }
                    } catch (e: Exception) {
                        Log.e("TAG", "updateAccount: $e", )
                        AccountResult.Failure("Lỗi không xác định")
                    }
                }
            }?: AccountResult.Failure("Không tìm thấy token")
        }
    }