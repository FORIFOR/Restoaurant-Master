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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.StoreEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StoreDao_Impl implements StoreDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StoreEntity> __insertionAdapterOfStoreEntity;

  private final EntityDeletionOrUpdateAdapter<StoreEntity> __updateAdapterOfStoreEntity;

  public StoreDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStoreEntity = new EntityInsertionAdapter<StoreEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `stores` (`id`,`name`,`address`,`phone`,`invoice_registration_number`,`postal_code`,`address_line1`,`address_line2`,`tax_included_pricing`,`splash_image_path`,`theme_color`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StoreEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getAddress() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAddress());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getInvoiceRegistrationNumber() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getInvoiceRegistrationNumber());
        }
        if (entity.getPostalCode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPostalCode());
        }
        if (entity.getAddressLine1() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAddressLine1());
        }
        if (entity.getAddressLine2() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAddressLine2());
        }
        final int _tmp = entity.getTaxIncludedPricing() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getSplashImagePath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSplashImagePath());
        }
        if (entity.getThemeColor() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getThemeColor());
        }
      }
    };
    this.__updateAdapterOfStoreEntity = new EntityDeletionOrUpdateAdapter<StoreEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `stores` SET `id` = ?,`name` = ?,`address` = ?,`phone` = ?,`invoice_registration_number` = ?,`postal_code` = ?,`address_line1` = ?,`address_line2` = ?,`tax_included_pricing` = ?,`splash_image_path` = ?,`theme_color` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StoreEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        if (entity.getAddress() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getAddress());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getPhone());
        }
        if (entity.getInvoiceRegistrationNumber() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getInvoiceRegistrationNumber());
        }
        if (entity.getPostalCode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPostalCode());
        }
        if (entity.getAddressLine1() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAddressLine1());
        }
        if (entity.getAddressLine2() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAddressLine2());
        }
        final int _tmp = entity.getTaxIncludedPricing() ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.getSplashImagePath() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSplashImagePath());
        }
        if (entity.getThemeColor() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getThemeColor());
        }
        statement.bindLong(12, entity.getId());
      }
    };
  }

  @Override
  public Object insertStore(final StoreEntity store, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStoreEntity.insert(store);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStore(final StoreEntity store, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStoreEntity.handle(store);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StoreEntity> getStoreFlow() {
    final String _sql = "SELECT * FROM stores LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"stores"}, new Callable<StoreEntity>() {
      @Override
      @Nullable
      public StoreEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoice_registration_number");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postal_code");
          final int _cursorIndexOfAddressLine1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line1");
          final int _cursorIndexOfAddressLine2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line2");
          final int _cursorIndexOfTaxIncludedPricing = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_included_pricing");
          final int _cursorIndexOfSplashImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "splash_image_path");
          final int _cursorIndexOfThemeColor = CursorUtil.getColumnIndexOrThrow(_cursor, "theme_color");
          final StoreEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpInvoiceRegistrationNumber;
            if (_cursor.isNull(_cursorIndexOfInvoiceRegistrationNumber)) {
              _tmpInvoiceRegistrationNumber = null;
            } else {
              _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpAddressLine1;
            if (_cursor.isNull(_cursorIndexOfAddressLine1)) {
              _tmpAddressLine1 = null;
            } else {
              _tmpAddressLine1 = _cursor.getString(_cursorIndexOfAddressLine1);
            }
            final String _tmpAddressLine2;
            if (_cursor.isNull(_cursorIndexOfAddressLine2)) {
              _tmpAddressLine2 = null;
            } else {
              _tmpAddressLine2 = _cursor.getString(_cursorIndexOfAddressLine2);
            }
            final boolean _tmpTaxIncludedPricing;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncludedPricing);
            _tmpTaxIncludedPricing = _tmp != 0;
            final String _tmpSplashImagePath;
            if (_cursor.isNull(_cursorIndexOfSplashImagePath)) {
              _tmpSplashImagePath = null;
            } else {
              _tmpSplashImagePath = _cursor.getString(_cursorIndexOfSplashImagePath);
            }
            final String _tmpThemeColor;
            if (_cursor.isNull(_cursorIndexOfThemeColor)) {
              _tmpThemeColor = null;
            } else {
              _tmpThemeColor = _cursor.getString(_cursorIndexOfThemeColor);
            }
            _result = new StoreEntity(_tmpId,_tmpName,_tmpAddress,_tmpPhone,_tmpInvoiceRegistrationNumber,_tmpPostalCode,_tmpAddressLine1,_tmpAddressLine2,_tmpTaxIncludedPricing,_tmpSplashImagePath,_tmpThemeColor);
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
  public Object getStore(final Continuation<? super StoreEntity> $completion) {
    final String _sql = "SELECT * FROM stores LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StoreEntity>() {
      @Override
      @Nullable
      public StoreEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoice_registration_number");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postal_code");
          final int _cursorIndexOfAddressLine1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line1");
          final int _cursorIndexOfAddressLine2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line2");
          final int _cursorIndexOfTaxIncludedPricing = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_included_pricing");
          final int _cursorIndexOfSplashImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "splash_image_path");
          final int _cursorIndexOfThemeColor = CursorUtil.getColumnIndexOrThrow(_cursor, "theme_color");
          final StoreEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpInvoiceRegistrationNumber;
            if (_cursor.isNull(_cursorIndexOfInvoiceRegistrationNumber)) {
              _tmpInvoiceRegistrationNumber = null;
            } else {
              _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpAddressLine1;
            if (_cursor.isNull(_cursorIndexOfAddressLine1)) {
              _tmpAddressLine1 = null;
            } else {
              _tmpAddressLine1 = _cursor.getString(_cursorIndexOfAddressLine1);
            }
            final String _tmpAddressLine2;
            if (_cursor.isNull(_cursorIndexOfAddressLine2)) {
              _tmpAddressLine2 = null;
            } else {
              _tmpAddressLine2 = _cursor.getString(_cursorIndexOfAddressLine2);
            }
            final boolean _tmpTaxIncludedPricing;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncludedPricing);
            _tmpTaxIncludedPricing = _tmp != 0;
            final String _tmpSplashImagePath;
            if (_cursor.isNull(_cursorIndexOfSplashImagePath)) {
              _tmpSplashImagePath = null;
            } else {
              _tmpSplashImagePath = _cursor.getString(_cursorIndexOfSplashImagePath);
            }
            final String _tmpThemeColor;
            if (_cursor.isNull(_cursorIndexOfThemeColor)) {
              _tmpThemeColor = null;
            } else {
              _tmpThemeColor = _cursor.getString(_cursorIndexOfThemeColor);
            }
            _result = new StoreEntity(_tmpId,_tmpName,_tmpAddress,_tmpPhone,_tmpInvoiceRegistrationNumber,_tmpPostalCode,_tmpAddressLine1,_tmpAddressLine2,_tmpTaxIncludedPricing,_tmpSplashImagePath,_tmpThemeColor);
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
  public Object getStoreById(final long storeId,
      final Continuation<? super StoreEntity> $completion) {
    final String _sql = "SELECT * FROM stores WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StoreEntity>() {
      @Override
      @Nullable
      public StoreEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfInvoiceRegistrationNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoice_registration_number");
          final int _cursorIndexOfPostalCode = CursorUtil.getColumnIndexOrThrow(_cursor, "postal_code");
          final int _cursorIndexOfAddressLine1 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line1");
          final int _cursorIndexOfAddressLine2 = CursorUtil.getColumnIndexOrThrow(_cursor, "address_line2");
          final int _cursorIndexOfTaxIncludedPricing = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_included_pricing");
          final int _cursorIndexOfSplashImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "splash_image_path");
          final int _cursorIndexOfThemeColor = CursorUtil.getColumnIndexOrThrow(_cursor, "theme_color");
          final StoreEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpInvoiceRegistrationNumber;
            if (_cursor.isNull(_cursorIndexOfInvoiceRegistrationNumber)) {
              _tmpInvoiceRegistrationNumber = null;
            } else {
              _tmpInvoiceRegistrationNumber = _cursor.getString(_cursorIndexOfInvoiceRegistrationNumber);
            }
            final String _tmpPostalCode;
            if (_cursor.isNull(_cursorIndexOfPostalCode)) {
              _tmpPostalCode = null;
            } else {
              _tmpPostalCode = _cursor.getString(_cursorIndexOfPostalCode);
            }
            final String _tmpAddressLine1;
            if (_cursor.isNull(_cursorIndexOfAddressLine1)) {
              _tmpAddressLine1 = null;
            } else {
              _tmpAddressLine1 = _cursor.getString(_cursorIndexOfAddressLine1);
            }
            final String _tmpAddressLine2;
            if (_cursor.isNull(_cursorIndexOfAddressLine2)) {
              _tmpAddressLine2 = null;
            } else {
              _tmpAddressLine2 = _cursor.getString(_cursorIndexOfAddressLine2);
            }
            final boolean _tmpTaxIncludedPricing;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfTaxIncludedPricing);
            _tmpTaxIncludedPricing = _tmp != 0;
            final String _tmpSplashImagePath;
            if (_cursor.isNull(_cursorIndexOfSplashImagePath)) {
              _tmpSplashImagePath = null;
            } else {
              _tmpSplashImagePath = _cursor.getString(_cursorIndexOfSplashImagePath);
            }
            final String _tmpThemeColor;
            if (_cursor.isNull(_cursorIndexOfThemeColor)) {
              _tmpThemeColor = null;
            } else {
              _tmpThemeColor = _cursor.getString(_cursorIndexOfThemeColor);
            }
            _result = new StoreEntity(_tmpId,_tmpName,_tmpAddress,_tmpPhone,_tmpInvoiceRegistrationNumber,_tmpPostalCode,_tmpAddressLine1,_tmpAddressLine2,_tmpTaxIncludedPricing,_tmpSplashImagePath,_tmpThemeColor);
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
