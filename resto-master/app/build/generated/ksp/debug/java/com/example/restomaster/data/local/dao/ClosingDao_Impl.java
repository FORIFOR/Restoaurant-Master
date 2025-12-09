package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ClosingDao_Impl implements ClosingDao {
  private final RoomDatabase __db;

  public ClosingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
  }

  @Override
  public Object getSalesTotal(final long from, final long to,
      final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(total_amount), 0)\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
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
  public Object getPaymentsByMethod(final long from, final long to,
      final Continuation<? super List<PaymentMethodSummary>> $completion) {
    final String _sql = "\n"
            + "        SELECT provider, SUM(amount) AS total\n"
            + "        FROM payments\n"
            + "        WHERE status = 'SUCCEEDED' AND confirmed_at BETWEEN ? AND ?\n"
            + "        GROUP BY provider\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentMethodSummary>>() {
      @Override
      @NonNull
      public List<PaymentMethodSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProvider = 0;
          final int _cursorIndexOfTotal = 1;
          final List<PaymentMethodSummary> _result = new ArrayList<PaymentMethodSummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentMethodSummary _item;
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final long _tmpTotal;
            _tmpTotal = _cursor.getLong(_cursorIndexOfTotal);
            _item = new PaymentMethodSummary(_tmpProvider,_tmpTotal);
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
  public Object getPaymentsTotal(final long from, final long to,
      final Continuation<? super Long> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0)\n"
            + "        FROM payments\n"
            + "        WHERE status = 'SUCCEEDED' AND confirmed_at BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final long _tmp;
            _tmp = _cursor.getLong(0);
            _result = _tmp;
          } else {
            _result = 0L;
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
  public Object getUnpaidBills(final long from, final long to,
      final Continuation<? super List<UnpaidBillSummary>> $completion) {
    final String _sql = "\n"
            + "        SELECT b.id, b.table_id, b.total_amount, b.closed_at, b.payment_method\n"
            + "        FROM bills b\n"
            + "        WHERE b.status = 'PAID' \n"
            + "          AND b.closed_at BETWEEN ? AND ?\n"
            + "          AND b.id NOT IN (\n"
            + "            SELECT DISTINCT bill_id FROM payments WHERE status = 'SUCCEEDED'\n"
            + "          )\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<UnpaidBillSummary>>() {
      @Override
      @NonNull
      public List<UnpaidBillSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfTableId = 1;
          final int _cursorIndexOfTotalAmount = 2;
          final int _cursorIndexOfClosedAt = 3;
          final int _cursorIndexOfPaymentMethod = 4;
          final List<UnpaidBillSummary> _result = new ArrayList<UnpaidBillSummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final UnpaidBillSummary _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpTable_id;
            _tmpTable_id = _cursor.getLong(_cursorIndexOfTableId);
            final int _tmpTotal_amount;
            _tmpTotal_amount = _cursor.getInt(_cursorIndexOfTotalAmount);
            final Long _tmpClosed_at;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosed_at = null;
            } else {
              _tmpClosed_at = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final String _tmpPayment_method;
            if (_cursor.isNull(_cursorIndexOfPaymentMethod)) {
              _tmpPayment_method = null;
            } else {
              _tmpPayment_method = _cursor.getString(_cursorIndexOfPaymentMethod);
            }
            _item = new UnpaidBillSummary(_tmpId,_tmpTable_id,_tmpTotal_amount,_tmpClosed_at,_tmpPayment_method);
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
  public Object getAbnormalPayments(final long from, final long to,
      final Continuation<? super List<AbnormalPaymentSummary>> $completion) {
    final String _sql = "\n"
            + "        SELECT id, bill_id, provider, amount, status, created_at\n"
            + "        FROM payments\n"
            + "        WHERE created_at BETWEEN ? AND ?\n"
            + "          AND status != 'SUCCEEDED'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AbnormalPaymentSummary>>() {
      @Override
      @NonNull
      public List<AbnormalPaymentSummary> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfBillId = 1;
          final int _cursorIndexOfProvider = 2;
          final int _cursorIndexOfAmount = 3;
          final int _cursorIndexOfStatus = 4;
          final int _cursorIndexOfCreatedAt = 5;
          final List<AbnormalPaymentSummary> _result = new ArrayList<AbnormalPaymentSummary>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AbnormalPaymentSummary _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBill_id;
            _tmpBill_id = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreated_at;
            _tmpCreated_at = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new AbnormalPaymentSummary(_tmpId,_tmpBill_id,_tmpProvider,_tmpAmount,_tmpStatus,_tmpCreated_at);
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
  public Object getPaidBillCount(final long from, final long to,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*)\n"
            + "        FROM bills\n"
            + "        WHERE status = 'PAID' AND closed_at BETWEEN ? AND ?\n"
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
