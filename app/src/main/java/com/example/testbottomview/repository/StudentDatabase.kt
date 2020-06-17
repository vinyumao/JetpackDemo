package com.example.testbottomview.repository

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.testbottomview.MyApp

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun getStudentDao(): StudentDao

    companion object {
        //单例 double check
        val instance: StudentDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            Room.databaseBuilder(
                MyApp.instance,
                StudentDatabase::class.java,
                "student_database")
                .build()
        }
    }
}