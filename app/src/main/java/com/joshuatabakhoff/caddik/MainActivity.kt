package com.joshuatabakhoff.caddik

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val CAMERA_PERMISSION_REQUEST_CODE = 4242
    val IMAGE_CAPTURE_REQUEST_CODE = 1337

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.scanButton -> {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }else{
                    // Ask for the permission.
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCamera()
                } else {
                    Toast.makeText(this, "L'accès à votre caméra est nécessaire, merci de l'activer dans vos réglages.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun openCamera() {
        Toast.makeText(this, "Open camera", Toast.LENGTH_SHORT).show()
        /*Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)
            }
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == IMAGE_CAPTURE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap

            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .build()
            //imageView.setImageBitmap(imageBitmap)
        }*/
    }
}
