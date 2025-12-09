package com.example.restomaster.domain.repository

import com.example.restomaster.data.local.entity.ReservationEntity
import kotlinx.coroutines.flow.Flow

interface ReservationRepository {
    fun getReservations(storeId: Long, date: String): Flow<List<ReservationEntity>>
    suspend fun addReservation(reservation: ReservationEntity)
    suspend fun updateReservation(reservation: ReservationEntity)
}
