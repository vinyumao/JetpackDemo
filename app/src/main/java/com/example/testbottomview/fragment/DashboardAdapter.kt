package com.example.testbottomview.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.base.utlis.DensityUtil
import com.example.common.CommonViewHolder
import com.example.common.bean.Pixabay
import com.example.net.RequestStatus
import com.example.testbottomview.MyApp
import com.example.testbottomview.R
import kotlinx.android.synthetic.main.item_rv_load_more.view.*
import kotlinx.android.synthetic.main.item_rv_stagger_image.view.*

/**
 * ClassName:      DashboardAdapter
 * Description:    DashboardAdapter
 * Author:         mwy
 * CreateDate:     2020/6/19 19:08
 */
class DashboardAdapter(private var viewModel: DashboardViewModel) :
    PagedListAdapter<Pixabay.PhotoItem, RecyclerView.ViewHolder>(DIFF_CALL_BACK) {
    private var hasFooter = false
    private var requestStatus: RequestStatus? = null

    companion object {
        val DIFF_CALL_BACK = object : DiffUtil.ItemCallback<Pixabay.PhotoItem>() {
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

    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.item_rv_load_more else R.layout.item_rv_stagger_image
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_rv_stagger_image -> ImageViewHolder.newInstance(parent)
            else -> FootViewHolder.newInstance(parent).also {
                it.itemView.mTvLoadState.setOnClickListener {
                    viewModel.retryFetchData()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            val item = getItem(position)
            item?.let {
                holder.bind(it)
            }
        } else if (holder is FootViewHolder) {
            holder.bind(requestStatus)
        }
    }

    class ImageViewHolder(itemView: View) : CommonViewHolder(itemView) {
        companion object {
            fun newInstance(view: ViewGroup): ImageViewHolder {
                val itemView = LayoutInflater.from(view.context)
                    .inflate(R.layout.item_rv_stagger_image, view, false)
                return ImageViewHolder(itemView)
            }
        }

        fun bind(photoItem: Pixabay.PhotoItem) {
            val itemWidth: Int =
                MyApp.instance.resources.displayMetrics.widthPixels / 2 - DensityUtil.dp2px(
                    MyApp.instance,
                    16.0f
                )
            val finalHeight =
                (photoItem.photoHeight / (photoItem.photoWidth * 1.0f / itemWidth)).toInt()
            itemView.layoutParams.height = photoItem.photoHeight
            itemView.mTvPicSize.text = String.format(MyApp.instance.getString(R.string.img_text),photoItem.photoWidth,photoItem.photoHeight,adapterPosition)
            Glide.with(itemView)
                .load(photoItem.previewUrl)
                .placeholder(R.drawable.ic_placeholder_400)
                .into(itemView.imageView4)
        }
    }

    class FootViewHolder(itemView: View) : CommonViewHolder(itemView) {
        companion object {
            fun newInstance(view: ViewGroup): FootViewHolder {
                val itemView = LayoutInflater.from(view.context)
                    .inflate(R.layout.item_rv_load_more, view, false)
                (itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
                return FootViewHolder(itemView)
            }
        }

        fun bind(requestStatus: RequestStatus?) {
            itemView.run {
                when(requestStatus){
                    RequestStatus.FAILED ->{
                        mTvLoadState.text = "加载失败,点击重试"
                        mTvLoadState.isClickable = true
                        mProgressBar.visibility = View.GONE
                    }
                    RequestStatus.COMPLETED ->{
                        mTvLoadState.text = "没有更多了,我可是有底线的"
                        mTvLoadState.isClickable = false
                        mProgressBar.visibility = View.GONE
                    }
                    else ->{
                        mTvLoadState.text = "正在加载中"
                        mTvLoadState.isClickable = true
                        mProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    fun updateRequestStatus(requestStatus: RequestStatus?) {
        this.requestStatus = requestStatus
        if (requestStatus == RequestStatus.INITIAL_LOADING) hideFoot() else showFoot()
    }

    private fun hideFoot(){
        if (hasFooter && itemCount >1){
            notifyItemRemoved(itemCount -1)
        }
        hasFooter = false
    }

    private fun showFoot(){
        if(hasFooter && itemCount >1){
            notifyItemChanged(itemCount -1)
        }else{
            hasFooter = true
            notifyItemInserted(itemCount -1)
        }
    }
}