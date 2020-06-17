package com.example.testbottomview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testbottomview.repository.Student
import kotlinx.android.synthetic.main.item_rv_student.view.*
class MyPageAdapter : PagedListAdapter<Student, MyPageAdapter.MyViewHolder>(DIFF_CALLBACK) {


    private var mLongClickListener: ((view:View,position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_student,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student = getItem(position)
        if (student != null){
            holder.itemView.mTvId.text = student.id.toString()
            holder.itemView.mTvName.text = student.name
            holder.itemView.mTvAge.text = "${student.age}岁"
        }else{
            holder.itemView.mTvName.text = "no data"
        }
        holder.itemView.setOnLongClickListener {
            //不能用onBindViewHolder 参数的position 因为那是老的position 要用holder.adapterPosition
            mLongClickListener?.invoke(it,holder.adapterPosition)
            true
        }
    }
     class MyViewHolder: RecyclerView.ViewHolder {
        constructor(itemView: View) : super(itemView) {
        }
    }

    companion object{
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Student>(){
            override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean {
//                return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.age == newItem.age
                return oldItem == newItem
            }

        }
    }
    fun setOnRecyclerItemLongClickListener(longClickListener: ((view:View,position: Int) -> Unit)?){
        mLongClickListener = longClickListener
    }

}

