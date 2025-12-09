package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.ReservationEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReservationDao_Impl implements ReservationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReservationEntity> __insertionAdapterOfReservationEntity;

  private final EntityDeletionOrUpdateAdapter<ReservationEntity> __updateAdapterOfReservationEntity;

  public ReservationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReservationEntity = new EntityInsertionAdapter<ReservationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reservations` (`id`,`storeId`,`date`,`time`,`name`,`phone`,`peopleCount`,`assignedTableId`,`note`,`status`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReservationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getTime());
        statement.bindString(5, entity.getName());
        if (entity.getPhone() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhone());
        }
        statement.bindLong(7, entity.getPeopleCount());
        if (entity.getAssignedTableId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getAssignedTableId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNote());
        }
        statement.bindString(10, entity.getStatus());
      }
    };
    this.__updateAdapterOfReservationEntity = new EntityDeletionOrUpdateAdapter<ReservationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reservations` SET `id` = ?,`storeId` = ?,`date` = ?,`time` = ?,`name` = ?,`phone` = ?,`peopleCount` = ?,`assignedTableId` = ?,`note` = ?,`status` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReservationEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getTime());
        statement.bindString(5, entity.getName());
        if (entity.getPhone() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPhone());
        }
        statement.bindLong(7, entity.getPeopleCount());
        if (entity.getAssignedTableId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getAssignedTableId());
        }
        if (entity.getNote() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNote());
        }
        statement.bindString(10, entity.getStatus());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insertReservation(final ReservationEntity reservation,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfReservationEntity.insert(reservation);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReservation(final ReservationEntity reservation,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfReservationEntity.handle(reservation);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReservationEntity>> getReservations(final long storeId, final String date) {
    final String _sql = "SELECT * FROM reservations WHERE storeId = ? AND date = ? ORDER BY time ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    _argIndex = 2;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"reservations"}, new Callable<List<ReservationEntity>>() {
      @Override
      @NonNull
      public List<ReservationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "time");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfAssignedTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "assignedTableId");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final List<ReservationEntity> _result = new ArrayList<ReservationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReservationEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpTime;
            _tmpTime = _cursor.getString(_cursorIndexOfTime);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final int _tmpPeopleCount;
            _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            final Long _tmpAssignedTableId;
            if (_cursor.isNull(_cursorIndexOfAssignedTableId)) {
              _tmpAssignedTableId = null;
            } else {
              _tmpAssignedTableId = _cursor.getLong(_cursorIndexOfAssignedTableId);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            _item = new ReservationEntity(_tmpId,_tmpStoreId,_tmpDate,_tmpTime,_tmpName,_tmpPhone,_tmpPeopleCount,_tmpAssignedTableId,_tmpNote,_tmpStatus);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
