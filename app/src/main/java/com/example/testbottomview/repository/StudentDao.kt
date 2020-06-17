package com.example.testbottomview.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {
    @Query("SELECT * FROM STUDENT_TABLE")
    fun getAllStudentLive(): LiveData<List<Student>>

    @Insert
    fun insert(vararg student: Student)

    @Delete
    fun deleteStudent(vararg student: Student)

    @Query("DELETE FROM STUDENT_TABLE")
    fun deleteAll()

    @Query("SELECT * FROM STUDENT_TABLE ORDER BY id")
    fun getAllStudents(): DataSource.Factory<Int,Student>

    @Query("SELECT * FROM STUDENT_TABLE WHERE id = :id")
    fun getStudentById(id:Int):Student

    @Query("DELETE FROM STUDENT_TABLE WHERE id = :id")
    fun deleteStudentById(id:Int)
}