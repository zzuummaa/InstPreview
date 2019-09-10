package ru.zuma.instpreview

import android.content.ContentResolver
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(private val itemData: List<Uri>,
                   private val contentResolver: ContentResolver) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ImageAdapter.ImageViewHolder {
        // create a new view
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)

        return ImageViewHolder(itemView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val imageView = holder.itemView.findViewById<ImageView>(R.id.iv)
        imageView.setImageURI(itemData[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = itemData.size
}