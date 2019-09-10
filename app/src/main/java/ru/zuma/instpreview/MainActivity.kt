package ru.zuma.instpreview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var imageURIList: ArrayList<Uri>
    private lateinit var imageURISet:  HashSet<Uri>
    private val LOAD_IMAGE_RESULTS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        imageURISet  = HashSet()
        imageURIList = ArrayList()

        val colCount = 3

        imageAdapter = ImageAdapter(imageURIList, this.contentResolver)
        rvImages.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, colCount)
            adapter = imageAdapter
        }
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
            // When an Image is picked
            if (requestCode == LOAD_IMAGE_RESULTS && resultCode == Activity.RESULT_OK && null != data) {
                // Get the Image from data


                if (data.data != null) {
                    val mImageUri = data.data ?: throw NullPointerException()
                    if (imageURISet.add(mImageUri)) imageURIList.add(0, mImageUri)
                    Log.i(this@MainActivity.javaClass.simpleName, mImageUri?.path)
                } else {
                    if (data.clipData != null) {
                        val mClipData = data.clipData
                        for (i in 0 until mClipData!!.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val uri = item.uri
                            if (imageURISet.add(uri)) imageURIList.add(0, uri)
                            Log.i(this@MainActivity.javaClass.simpleName, uri?.path)
                        }
                    }
                }
                imageAdapter.notifyDataSetChanged()
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
