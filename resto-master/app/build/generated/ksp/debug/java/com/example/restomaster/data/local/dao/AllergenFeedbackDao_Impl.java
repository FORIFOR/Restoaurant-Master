package com.example.restomaster.data.local.dao;

import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.AllergenFeedbackEntity;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AllergenFeedbackDao_Impl implements AllergenFeedbackDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AllergenFeedbackEntity> __insertionAdapterOfAllergenFeedbackEntity;

  public AllergenFeedbackDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAllergenFeedbackEntity = new EntityInsertionAdapter<AllergenFeedbackEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `allergen_feedback` (`id`,`menuItemId`,`allergenCode`,`aiSuggested`,`userAction`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AllergenFeedbackEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getMenuItemId());
        statement.bindString(3, entity.getAllergenCode());
        final int _tmp = entity.getAiSuggested() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindString(5, entity.getUserAction());
        statement.bindLong(6, entity.getTimestamp());
      }
    };
  }

  @Override
  public Object insert(final AllergenFeedbackEntity feedback,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAllergenFeedbackEntity.insert(feedback);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
