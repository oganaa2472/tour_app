package com.example.survey.data.remote.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    
    fun bitmapToMultipartBody(bitmap: Bitmap, fieldName: String = "photo"): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        
        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        
        return MultipartBody.Part.createFormData(fieldName, "profile_photo.jpg", requestBody)
    }
    
    fun bitmapToFile(context: Context, bitmap: Bitmap, fileName: String = "profile_photo.jpg"): File {
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            outputStream.close()
        }
        
        return file
    }
    
    fun fileToMultipartBody(file: File, fieldName: String = "photo"): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestBody)
    }
}
