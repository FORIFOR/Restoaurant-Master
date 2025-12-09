package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.CallRequestEntity;
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
public final class CallRequestDao_Impl implements CallRequestDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CallRequestEntity> __insertionAdapterOfCallRequestEntity;

  private final EntityDeletionOrUpdateAdapter<CallRequestEntity> __updateAdapterOfCallRequestEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkAsHandled;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CallRequestDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCallRequestEntity = new EntityInsertionAdapter<CallRequestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `call_requests` (`id`,`storeId`,`tableId`,`type`,`status`,`createdAt`,`handledAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallRequestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getHandledAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHandledAt());
        }
      }
    };
    this.__updateAdapterOfCallRequestEntity = new EntityDeletionOrUpdateAdapter<CallRequestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `call_requests` SET `id` = ?,`storeId` = ?,`tableId` = ?,`type` = ?,`status` = ?,`createdAt` = ?,`handledAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallRequestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getHandledAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHandledAt());
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfMarkAsHandled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE call_requests SET status = 'handled', handledAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM call_requests";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final CallRequestEntity callRequest,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCallRequestEntity.insertAndReturnId(callRequest);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CallRequestEntity callRequest,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCallRequestEntity.handle(callRequest);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markAsHandled(final long id, final long handledAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkAsHandled.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, handledAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkAsHandled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CallRequestEntity>> getActiveCallRequests(final long storeId) {
    final String _sql = "SELECT * FROM call_requests WHERE storeId = ? AND status = 'new' ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_requests"}, new Callable<List<CallRequestEntity>>() {
      @Override
      @NonNull
      public List<CallRequestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHandledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "handledAt");
          final List<CallRequestEntity> _result = new ArrayList<CallRequestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallRequestEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpHandledAt;
            if (_cursor.isNull(_cursorIndexOfHandledAt)) {
              _tmpHandledAt = null;
            } else {
              _tmpHandledAt = _cursor.getLong(_cursorIndexOfHandledAt);
            }
            _item = new CallRequestEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpHandledAt);
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

  @Override
  public Flow<List<CallRequestEntity>> getAllCallRequests(final long storeId) {
    final String _sql = "SELECT * FROM call_requests WHERE storeId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_requests"}, new Callable<List<CallRequestEntity>>() {
      @Override
      @NonNull
      public List<CallRequestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHandledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "handledAt");
          final List<CallRequestEntity> _result = new ArrayList<CallRequestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallRequestEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpHandledAt;
            if (_cursor.isNull(_cursorIndexOfHandledAt)) {
              _tmpHandledAt = null;
            } else {
              _tmpHandledAt = _cursor.getLong(_cursorIndexOfHandledAt);
            }
            _item = new CallRequestEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpHandledAt);
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
