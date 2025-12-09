package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.BillItemEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class BillItemDao_Impl implements BillItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BillItemEntity> __insertionAdapterOfBillItemEntity;

  private final EntityDeletionOrUpdateAdapter<BillItemEntity> __updateAdapterOfBillItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByBillId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateIsPaid;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BillItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBillItemEntity = new EntityInsertionAdapter<BillItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bill_items` (`id`,`bill_id`,`order_id`,`order_item_id`,`menu_item_id`,`name`,`unit_price`,`quantity`,`line_total`,`tax_rate`,`tax_amount`,`is_paid`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBillId());
        statement.bindLong(3, entity.getOrderId());
        statement.bindLong(4, entity.getOrderItemId());
        statement.bindLong(5, entity.getMenuItemId());
        statement.bindString(6, entity.getName());
        statement.bindLong(7, entity.getUnitPrice());
        statement.bindLong(8, entity.getQuantity());
        statement.bindLong(9, entity.getLineTotal());
        statement.bindDouble(10, entity.getTaxRate());
        statement.bindLong(11, entity.getTaxAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(12, _tmp);
      }
    };
    this.__updateAdapterOfBillItemEntity = new EntityDeletionOrUpdateAdapter<BillItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `bill_items` SET `id` = ?,`bill_id` = ?,`order_id` = ?,`order_item_id` = ?,`menu_item_id` = ?,`name` = ?,`unit_price` = ?,`quantity` = ?,`line_total` = ?,`tax_rate` = ?,`tax_amount` = ?,`is_paid` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBillId());
        statement.bindLong(3, entity.getOrderId());
        statement.bindLong(4, entity.getOrderItemId());
        statement.bindLong(5, entity.getMenuItemId());
        statement.bindString(6, entity.getName());
        statement.bindLong(7, entity.getUnitPrice());
        statement.bindLong(8, entity.getQuantity());
        statement.bindLong(9, entity.getLineTotal());
        statement.bindDouble(10, entity.getTaxRate());
        statement.bindLong(11, entity.getTaxAmount());
        final int _tmp = entity.isPaid() ? 1 : 0;
        statement.bindLong(12, _tmp);
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteByBillId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bill_items WHERE bill_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateIsPaid = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE bill_items SET is_paid = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bill_items";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final BillItemEntity billItem,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBillItemEntity.insertAndReturnId(billItem);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final BillItemEntity billItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBillItemEntity.handle(billItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByBillId(final long billId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByBillId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, billId);
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
          __preparedStmtOfDeleteByBillId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateIsPaid(final long billItemId, final boolean isPaid,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateIsPaid.acquire();
        int _argIndex = 1;
        final int _tmp = isPaid ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, billItemId);
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
          __preparedStmtOfUpdateIsPaid.release(_stmt);
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
  public Object findByBillId(final long billId,
      final Continuation<? super List<BillItemEntity>> $completion) {
    final String _sql = "SELECT * FROM bill_items WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillItemEntity>>() {
      @Override
      @NonNull
      public List<BillItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_id");
          final int _cursorIndexOfOrderItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_item_id");
          final int _cursorIndexOfMenuItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "menu_item_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unit_price");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfLineTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "line_total");
          final int _cursorIndexOfTaxRate = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_rate");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_paid");
          final List<BillItemEntity> _result = new ArrayList<BillItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final long _tmpOrderId;
            _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
            final long _tmpOrderItemId;
            _tmpOrderItemId = _cursor.getLong(_cursorIndexOfOrderItemId);
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final int _tmpLineTotal;
            _tmpLineTotal = _cursor.getInt(_cursorIndexOfLineTotal);
            final float _tmpTaxRate;
            _tmpTaxRate = _cursor.getFloat(_cursorIndexOfTaxRate);
            final int _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getInt(_cursorIndexOfTaxAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            _item = new BillItemEntity(_tmpId,_tmpBillId,_tmpOrderId,_tmpOrderItemId,_tmpMenuItemId,_tmpName,_tmpUnitPrice,_tmpQuantity,_tmpLineTotal,_tmpTaxRate,_tmpTaxAmount,_tmpIsPaid);
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
  public Object findByIds(final List<Long> ids,
      final Continuation<? super List<BillItemEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM bill_items WHERE id IN (");
    final int _inputSize = ids.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (long _item : ids) {
      _statement.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillItemEntity>>() {
      @Override
      @NonNull
      public List<BillItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_id");
          final int _cursorIndexOfOrderItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "order_item_id");
          final int _cursorIndexOfMenuItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "menu_item_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unit_price");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfLineTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "line_total");
          final int _cursorIndexOfTaxRate = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_rate");
          final int _cursorIndexOfTaxAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "tax_amount");
          final int _cursorIndexOfIsPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "is_paid");
          final List<BillItemEntity> _result = new ArrayList<BillItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillItemEntity _item_1;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final long _tmpOrderId;
            _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
            final long _tmpOrderItemId;
            _tmpOrderItemId = _cursor.getLong(_cursorIndexOfOrderItemId);
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final int _tmpLineTotal;
            _tmpLineTotal = _cursor.getInt(_cursorIndexOfLineTotal);
            final float _tmpTaxRate;
            _tmpTaxRate = _cursor.getFloat(_cursorIndexOfTaxRate);
            final int _tmpTaxAmount;
            _tmpTaxAmount = _cursor.getInt(_cursorIndexOfTaxAmount);
            final boolean _tmpIsPaid;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsPaid);
            _tmpIsPaid = _tmp != 0;
            _item_1 = new BillItemEntity(_tmpId,_tmpBillId,_tmpOrderId,_tmpOrderItemId,_tmpMenuItemId,_tmpName,_tmpUnitPrice,_tmpQuantity,_tmpLineTotal,_tmpTaxRate,_tmpTaxAmount,_tmpIsPaid);
            _result.add(_item_1);
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
  public Object getMenuItemSalesRaw(final long from, final long to,
      final Continuation<? super List<MenuItemSalesRaw>> $completion) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            bi.menu_item_id as menuItemId,\n"
            + "            bi.name,\n"
            + "            SUM(bi.quantity) as quantitySold,\n"
            + "            SUM(bi.line_total) as totalRevenue\n"
            + "        FROM bill_items bi\n"
            + "        JOIN bills b ON bi.bill_id = b.id\n"
            + "        WHERE b.status = 'PAID' AND b.closed_at BETWEEN ? AND ?\n"
            + "        GROUP BY bi.menu_item_id, bi.name\n"
            + "        ORDER BY totalRevenue DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, from);
    _argIndex = 2;
    _statement.bindLong(_argIndex, to);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MenuItemSalesRaw>>() {
      @Override
      @NonNull
      public List<MenuItemSalesRaw> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMenuItemId = 0;
          final int _cursorIndexOfName = 1;
          final int _cursorIndexOfQuantitySold = 2;
          final int _cursorIndexOfTotalRevenue = 3;
          final List<MenuItemSalesRaw> _result = new ArrayList<MenuItemSalesRaw>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MenuItemSalesRaw _item;
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpQuantitySold;
            _tmpQuantitySold = _cursor.getInt(_cursorIndexOfQuantitySold);
            final int _tmpTotalRevenue;
            _tmpTotalRevenue = _cursor.getInt(_cursorIndexOfTotalRevenue);
            _item = new MenuItemSalesRaw(_tmpMenuItemId,_tmpName,_tmpQuantitySold,_tmpTotalRevenue);
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
