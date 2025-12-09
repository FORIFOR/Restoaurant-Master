package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.MasterAllergenEntity;
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
public final class MasterAllergenDao_Impl implements MasterAllergenDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MasterAllergenEntity> __insertionAdapterOfMasterAllergenEntity;

  public MasterAllergenDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMasterAllergenEntity = new EntityInsertionAdapter<MasterAllergenEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `master_allergens` (`code`,`labelJa`,`sortOrder`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MasterAllergenEntity entity) {
        statement.bindString(1, entity.getCode());
        statement.bindString(2, entity.getLabelJa());
        statement.bindLong(3, entity.getSortOrder());
      }
    };
  }

  @Override
  public Object insertAll(final List<MasterAllergenEntity> allergens,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMasterAllergenEntity.insert(allergens);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllAllergens(
      final Continuation<? super List<MasterAllergenEntity>> $completion) {
    final String _sql = "SELECT * FROM master_allergens ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MasterAllergenEntity>>() {
      @Override
      @NonNull
      public List<MasterAllergenEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
          final int _cursorIndexOfLabelJa = CursorUtil.getColumnIndexOrThrow(_cursor, "labelJa");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<MasterAllergenEntity> _result = new ArrayList<MasterAllergenEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MasterAllergenEntity _item;
            final String _tmpCode;
            _tmpCode = _cursor.getString(_cursorIndexOfCode);
            final String _tmpLabelJa;
            _tmpLabelJa = _cursor.getString(_cursorIndexOfLabelJa);
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new MasterAllergenEntity(_tmpCode,_tmpLabelJa,_tmpSortOrder);
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
