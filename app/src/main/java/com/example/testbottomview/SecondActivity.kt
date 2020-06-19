package com.example.testbottomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.L
import com.example.testbottomview.repository.Student
import com.example.testbottomview.repository.StudentDao
import com.example.testbottomview.repository.StudentDatabase
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    private val viewModel: SecondActivityViewModel by viewModels()
    private lateinit var adapter: MyPageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        adapter = MyPageAdapter()
        mRvStudent.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)
        mRvStudent.addItemDecoration(
            DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL)
        )
        mRvStudent.adapter = adapter

        viewModel.studentList.observe(this, Observer {
            adapter.submitList(it)
            L.i("data changed")
            /*it.addWeakCallback(null,object : PagedList.Callback(){
                override fun onChanged(position: Int, count: Int) {
                }

                override fun onInserted(position: Int, count: Int) {
                }

                override fun onRemoved(position: Int, count: Int) {
                }

            })*/
        })


        mBtnPopulate.setOnClickListener {
            viewModel.insertStudents(*students)
        }

        mBtnClear.setOnClickListener {
            viewModel.deleteAllStudents()
        }

        adapter.setOnRecyclerItemLongClickListener{ _ : View, position: Int ->
            Toast.makeText(SecondActivity@ this, "$position", Toast.LENGTH_SHORT).show()
            val student = adapter.currentList?.get(position)
            student?.let {
                viewModel.deleteStudentById(it.id)
            }
        }
    }

    private val students = arrayOf(
        Student(0,"蓟蝶梦",18),
        Student(0,"广田田",22),
        Student(0,"勾智菱",19),
        Student(0,"黄智敏",20),
        Student(0,"晃名姝",21),
        Student(0,"孟彩娟",12),
        Student(0,"游瑜文",25),
        Student(0,"宁贝晨",15),
        Student(0,"蓟听兰",18),
        Student(0,"石谷雪",24),
        Student(0,"茹添智",21),
        Student(0,"詹星瑶",14),
        Student(0,"班齐心",18),
        Student(0,"瞿乐枫",17),
        Student(0,"邱虹影",12),
        Student(0,"劳一芳",11),
        Student(0,"鱼小妹",15),
        Student(0,"公小霜",12),
        Student(0,"赖悦畅",14),
        Student(0,"乌冉冉",16),
        Student(0,"白子明",17),
        Student(0,"阎元驹",14),
        Student(0,"孙德馨",18),
        Student(0,"姚伟泽",18),
        Student(0,"谭文成",19),
        Student(0,"任景明",17),
        Student(0,"易新立",15),
        Student(0,"陈阳曜",17),
        Student(0,"赵锐藻",20),
        Student(0,"傅乐贤",10),
        Student(0,"钱成仁",17),
        Student(0,"沈宏胜",14),
        Student(0,"曾思远",15),
        Student(0,"吴德惠",16),
        Student(0,"崔永逸",17),
        Student(0,"李光赫",18),
        Student(0,"贺天华",19),
        Student(0,"邓志明",20),
        Student(0,"崔经纶",21),
        Student(0,"尹雨信",22)
    )
}