package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.MasterMenuItemAllergen;
import java.lang.Class;
import java.lang.Exception;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MasterMenuItemAllergenDao_Impl implements MasterMenuItemAllergenDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MasterMenuItemAllergen> __insertionAdapterOfMasterMenuItemAllergen;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllForMenu;

  public MasterMenuItemAllergenDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMasterMenuItemAllergen = new EntityInsertionAdapter<MasterMenuItemAllergen>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `master_menu_item_allergens` (`menuItemId`,`allergenCode`,`source`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MasterMenuItemAllergen entity) {
        statement.bindLong(1, entity.getMenuItemId());
        statement.bindString(2, entity.getAllergenCode());
        statement.bindString(3, entity.getSource());
      }
    };
    this.__preparedStmtOfDeleteAllForMenu = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM master_menu_item_allergens WHERE menuItemId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<MasterMenuItemAllergen> relations,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMasterMenuItemAllergen.insert(relations);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllForMenu(final long menuItemId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllForMenu.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, menuItemId);
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
          __preparedStmtOfDeleteAllForMenu.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllergensForMenu(final long menuItemId,
      final Continuation<? super List<MasterMenuItemAllergen>> $completion) {
    final String _sql = "SELECT * FROM master_menu_item_allergens WHERE menuItemId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, menuItemId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MasterMenuItemAllergen>>() {
      @Override
      @NonNull
      public List<MasterMenuItemAllergen> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMenuItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "menuItemId");
          final int _cursorIndexOfAllergenCode = CursorUtil.getColumnIndexOrThrow(_cursor, "allergenCode");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final List<MasterMenuItemAllergen> _result = new ArrayList<MasterMenuItemAllergen>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MasterMenuItemAllergen _item;
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final String _tmpAllergenCode;
            _tmpAllergenCode = _cursor.getString(_cursorIndexOfAllergenCode);
            final String _tmpSource;
            _tmpSource = _cursor.getString(_cursorIndexOfSource);
            _item = new MasterMenuItemAllergen(_tmpMenuItemId,_tmpAllergenCode,_tmpSource);
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
