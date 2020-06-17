package com.example.testbottomview.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "student_name") val name: String?,
    @ColumnInfo(name = "student_age") val age: Int?
)