package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.ReceiptEntity;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReceiptDao_Impl implements ReceiptDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReceiptEntity> __insertionAdapterOfReceiptEntity;

  public ReceiptDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReceiptEntity = new EntityInsertionAdapter<ReceiptEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `receipts` (`id`,`billId`,`receiptNumber`,`issuedAt`,`invoiceRegistrationNumber`,`taxIncluded`,`subtotal`,`taxAmount`,`totalAmount`,`taxBreakdownJson`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReceiptEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBillId());
        statement.bindString(3, entity.getReceiptNumber());
        statement.bindLong(4, entity.getIssuedAt());
        statement.bindString(5, entity.getInvoiceRegistrationNumber());
        final int _tmp = entity.getTaxIncluded() ? 1 : 0;
        statement.bindLong(6, _tmp);
        statement.bindLong(7, entity.getSubtotal());
        statement.bindLong(8, entity.getTaxAmount());
        statement.bindLong(9, entity.getTotalAmount());
        if (entity.getTaxBreakdownJson() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getTaxBreakdownJson());
        }
      }
    };
  }

  @Override
  public Object insertReceipt(final ReceiptEntity receipt,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReceiptEntity.insertAndReturnId(receipt);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReceiptForBill(final long billId,
      final Continuation<? super ReceiptEntity> $completion) {
    final String _sql = "SELECT * FROM receipts WHERE billId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReceiptEntity>() {
      @Override
      @Nullable
      public ReceiptEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "billId");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final int _cursorIndexOfIssuedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedAt");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceRegistrationNumber");
          final int _cursorIndexOfTaxIncluded = CursorUtil.getColumnIndexOrThrow(_cursor, "taxIncluded");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfTaxBreakdownJson = CursorUtil.getColumnIndexOrThrow(_cursor, "taxBreakdownJson");
          final ReceiptEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            final long _tmpIssuedAt;
            _tmpIssuedAt = _cursor.getLong(_cursorIndexOfIssuedAt);
            final String _tmpInvoiceRegistrationNumber;
            _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            final boolean _tmpTaxIncluded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncluded);
            _tmpTaxIncluded = _tmp != 0;
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getInt(_cursorIndexOfTaxAmount);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final String _tmpTaxBreakdownJson;
            if (_cursor.isNull(_cursorIndexOfTaxBreakdownJson)) {
              _tmpTaxBreakdownJson = null;
            } else {
              _tmpTaxBreakdownJson = _cursor.getString(_cursorIndexOfTaxBreakdownJson);
            }
            _result = new ReceiptEntity(_tmpId,_tmpBillId,_tmpReceiptNumber,_tmpIssuedAt,_tmpInvoiceRegistrationNumber,_tmpTaxIncluded,_tmpSubtotal,_tmpTaxAmount,_tmpTotalAmount,_tmpTaxBreakdownJson);
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
  public Object getReceiptById(final long id,
      final Continuation<? super ReceiptEntity> $completion) {
    final String _sql = "SELECT * FROM receipts WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ReceiptEntity>() {
      @Override
      @Nullable
      public ReceiptEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "billId");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final int _cursorIndexOfIssuedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedAt");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceRegistrationNumber");
          final int _cursorIndexOfTaxIncluded = CursorUtil.getColumnIndexOrThrow(_cursor, "taxIncluded");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfTaxBreakdownJson = CursorUtil.getColumnIndexOrThrow(_cursor, "taxBreakdownJson");
          final ReceiptEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            final long _tmpIssuedAt;
            _tmpIssuedAt = _cursor.getLong(_cursorIndexOfIssuedAt);
            final String _tmpInvoiceRegistrationNumber;
            _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            final boolean _tmpTaxIncluded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncluded);
            _tmpTaxIncluded = _tmp != 0;
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getInt(_cursorIndexOfTaxAmount);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final String _tmpTaxBreakdownJson;
            if (_cursor.isNull(_cursorIndexOfTaxBreakdownJson)) {
              _tmpTaxBreakdownJson = null;
            } else {
              _tmpTaxBreakdownJson = _cursor.getString(_cursorIndexOfTaxBreakdownJson);
            }
            _result = new ReceiptEntity(_tmpId,_tmpBillId,_tmpReceiptNumber,_tmpIssuedAt,_tmpInvoiceRegistrationNumber,_tmpTaxIncluded,_tmpSubtotal,_tmpTaxAmount,_tmpTotalAmount,_tmpTaxBreakdownJson);
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
  public Flow<List<ReceiptEntity>> getAllReceipts() {
    final String _sql = "SELECT * FROM receipts ORDER BY issuedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"receipts"}, new Callable<List<ReceiptEntity>>() {
      @Override
      @NonNull
      public List<ReceiptEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "billId");
          final int _cursorIndexOfReceiptNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptNumber");
          final int _cursorIndexOfIssuedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedAt");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceRegistrationNumber");
          final int _cursorIndexOfTaxIncluded = CursorUtil.getColumnIndexOrThrow(_cursor, "taxIncluded");
          final int _cursorIndexOfSubtotal = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "taxAmount");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfTaxBreakdownJson = CursorUtil.getColumnIndexOrThrow(_cursor, "taxBreakdownJson");
          final List<ReceiptEntity> _result = new ArrayList<ReceiptEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReceiptEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpReceiptNumber;
            _tmpReceiptNumber = _cursor.getString(_cursorIndexOfReceiptNumber);
            final long _tmpIssuedAt;
            _tmpIssuedAt = _cursor.getLong(_cursorIndexOfIssuedAt);
            final String _tmpInvoiceRegistrationNumber;
            _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            final boolean _tmpTaxIncluded;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncluded);
            _tmpTaxIncluded = _tmp != 0;
            final int _tmpSubtotal;
            _tmpSubtotal = _cursor.getInt(_cursorIndexOfSubtotal);
            final int _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getInt(_cursorIndexOfTaxAmount);
            final int _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final String _tmpTaxBreakdownJson;
            if (_cursor.isNull(_cursorIndexOfTaxBreakdownJson)) {
              _tmpTaxBreakdownJson = null;
            } else {
              _tmpTaxBreakdownJson = _cursor.getString(_cursorIndexOfTaxBreakdownJson);
            }
            _item = new ReceiptEntity(_tmpId,_tmpBillId,_tmpReceiptNumber,_tmpIssuedAt,_tmpInvoiceRegistrationNumber,_tmpTaxIncluded,_tmpSubtotal,_tmpTaxAmount,_tmpTotalAmount,_tmpTaxBreakdownJson);
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
  public Object getMaxReceiptNumberForDate(final String datePrefix,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(CAST(SUBSTR(receiptNumber, -3) AS INTEGER)) FROM receipts WHERE receiptNumber LIKE ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, datePrefix);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
