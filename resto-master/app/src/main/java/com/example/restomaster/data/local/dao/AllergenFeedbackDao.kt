package com.example.restomaster.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.restomaster.data.local.entity.AllergenFeedbackEntity

@Dao
interface AllergenFeedbackDao {
    @Insert
    suspend fun insert(feedback: AllergenFeedbackEntity)
}
