package com.example.sunofbeachagain.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QuestionEntity::class], version = 1, exportSchema = true)
abstract class SobDataBase : RoomDatabase() {
    abstract fun getQuestionDao(): QuestionDao

    companion object {
        var sobDataBase: SobDataBase? = null

        @Synchronized
        fun getSobDataBase(context: Context): SobDataBase {
            sobDataBase?.let {
                return it
            }

            return Room.databaseBuilder(context, SobDataBase::class.java, "sob.db")
                .build()
                .apply {
                sobDataBase = this
            }
        }
    }
}