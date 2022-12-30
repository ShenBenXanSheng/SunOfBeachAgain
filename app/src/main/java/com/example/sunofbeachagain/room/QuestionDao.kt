package com.example.sunofbeachagain.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDao {
    @Insert
    fun insertQuestionData(questionEntity: QuestionEntity)

    @Query("DELETE FROM QUESTIONTAB")
    fun clearQuestionList()

    @Query("DELETE FROM QUESTIONTAB WHERE wendaId = :keyword")
    fun deleteQuestionData(keyword:String)

    @Query("SELECT * FROM QUESTIONTAB")
    fun queryQuestionList():LiveData<List<QuestionEntity>>

    @Query("SELECT * FROM QUESTIONTAB WHERE title LIKE '%' || :keyword || '%'")
    fun queryQuestionData(keyword: String):List<QuestionEntity>

}