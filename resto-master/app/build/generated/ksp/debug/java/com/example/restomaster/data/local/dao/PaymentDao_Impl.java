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
import com.example.restomaster.data.local.entity.PaymentEntity;
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
public final class PaymentDao_Impl implements PaymentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PaymentEntity> __insertionAdapterOfPaymentEntity;

  private final EntityDeletionOrUpdateAdapter<PaymentEntity> __updateAdapterOfPaymentEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public PaymentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPaymentEntity = new EntityInsertionAdapter<PaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `payments` (`id`,`bill_id`,`provider`,`provider_type`,`amount`,`currency`,`status`,`provider_ref_id`,`merchant_payment_id`,`paypay_code_id`,`created_at`,`confirmed_at`,`extra_json`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBillId());
        statement.bindString(3, entity.getProvider());
        statement.bindString(4, entity.getProviderType());
        statement.bindLong(5, entity.getAmount());
        statement.bindString(6, entity.getCurrency());
        statement.bindString(7, entity.getStatus());
        if (entity.getProviderRefId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getProviderRefId());
        }
        if (entity.getMerchantPaymentId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMerchantPaymentId());
        }
        if (entity.getPaypayCodeId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPaypayCodeId());
        }
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getConfirmedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getConfirmedAt());
        }
        if (entity.getExtraJson() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExtraJson());
        }
      }
    };
    this.__updateAdapterOfPaymentEntity = new EntityDeletionOrUpdateAdapter<PaymentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `payments` SET `id` = ?,`bill_id` = ?,`provider` = ?,`provider_type` = ?,`amount` = ?,`currency` = ?,`status` = ?,`provider_ref_id` = ?,`merchant_payment_id` = ?,`paypay_code_id` = ?,`created_at` = ?,`confirmed_at` = ?,`extra_json` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PaymentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBillId());
        statement.bindString(3, entity.getProvider());
        statement.bindString(4, entity.getProviderType());
        statement.bindLong(5, entity.getAmount());
        statement.bindString(6, entity.getCurrency());
        statement.bindString(7, entity.getStatus());
        if (entity.getProviderRefId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getProviderRefId());
        }
        if (entity.getMerchantPaymentId() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMerchantPaymentId());
        }
        if (entity.getPaypayCodeId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPaypayCodeId());
        }
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getConfirmedAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getConfirmedAt());
        }
        if (entity.getExtraJson() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getExtraJson());
        }
        statement.bindLong(14, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM payments";
        return _query;
      }
    };
  }

  @Override
  public Object insertPayment(final PaymentEntity payment,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfPaymentEntity.insertAndReturnId(payment);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePayment(final PaymentEntity payment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPaymentEntity.handle(payment);
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
  public Object getPaymentsForBill(final long billId,
      final Continuation<? super List<PaymentEntity>> $completion) {
    final String _sql = "SELECT * FROM payments WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _item = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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
  public Flow<List<PaymentEntity>> getPaymentsForBillFlow(final long billId) {
    final String _sql = "SELECT * FROM payments WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"payments"}, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _item = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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
  public Object getSuccessfulPaymentsInRange(final long from, final long to,
      final Continuation<? super List<PaymentEntity>> $completion) {
    final String _sql = "SELECT * FROM payments WHERE confirmed_at BETWEEN ? AND ? AND status = 'SUCCEEDED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _item = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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
  public Object getByProviderRefId(final String refId,
      final Continuation<? super PaymentEntity> $completion) {
    final String _sql = "SELECT * FROM payments WHERE provider_ref_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, refId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PaymentEntity>() {
      @Override
      @Nullable
      public PaymentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final PaymentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _result = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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
  public Object getPaymentById(final long paymentId,
      final Continuation<? super PaymentEntity> $completion) {
    final String _sql = "SELECT * FROM payments WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, paymentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PaymentEntity>() {
      @Override
      @Nullable
      public PaymentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final PaymentEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _result = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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
  public Object getAllPayments(final Continuation<? super List<PaymentEntity>> $completion) {
    final String _sql = "SELECT * FROM payments";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<PaymentEntity>>() {
      @Override
      @NonNull
      public List<PaymentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfProviderType = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_type");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfProviderRefId = CursorUtil.getColumnIndexOrThrow(_cursor, "provider_ref_id");
          final int _cursorIndexOfMerchantPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchant_payment_id");
          final int _cursorIndexOfPaypayCodeId = CursorUtil.getColumnIndexOrThrow(_cursor, "paypay_code_id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfConfirmedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "confirmed_at");
          final int _cursorIndexOfExtraJson = CursorUtil.getColumnIndexOrThrow(_cursor, "extra_json");
          final List<PaymentEntity> _result = new ArrayList<PaymentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PaymentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpProviderType;
            _tmpProviderType = _cursor.getString(_cursorIndexOfProviderType);
            final int _tmpAmount;
            _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpProviderRefId;
            if (_cursor.isNull(_cursorIndexOfProviderRefId)) {
              _tmpProviderRefId = null;
            } else {
              _tmpProviderRefId = _cursor.getString(_cursorIndexOfProviderRefId);
            }
            final String _tmpMerchantPaymentId;
            if (_cursor.isNull(_cursorIndexOfMerchantPaymentId)) {
              _tmpMerchantPaymentId = null;
            } else {
              _tmpMerchantPaymentId = _cursor.getString(_cursorIndexOfMerchantPaymentId);
            }
            final String _tmpPaypayCodeId;
            if (_cursor.isNull(_cursorIndexOfPaypayCodeId)) {
              _tmpPaypayCodeId = null;
            } else {
              _tmpPaypayCodeId = _cursor.getString(_cursorIndexOfPaypayCodeId);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpConfirmedAt;
            if (_cursor.isNull(_cursorIndexOfConfirmedAt)) {
              _tmpConfirmedAt = null;
            } else {
              _tmpConfirmedAt = _cursor.getLong(_cursorIndexOfConfirmedAt);
            }
            final String _tmpExtraJson;
            if (_cursor.isNull(_cursorIndexOfExtraJson)) {
              _tmpExtraJson = null;
            } else {
              _tmpExtraJson = _cursor.getString(_cursorIndexOfExtraJson);
            }
            _item = new PaymentEntity(_tmpId,_tmpBillId,_tmpProvider,_tmpProviderType,_tmpAmount,_tmpCurrency,_tmpStatus,_tmpProviderRefId,_tmpMerchantPaymentId,_tmpPaypayCodeId,_tmpCreatedAt,_tmpConfirmedAt,_tmpExtraJson);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
