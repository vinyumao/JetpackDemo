package com.example.testbottomview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.testbottomview.repository.Student
import com.example.testbottomview.repository.StudentDao
import com.example.testbottomview.repository.StudentDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecondActivityViewModel(
    application: Application
) : AndroidViewModel(application) {
    private var studentDao: StudentDao = StudentDatabase.instance.getStudentDao()
    var studentList: LiveData<PagedList<Student>>

    init {
        studentList =
            LivePagedListBuilder(studentDao.getAllStudents(), 20).build()
    }

    fun insertStudents(vararg student: Student) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                studentDao.insert(*student)
            }
        }
    }

    fun deleteAllStudents() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                studentDao.deleteAll()
            }
        }
    }

    fun deleteStudent(vararg students: Student){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                studentDao.deleteStudent(*students)
            }
        }
    }

    fun deleteStudentById(id: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                studentDao.deleteStudentById(id)
            }
        }
    }
}