package com.example.restomaster.data.repository

import com.example.restomaster.data.local.dao.ReservationDao
import com.example.restomaster.data.local.entity.ReservationEntity
import com.example.restomaster.domain.repository.ReservationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReservationRepositoryImpl @Inject constructor(
    private val reservationDao: ReservationDao
) : ReservationRepository {
    override fun getReservations(storeId: Long, date: String): Flow<List<ReservationEntity>> = reservationDao.getReservations(storeId, date)
    override suspend fun addReservation(reservation: ReservationEntity) = reservationDao.insertReservation(reservation)
    override suspend fun updateReservation(reservation: ReservationEntity) = reservationDao.updateReservation(reservation)
}
