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
import com.example.restomaster.data.local.entity.MenuItemEntity;
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
public final class MenuDao_Impl implements MenuDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MenuItemEntity> __insertionAdapterOfMenuItemEntity;

  private final EntityDeletionOrUpdateAdapter<MenuItemEntity> __deletionAdapterOfMenuItemEntity;

  private final EntityDeletionOrUpdateAdapter<MenuItemEntity> __updateAdapterOfMenuItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMenuItemById;

  public MenuDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMenuItemEntity = new EntityInsertionAdapter<MenuItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `menu_items` (`id`,`storeId`,`name`,`description`,`price`,`category`,`image_path`,`isAvailable`,`tax_rate_type`,`is_ai_generated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MenuItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getPrice());
        statement.bindString(6, entity.getCategory());
        if (entity.getImagePath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImagePath());
        }
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getTaxRateType());
        final int _tmp_1 = entity.isAiGenerated() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__deletionAdapterOfMenuItemEntity = new EntityDeletionOrUpdateAdapter<MenuItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `menu_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MenuItemEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfMenuItemEntity = new EntityDeletionOrUpdateAdapter<MenuItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `menu_items` SET `id` = ?,`storeId` = ?,`name` = ?,`description` = ?,`price` = ?,`category` = ?,`image_path` = ?,`isAvailable` = ?,`tax_rate_type` = ?,`is_ai_generated` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MenuItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindString(3, entity.getName());
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        statement.bindLong(5, entity.getPrice());
        statement.bindString(6, entity.getCategory());
        if (entity.getImagePath() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getImagePath());
        }
        final int _tmp = entity.isAvailable() ? 1 : 0;
        statement.bindLong(8, _tmp);
        statement.bindString(9, entity.getTaxRateType());
        final int _tmp_1 = entity.isAiGenerated() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteMenuItemById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM menu_items WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMenuItem(final MenuItemEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMenuItemEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMenuItem(final MenuItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMenuItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMenuItem(final MenuItemEntity item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMenuItemEntity.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMenuItemById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMenuItemById.acquire();
        int _argIndex = 1;
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
          __preparedStmtOfDeleteMenuItemById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MenuItemEntity>> getMenuItems(final long storeId) {
    final String _sql = "SELECT * FROM menu_items WHERE storeId = ? ORDER BY category, name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"menu_items"}, new Callable<List<MenuItemEntity>>() {
      @Override
      @NonNull
      public List<MenuItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfTaxRateType = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_rate_type");
          final int _cursorIndexOfIsAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ai_generated");
          final List<MenuItemEntity> _result = new ArrayList<MenuItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MenuItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpPrice;
            _tmpPrice = _cursor.getInt(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpTaxRateType;
            _tmpTaxRateType = _cursor.getString(_cursorIndexOfTaxRateType);
            final boolean _tmpIsAiGenerated;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAiGenerated);
            _tmpIsAiGenerated = _tmp_1 != 0;
            _item = new MenuItemEntity(_tmpId,_tmpStoreId,_tmpName,_tmpDescription,_tmpPrice,_tmpCategory,_tmpImagePath,_tmpIsAvailable,_tmpTaxRateType,_tmpIsAiGenerated);
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
  public Object getMenuItem(final long id, final Continuation<? super MenuItemEntity> $completion) {
    final String _sql = "SELECT * FROM menu_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MenuItemEntity>() {
      @Override
      @Nullable
      public MenuItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "image_path");
          final int _cursorIndexOfIsAvailable = CursorUtil.getColumnIndexOrThrow(_cursor, "isAvailable");
          final int _cursorIndexOfTaxRateType = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_rate_type");
          final int _cursorIndexOfIsAiGenerated = CursorUtil.getColumnIndexOrThrow(_cursor, "is_ai_generated");
          final MenuItemEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final int _tmpPrice;
            _tmpPrice = _cursor.getInt(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpImagePath;
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _tmpImagePath = null;
            } else {
              _tmpImagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            final boolean _tmpIsAvailable;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsAvailable);
            _tmpIsAvailable = _tmp != 0;
            final String _tmpTaxRateType;
            _tmpTaxRateType = _cursor.getString(_cursorIndexOfTaxRateType);
            final boolean _tmpIsAiGenerated;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsAiGenerated);
            _tmpIsAiGenerated = _tmp_1 != 0;
            _result = new MenuItemEntity(_tmpId,_tmpStoreId,_tmpName,_tmpDescription,_tmpPrice,_tmpCategory,_tmpImagePath,_tmpIsAvailable,_tmpTaxRateType,_tmpIsAiGenerated);
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
