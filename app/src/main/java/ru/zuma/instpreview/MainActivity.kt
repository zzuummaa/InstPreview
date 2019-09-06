package ru.zuma.instpreview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.content.Intent
import android.widget.Toast
import android.R.attr.data
import android.app.Activity
import android.util.Log
import android.view.Menu
import java.nio.file.Files.size
import android.content.ClipData
import android.net.Uri
import android.provider.MediaStore




class MainActivity : AppCompatActivity() {

    private lateinit var imagesEncodedList: ArrayList<String>
    private val LOAD_IMAGE_RESULTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_add_image -> {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                LOAD_IMAGE_RESULTS
            )
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            var imageEncoded: String
            // When an Image is picked
            if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK && null != data) {
                // Get the Image from data

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                imagesEncodedList = ArrayList<String>()
                if (data.data != null) {
                    val mImageUri = data.data
                    Log.i(this@MainActivity.javaClass.simpleName, mImageUri?.path)
                } else {
                    if (data.clipData != null) {
                        val mClipData = data.clipData
                        val mArrayUri = ArrayList<Uri>()
                        for (i in 0 until mClipData!!.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val uri = item.uri
                            mArrayUri.add(uri)
                            Log.i(this@MainActivity.javaClass.simpleName, uri?.path)
                        }
                        Log.i(this@MainActivity.javaClass.simpleName, "Selected Images " + mArrayUri.size)
                    }
                }
            } else {
                Toast.makeText(
                    this, "You haven't picked Image",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Log.w(this@MainActivity.javaClass.simpleName, "Can't load image paths", e)
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                .show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
