package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.BillEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class BillDao_Impl implements BillDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BillEntity> __insertionAdapterOfBillEntity;

  private final EntityDeletionOrUpdateAdapter<BillEntity> __updateAdapterOfBillEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeletePaidBills;

  public BillDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBillEntity = new EntityInsertionAdapter<BillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bills` (`id`,`table_id`,`status`,`open_at`,`closed_at`,`people_count`,`subtotal`,`tax`,`total_amount`,`total_tax_10`,`total_tax_8`,`paid_amount`,`payment_method`,`note`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTableId());
        statement.bindString(3, entity.getStatus());
        statement.bindLong(4, entity.getOpenAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getClosedAt());
        }
        if (entity.getPeopleCount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPeopleCount());
        }
        statement.bindLong(7, entity.getSubtotal());
        statement.bindLong(8, entity.getTax());
        statement.bindLong(9, entity.getTotalAmount());
        statement.bindLong(10, entity.getTotalTax10());
        statement.bindLong(11, entity.getTotalTax8());
        statement.bindLong(12, entity.getPaidAmount());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPaymentMethod());
        }
        if (entity.getNote() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNote());
        }
      }
    };
    this.__updateAdapterOfBillEntity = new EntityDeletionOrUpdateAdapter<BillEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bills` SET `id` = ?,`table_id` = ?,`status` = ?,`open_at` = ?,`closed_at` = ?,`people_count` = ?,`subtotal` = ?,`tax` = ?,`total_amount` = ?,`total_tax_10` = ?,`total_tax_8` = ?,`paid_amount` = ?,`payment_method` = ?,`note` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getTableId());
        statement.bindString(3, entity.getStatus());
        statement.bindLong(4, entity.getOpenAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getClosedAt());
        }
        if (entity.getPeopleCount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getPeopleCount());
        }
        statement.bindLong(7, entity.getSubtotal());
        statement.bindLong(8, entity.getTax());
        statement.bindLong(9, entity.getTotalAmount());
        statement.bindLong(10, entity.getTotalTax10());
        statement.bindLong(11, entity.getTotalTax8());
        statement.bindLong(12, entity.getPaidAmount());
        if (entity.getPaymentMethod() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getPaymentMethod());
        }
        if (entity.getNote() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getNote());
        }
        statement.bindLong(15, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bills";
        return _query;
      }
    };
    this.__preparedStmtOfDeletePaidBills = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bills WHERE status = 'PAID'";
        return _query;
      }
    };
  }

  @Override
  public Object insertBill(final BillEntity bill, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBillEntity.insertAndReturnId(bill);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBill(final BillEntity bill, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBillEntity.handle(bill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
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
  public Object deletePaidBills(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeletePaidBills.acquire();
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
          __preparedStmtOfDeletePaidBills.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getOpenBillForTable(final long tableId,
      final Continuation<? super BillEntity> $completion) {
    final String _sql = "SELECT * FROM bills WHERE table_id = ? AND status = 'OPEN' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tableId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BillEntity>() {
      @Override
      @Nullable
      public BillEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "table_id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "open_at");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "people_count");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfTotalTax10 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_10");
          final int _cursorIndexOfTotalTax8 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_8");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_amount");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final BillEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpOpenAt;
            _tmpOpenAt = _cursor.getLong(_cursorIndexOfOpenAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTax;
            _tmpTax = _cursor.getInt(_cursorIndexOfTax);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final int _tmpTotalTax10;
            _tmpTotalTax10 = _cursor.getInt(_cursorIndexOfTotalTax10);
            final int _tmpTotalTax8;
            _tmpTotalTax8 = _cursor.getInt(_cursorIndexOfTotalTax8);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _result = new BillEntity(_tmpId,_tmpTableId,_tmpStatus,_tmpOpenAt,_tmpClosedAt,_tmpPeopleCount,_tmpSubtotal,_tmpTax,_tmpTotalAmount,_tmpTotalTax10,_tmpTotalTax8,_tmpPaidAmount,_tmpPaymentMethod,_tmpNote);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBill(final long billId, final Continuation<? super BillEntity> $completion) {
    final String _sql = "SELECT * FROM bills WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BillEntity>() {
      @Override
      @Nullable
      public BillEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "table_id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "open_at");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "people_count");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfTotalTax10 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_10");
          final int _cursorIndexOfTotalTax8 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_8");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_amount");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final BillEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpOpenAt;
            _tmpOpenAt = _cursor.getLong(_cursorIndexOfOpenAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTax;
            _tmpTax = _cursor.getInt(_cursorIndexOfTax);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final int _tmpTotalTax10;
            _tmpTotalTax10 = _cursor.getInt(_cursorIndexOfTotalTax10);
            final int _tmpTotalTax8;
            _tmpTotalTax8 = _cursor.getInt(_cursorIndexOfTotalTax8);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _result = new BillEntity(_tmpId,_tmpTableId,_tmpStatus,_tmpOpenAt,_tmpClosedAt,_tmpPeopleCount,_tmpSubtotal,_tmpTax,_tmpTotalAmount,_tmpTotalTax10,_tmpTotalTax8,_tmpPaidAmount,_tmpPaymentMethod,_tmpNote);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BillEntity> getBillFlow(final long billId) {
    final String _sql = "SELECT * FROM bills WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<BillEntity>() {
      @Override
      @Nullable
      public BillEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "table_id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "open_at");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "people_count");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfTotalTax10 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_10");
          final int _cursorIndexOfTotalTax8 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_8");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_amount");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final BillEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpOpenAt;
            _tmpOpenAt = _cursor.getLong(_cursorIndexOfOpenAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTax;
            _tmpTax = _cursor.getInt(_cursorIndexOfTax);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final int _tmpTotalTax10;
            _tmpTotalTax10 = _cursor.getInt(_cursorIndexOfTotalTax10);
            final int _tmpTotalTax8;
            _tmpTotalTax8 = _cursor.getInt(_cursorIndexOfTotalTax8);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _result = new BillEntity(_tmpId,_tmpTableId,_tmpStatus,_tmpOpenAt,_tmpClosedAt,_tmpPeopleCount,_tmpSubtotal,_tmpTax,_tmpTotalAmount,_tmpTotalTax10,_tmpTotalTax8,_tmpPaidAmount,_tmpPaymentMethod,_tmpNote);
          } else {
            _result = null;
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
  public Object getClosedBillsInRange(final long from, final long to,
      final Continuation<? super List<BillEntity>> $completion) {
    final String _sql = "SELECT * FROM bills WHERE closed_at BETWEEN ? AND ? AND status = 'PAID'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillEntity>>() {
      @Override
      @NonNull
      public List<BillEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "table_id");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOpenAt = CursorUtil.getColumnIndexOrThrow(_cursor, "open_at");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "people_count");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTax = CursorUtil.getColumnIndexOrThrow(_cursor, "tax");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "total_amount");
          final int _cursorIndexOfTotalTax10 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_10");
          final int _cursorIndexOfTotalTax8 = CursorUtil.getColumnIndexOrThrow(_cursor, "total_tax_8");
          final int _cursorIndexOfPaidAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "paid_amount");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "payment_method");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<BillEntity> _result = new ArrayList<BillEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpOpenAt;
            _tmpOpenAt = _cursor.getLong(_cursorIndexOfOpenAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTax;
            _tmpTax = _cursor.getInt(_cursorIndexOfTax);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final int _tmpTotalTax10;
            _tmpTotalTax10 = _cursor.getInt(_cursorIndexOfTotalTax10);
            final int _tmpTotalTax8;
            _tmpTotalTax8 = _cursor.getInt(_cursorIndexOfTotalTax8);
            final int _tmpPaidAmount;
            _tmpPaidAmount = _cursor.getInt(_cursorIndexOfPaidAmount);
            final String _tmpPaymentMethod;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPaymentMethod = null;
            } else {
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new BillEntity(_tmpId,_tmpTableId,_tmpStatus,_tmpOpenAt,_tmpClosedAt,_tmpPeopleCount,_tmpSubtotal,_tmpTax,_tmpTotalAmount,_tmpTotalTax10,_tmpTotalTax8,_tmpPaidAmount,_tmpPaymentMethod,_tmpNote);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getKPIsRaw(final long from, final long to,
      final Continuation<? super KPIsRaw> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COALESCE(SUM(total_amount), 0) as total,\n"
            + "            COALESCE(AVG(total_amount), 0) as avg_check,\n"
            + "            COALESCE(SUM(people_count), 0) as total_customers,\n"
            + "            COUNT(*) as total_bills\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<KPIsRaw>() {
      @Override
      @NonNull
      public KPIsRaw call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotal = 0;
          final int _cursorIndexOfAvgCheck = 1;
          final int _cursorIndexOfTotalCustomers = 2;
          final int _cursorIndexOfTotalBills = 3;
          final KPIsRaw _result;
          if (_cursor.moveToFirst()) {
            final long _tmpTotal;
            _tmpTotal = _cursor.getLong(_cursorIndexOfTotal);
            final int _tmpAvg_check;
            _tmpAvg_check = _cursor.getInt(_cursorIndexOfAvgCheck);
            final int _tmpTotal_customers;
            _tmpTotal_customers = _cursor.getInt(_cursorIndexOfTotalCustomers);
            final int _tmpTotal_bills;
            _tmpTotal_bills = _cursor.getInt(_cursorIndexOfTotalBills);
            _result = new KPIsRaw(_tmpTotal,_tmpAvg_check,_tmpTotal_customers,_tmpTotal_bills);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getHourlySalesRaw(final long from, final long to,
      final Continuation<? super List<HourlySalesRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            CAST(strftime('%H', closed_at / 1000, 'unixepoch', 'localtime') AS INTEGER) as hour,\n"
            + "            COUNT(*) as order_count,\n"
            + "            SUM(total_amount) as total_sales\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "        GROUP BY hour\n"
            + "        ORDER BY hour\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HourlySalesRaw>>() {
      @Override
      @NonNull
      public List<HourlySalesRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHour = 0;
          final int _cursorIndexOfOrderCount = 1;
          final int _cursorIndexOfTotalSales = 2;
          final List<HourlySalesRaw> _result = new ArrayList<HourlySalesRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HourlySalesRaw _item;
            final int _tmpHour;
            _tmpHour = _cursor.getInt(_cursorIndexOfHour);
            final int _tmpOrder_count;
            _tmpOrder_count = _cursor.getInt(_cursorIndexOfOrderCount);
            final int _tmpTotal_sales;
            _tmpTotal_sales = _cursor.getInt(_cursorIndexOfTotalSales);
            _item = new HourlySalesRaw(_tmpHour,_tmpOrder_count,_tmpTotal_sales);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDailySalesRaw(final long from, final long to,
      final Continuation<? super List<DailySalesRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            CAST(strftime('%w', closed_at / 1000, 'unixepoch', 'localtime') AS INTEGER) as day_of_week,\n"
            + "            COUNT(*) as order_count,\n"
            + "            SUM(total_amount) as total_sales\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "        GROUP BY day_of_week\n"
            + "        ORDER BY day_of_week\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DailySalesRaw>>() {
      @Override
      @NonNull
      public List<DailySalesRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDayOfWeek = 0;
          final int _cursorIndexOfOrderCount = 1;
          final int _cursorIndexOfTotalSales = 2;
          final List<DailySalesRaw> _result = new ArrayList<DailySalesRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailySalesRaw _item;
            final int _tmpDay_of_week;
            _tmpDay_of_week = _cursor.getInt(_cursorIndexOfDayOfWeek);
            final int _tmpOrder_count;
            _tmpOrder_count = _cursor.getInt(_cursorIndexOfOrderCount);
            final int _tmpTotal_sales;
            _tmpTotal_sales = _cursor.getInt(_cursorIndexOfTotalSales);
            _item = new DailySalesRaw(_tmpDay_of_week,_tmpOrder_count,_tmpTotal_sales);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getSalesByDateRaw(final long from, final long to,
      final Continuation<? super List<DateSalesRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            strftime('%Y-%m-%d', closed_at / 1000, 'unixepoch', 'localtime') as date,\n"
            + "            COUNT(*) as order_count,\n"
            + "            SUM(total_amount) as total_sales\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "        GROUP BY date\n"
            + "        ORDER BY date\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<DateSalesRaw>>() {
      @Override
      @NonNull
      public List<DateSalesRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = 0;
          final int _cursorIndexOfOrderCount = 1;
          final int _cursorIndexOfTotalSales = 2;
          final List<DateSalesRaw> _result = new ArrayList<DateSalesRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DateSalesRaw _item;
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpOrder_count;
            _tmpOrder_count = _cursor.getInt(_cursorIndexOfOrderCount);
            final int _tmpTotal_sales;
            _tmpTotal_sales = _cursor.getInt(_cursorIndexOfTotalSales);
            _item = new DateSalesRaw(_tmpDate,_tmpOrder_count,_tmpTotal_sales);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMonthlySalesRaw(final long from, final long to,
      final Continuation<? super List<MonthlySalesRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            strftime('%Y-%m', closed_at / 1000, 'unixepoch', 'localtime') as year_month,\n"
            + "            COUNT(*) as order_count,\n"
            + "            SUM(total_amount) as total_sales\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "        GROUP BY year_month\n"
            + "        ORDER BY year_month\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MonthlySalesRaw>>() {
      @Override
      @NonNull
      public List<MonthlySalesRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfYearMonth = 0;
          final int _cursorIndexOfOrderCount = 1;
          final int _cursorIndexOfTotalSales = 2;
          final List<MonthlySalesRaw> _result = new ArrayList<MonthlySalesRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MonthlySalesRaw _item;
            final String _tmpYear_month;
            _tmpYear_month = _cursor.getString(_cursorIndexOfYearMonth);
            final int _tmpOrder_count;
            _tmpOrder_count = _cursor.getInt(_cursorIndexOfOrderCount);
            final int _tmpTotal_sales;
            _tmpTotal_sales = _cursor.getInt(_cursorIndexOfTotalSales);
            _item = new MonthlySalesRaw(_tmpYear_month,_tmpOrder_count,_tmpTotal_sales);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTableUsesForDay(final long startOfDay, final long endOfDay,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(DISTINCT table_id) \n"
            + "        FROM bills \n"
            + "        WHERE status = 'PAID' \n"
            + "          AND closed_at >= ? \n"
            + "          AND closed_at < ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPaidBillCount(final long from, final long to,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) \n"
            + "        FROM bills \n"
            + "        WHERE status = 'PAID' \n"
            + "          AND closed_at BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
