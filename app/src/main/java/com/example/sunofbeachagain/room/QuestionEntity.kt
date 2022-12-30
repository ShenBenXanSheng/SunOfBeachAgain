package com.example.sunofbeachagain.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionTab")
data class QuestionEntity(
    @ColumnInfo(name = "wendaId", typeAffinity = ColumnInfo.TEXT)
    var wendaId: String,

    @ColumnInfo(name = "userId", typeAffinity = ColumnInfo.TEXT)
    var userId: String,

    @ColumnInfo(name = "avatar", typeAffinity = ColumnInfo.TEXT)
    var avatar: String,

    @ColumnInfo(name = "nickname", typeAffinity = ColumnInfo.TEXT)
    var nickname: String,

    @ColumnInfo(name = "createTime", typeAffinity = ColumnInfo.TEXT)
    var createTime: String,

    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    var title: String,

    @ColumnInfo(name = "viewCount", typeAffinity = ColumnInfo.INTEGER)
    var viewCount: Int,

    @ColumnInfo(name = "thumbUp", typeAffinity = ColumnInfo.INTEGER)
    var thumbUp: Int,

    @ColumnInfo(name = "sob", typeAffinity = ColumnInfo.INTEGER)
    var sob: Int,

    @ColumnInfo(name = "answerCount", typeAffinity = ColumnInfo.INTEGER)
    var answerCount: Int,
) {
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    @PrimaryKey(autoGenerate = true)
    var _id: Int = 0
}
