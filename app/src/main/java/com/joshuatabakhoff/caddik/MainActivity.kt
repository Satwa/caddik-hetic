package com.joshuatabakhoff.caddik

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val CAMERA_REQUEST_CODE = 4242

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
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
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

    fun openCamera() {
        Toast.makeText(this, "Open camera", Toast.LENGTH_SHORT).show()
    }
}
