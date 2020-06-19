package com.example.testbottomview.fragment

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.base.L
import com.example.base.utlis.DensityUtil
import com.example.bean.pixabay.Pixabay
import com.example.common.CommonViewHolder
import com.example.testbottomview.MyApp
import com.example.testbottomview.R
import kotlinx.android.synthetic.main.item_rv_stagger_image.view.*
import kotlin.properties.Delegates

/**
 * ClassName:      DashboardAdapter
 * Description:    DashboardAdapter
 * Author:         mwy
 * CreateDate:     2020/6/19 19:08
 */
class DashboardAdapter : PagedListAdapter<Pixabay.PhotoItem, CommonViewHolder>(DIFF_CALL_BACK){

    companion object{
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Pixabay.PhotoItem>(){
            override fun areItemsTheSame(
                oldItem: Pixabay.PhotoItem,
                newItem: Pixabay.PhotoItem
            ): Boolean {
               return oldItem.photoId == newItem.photoId
            }

            override fun areContentsTheSame(
                oldItem: Pixabay.PhotoItem,
                newItem: Pixabay.PhotoItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rv_stagger_image, parent, false)
        return CommonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val item = getItem(position);
        if (item != null){
            val itemWidth: Int = MyApp.instance.resources.displayMetrics.widthPixels / 2 - DensityUtil.dp2px(MyApp.instance,16.0f)
            val finalHeight = (item.photoHeight / (item.photoWidth * 1.0f / itemWidth)).toInt()
            holder.itemView.layoutParams.height = finalHeight
            L.i("itemWith = $itemWidth ----- $finalHeight")
        }
        Glide.with(holder.itemView)
            .load(getItem(position)?.previewUrl)
            .placeholder(R.drawable.ic_placeholder_400)
            .into(holder.itemView.imageView4)
    }
}