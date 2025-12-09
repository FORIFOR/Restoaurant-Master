package com.example.restomaster.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.restomaster.data.local.entity.CallRequestEntity;
import com.example.restomaster.data.local.entity.MenuItemEntity;
import com.example.restomaster.data.local.entity.OrderEntity;
import com.example.restomaster.data.local.entity.OrderItemEntity;
import com.example.restomaster.data.local.entity.TableEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OrderDao_Impl implements OrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OrderEntity> __insertionAdapterOfOrderEntity;

  private final EntityInsertionAdapter<OrderItemEntity> __insertionAdapterOfOrderItemEntity;

  private final EntityInsertionAdapter<CallRequestEntity> __insertionAdapterOfCallRequestEntity;

  private final EntityDeletionOrUpdateAdapter<OrderEntity> __updateAdapterOfOrderEntity;

  private final EntityDeletionOrUpdateAdapter<CallRequestEntity> __updateAdapterOfCallRequestEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderItemStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateOrderItemsStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public OrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrderEntity = new EntityInsertionAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `orders` (`id`,`storeId`,`tableId`,`status`,`peopleCount`,`createdAt`,`closed_at`,`bill_id`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getStatus());
        if (entity.getPeopleCount() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getPeopleCount());
        }
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getClosedAt());
        }
        if (entity.getBillId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getBillId());
        }
      }
    };
    this.__insertionAdapterOfOrderItemEntity = new EntityInsertionAdapter<OrderItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `order_items` (`id`,`orderId`,`menuItemId`,`quantity`,`unitPrice`,`status`,`memo`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderItemEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getOrderId());
        statement.bindLong(3, entity.getMenuItemId());
        statement.bindLong(4, entity.getQuantity());
        statement.bindLong(5, entity.getUnitPrice());
        statement.bindString(6, entity.getStatus());
        if (entity.getMemo() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMemo());
        }
      }
    };
    this.__insertionAdapterOfCallRequestEntity = new EntityInsertionAdapter<CallRequestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `call_requests` (`id`,`storeId`,`tableId`,`type`,`status`,`createdAt`,`handledAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallRequestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getHandledAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHandledAt());
        }
      }
    };
    this.__updateAdapterOfOrderEntity = new EntityDeletionOrUpdateAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `orders` SET `id` = ?,`storeId` = ?,`tableId` = ?,`status` = ?,`peopleCount` = ?,`createdAt` = ?,`closed_at` = ?,`bill_id` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getStatus());
        if (entity.getPeopleCount() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getPeopleCount());
        }
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getClosedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getClosedAt());
        }
        if (entity.getBillId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getBillId());
        }
        statement.bindLong(9, entity.getId());
      }
    };
    this.__updateAdapterOfCallRequestEntity = new EntityDeletionOrUpdateAdapter<CallRequestEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `call_requests` SET `id` = ?,`storeId` = ?,`tableId` = ?,`type` = ?,`status` = ?,`createdAt` = ?,`handledAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CallRequestEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getStoreId());
        statement.bindLong(3, entity.getTableId());
        statement.bindString(4, entity.getType());
        statement.bindString(5, entity.getStatus());
        statement.bindLong(6, entity.getCreatedAt());
        if (entity.getHandledAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getHandledAt());
        }
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateOrderStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE orders SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOrderItemStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE order_items SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateOrderItemsStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE order_items SET status = ? WHERE orderId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM orders";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrder(final OrderEntity order, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfOrderEntity.insertAndReturnId(order);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertOrderItem(final OrderItemEntity orderItem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderItemEntity.insert(orderItem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCallRequest(final CallRequestEntity request,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCallRequestEntity.insert(request);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrder(final OrderEntity order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfOrderEntity.handle(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCallRequest(final CallRequestEntity request,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCallRequestEntity.handle(request);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderStatus(final long orderId, final String newStatus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, newStatus);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, orderId);
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
          __preparedStmtOfUpdateOrderStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderItemStatus(final long orderItemId, final String newStatus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderItemStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, newStatus);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, orderItemId);
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
          __preparedStmtOfUpdateOrderItemStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateOrderItemsStatus(final long orderId, final String newStatus,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateOrderItemsStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, newStatus);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, orderId);
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
          __preparedStmtOfUpdateOrderItemsStatus.release(_stmt);
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
  public Flow<List<OrderEntity>> getActiveOrders(final long storeId) {
    final String _sql = "SELECT * FROM orders WHERE storeId = ? AND status != 'closed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Long _tmpBillId;
            if (_cursor.isNull(_cursorIndexOfBillId)) {
              _tmpBillId = null;
            } else {
              _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            }
            _item = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
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
  public Flow<List<OrderItemEntity>> getOrderItems(final long orderId) {
    final String _sql = "SELECT * FROM order_items WHERE orderId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"order_items"}, new Callable<List<OrderItemEntity>>() {
      @Override
      @NonNull
      public List<OrderItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfMenuItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "menuItemId");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<OrderItemEntity> _result = new ArrayList<OrderItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOrderId;
            _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final int _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new OrderItemEntity(_tmpId,_tmpOrderId,_tmpMenuItemId,_tmpQuantity,_tmpUnitPrice,_tmpStatus,_tmpMemo);
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
  public Flow<List<OrderWithItems>> getActiveOrdersWithItems(final long storeId) {
    final String _sql = "SELECT * FROM orders WHERE storeId = ? AND status != 'closed' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"menu_items", "order_items",
        "tables", "orders"}, new Callable<List<OrderWithItems>>() {
      @Override
      @NonNull
      public List<OrderWithItems> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
            final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
            final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
            final LongSparseArray<ArrayList<OrderItemWithMenu>> _collectionItems = new LongSparseArray<ArrayList<OrderItemWithMenu>>();
            final LongSparseArray<TableEntity> _collectionTable = new LongSparseArray<TableEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey)) {
                _collectionItems.put(_tmpKey, new ArrayList<OrderItemWithMenu>());
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfTableId);
              _collectionTable.put(_tmpKey_1, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiporderItemsAscomExampleRestomasterDataLocalDaoOrderItemWithMenu(_collectionItems);
            __fetchRelationshiptablesAscomExampleRestomasterDataLocalEntityTableEntity(_collectionTable);
            final List<OrderWithItems> _result = new ArrayList<OrderWithItems>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final OrderWithItems _item;
              final OrderEntity _tmpOrder;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStoreId;
              _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
              final long _tmpTableId;
              _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final Integer _tmpPeopleCount;
              if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
                _tmpPeopleCount = null;
              } else {
                _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final Long _tmpClosedAt;
              if (_cursor.isNull(_cursorIndexOfClosedAt)) {
                _tmpClosedAt = null;
              } else {
                _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
              }
              final Long _tmpBillId;
              if (_cursor.isNull(_cursorIndexOfBillId)) {
                _tmpBillId = null;
              } else {
                _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
              }
              _tmpOrder = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
              final ArrayList<OrderItemWithMenu> _tmpItemsCollection;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_2);
              final TableEntity _tmpTable;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfTableId);
              _tmpTable = _collectionTable.get(_tmpKey_3);
              _item = new OrderWithItems(_tmpOrder,_tmpItemsCollection,_tmpTable);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getOrderById(final long orderId,
      final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Long _tmpBillId;
            if (_cursor.isNull(_cursorIndexOfBillId)) {
              _tmpBillId = null;
            } else {
              _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            }
            _result = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
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
  public Object getOrderItemsForOrder(final long orderId,
      final Continuation<? super List<OrderItemEntity>> $completion) {
    final String _sql = "SELECT * FROM order_items WHERE orderId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, orderId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderItemEntity>>() {
      @Override
      @NonNull
      public List<OrderItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfOrderId = CursorUtil.getColumnIndexOrThrow(_cursor, "orderId");
          final int _cursorIndexOfMenuItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "menuItemId");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMemo = CursorUtil.getColumnIndexOrThrow(_cursor, "memo");
          final List<OrderItemEntity> _result = new ArrayList<OrderItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderItemEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpOrderId;
            _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
            final long _tmpMenuItemId;
            _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final int _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpMemo;
            if (_cursor.isNull(_cursorIndexOfMemo)) {
              _tmpMemo = null;
            } else {
              _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
            }
            _item = new OrderItemEntity(_tmpId,_tmpOrderId,_tmpMenuItemId,_tmpQuantity,_tmpUnitPrice,_tmpStatus,_tmpMemo);
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
  public Object getActiveOrdersForTable(final long tableId,
      final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders WHERE tableId = ? AND status != 'closed'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tableId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Long _tmpBillId;
            if (_cursor.isNull(_cursorIndexOfBillId)) {
              _tmpBillId = null;
            } else {
              _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            }
            _item = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
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
  public Flow<List<OrderEntity>> getOrdersForTable(final long tableId) {
    final String _sql = "SELECT * FROM orders WHERE tableId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, tableId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Long _tmpBillId;
            if (_cursor.isNull(_cursorIndexOfBillId)) {
              _tmpBillId = null;
            } else {
              _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            }
            _item = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
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
  public Object getOrdersByBillId(final long billId,
      final Continuation<? super List<OrderEntity>> $completion) {
    final String _sql = "SELECT * FROM orders WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final Integer _tmpPeopleCount;
            if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
              _tmpPeopleCount = null;
            } else {
              _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpClosedAt;
            if (_cursor.isNull(_cursorIndexOfClosedAt)) {
              _tmpClosedAt = null;
            } else {
              _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
            }
            final Long _tmpBillId;
            if (_cursor.isNull(_cursorIndexOfBillId)) {
              _tmpBillId = null;
            } else {
              _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            }
            _item = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
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
  public Flow<List<OrderWithItems>> getClosedOrders(final long storeId) {
    final String _sql = "SELECT * FROM orders WHERE storeId = ? AND status = 'closed' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"menu_items", "order_items",
        "tables", "orders"}, new Callable<List<OrderWithItems>>() {
      @Override
      @NonNull
      public List<OrderWithItems> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
            final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfPeopleCount = CursorUtil.getColumnIndexOrThrow(_cursor, "peopleCount");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfClosedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "closed_at");
            final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
            final LongSparseArray<ArrayList<OrderItemWithMenu>> _collectionItems = new LongSparseArray<ArrayList<OrderItemWithMenu>>();
            final LongSparseArray<TableEntity> _collectionTable = new LongSparseArray<TableEntity>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionItems.containsKey(_tmpKey)) {
                _collectionItems.put(_tmpKey, new ArrayList<OrderItemWithMenu>());
              }
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfTableId);
              _collectionTable.put(_tmpKey_1, null);
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiporderItemsAscomExampleRestomasterDataLocalDaoOrderItemWithMenu(_collectionItems);
            __fetchRelationshiptablesAscomExampleRestomasterDataLocalEntityTableEntity(_collectionTable);
            final List<OrderWithItems> _result = new ArrayList<OrderWithItems>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final OrderWithItems _item;
              final OrderEntity _tmpOrder;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final long _tmpStoreId;
              _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
              final long _tmpTableId;
              _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final Integer _tmpPeopleCount;
              if (_cursor.isNull(_cursorIndexOfPeopleCount)) {
                _tmpPeopleCount = null;
              } else {
                _tmpPeopleCount = _cursor.getInt(_cursorIndexOfPeopleCount);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              final Long _tmpClosedAt;
              if (_cursor.isNull(_cursorIndexOfClosedAt)) {
                _tmpClosedAt = null;
              } else {
                _tmpClosedAt = _cursor.getLong(_cursorIndexOfClosedAt);
              }
              final Long _tmpBillId;
              if (_cursor.isNull(_cursorIndexOfBillId)) {
                _tmpBillId = null;
              } else {
                _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
              }
              _tmpOrder = new OrderEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpStatus,_tmpPeopleCount,_tmpCreatedAt,_tmpClosedAt,_tmpBillId);
              final ArrayList<OrderItemWithMenu> _tmpItemsCollection;
              final long _tmpKey_2;
              _tmpKey_2 = _cursor.getLong(_cursorIndexOfId);
              _tmpItemsCollection = _collectionItems.get(_tmpKey_2);
              final TableEntity _tmpTable;
              final long _tmpKey_3;
              _tmpKey_3 = _cursor.getLong(_cursorIndexOfTableId);
              _tmpTable = _collectionTable.get(_tmpKey_3);
              _item = new OrderWithItems(_tmpOrder,_tmpItemsCollection,_tmpTable);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Long> getTotalSales(final long storeId) {
    final String _sql = "\n"
            + "        SELECT SUM(oi.unitPrice * oi.quantity) \n"
            + "        FROM orders o\n"
            + "        INNER JOIN order_items oi ON o.id = oi.orderId\n"
            + "        WHERE o.storeId = ? AND o.status = 'closed'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders",
        "order_items"}, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            final Long _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(0);
            }
            _result = _tmp;
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
  public Flow<List<CallRequestEntity>> getActiveCallRequests(final long storeId) {
    final String _sql = "SELECT * FROM call_requests WHERE storeId = ? AND status = 'new'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, storeId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"call_requests"}, new Callable<List<CallRequestEntity>>() {
      @Override
      @NonNull
      public List<CallRequestEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHandledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "handledAt");
          final List<CallRequestEntity> _result = new ArrayList<CallRequestEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CallRequestEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpHandledAt;
            if (_cursor.isNull(_cursorIndexOfHandledAt)) {
              _tmpHandledAt = null;
            } else {
              _tmpHandledAt = _cursor.getLong(_cursorIndexOfHandledAt);
            }
            _item = new CallRequestEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpHandledAt);
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
  public Object getCallRequestById(final long requestId,
      final Continuation<? super CallRequestEntity> $completion) {
    final String _sql = "SELECT * FROM call_requests WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, requestId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CallRequestEntity>() {
      @Override
      @Nullable
      public CallRequestEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "storeId");
          final int _cursorIndexOfTableId = CursorUtil.getColumnIndexOrThrow(_cursor, "tableId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfHandledAt = CursorUtil.getColumnIndexOrThrow(_cursor, "handledAt");
          final CallRequestEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpStoreId;
            _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
            final long _tmpTableId;
            _tmpTableId = _cursor.getLong(_cursorIndexOfTableId);
            final String _tmpType;
            _tmpType = _cursor.getString(_cursorIndexOfType);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpHandledAt;
            if (_cursor.isNull(_cursorIndexOfHandledAt)) {
              _tmpHandledAt = null;
            } else {
              _tmpHandledAt = _cursor.getLong(_cursorIndexOfHandledAt);
            }
            _result = new CallRequestEntity(_tmpId,_tmpStoreId,_tmpTableId,_tmpType,_tmpStatus,_tmpCreatedAt,_tmpHandledAt);
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

  private void __fetchRelationshipmenuItemsAscomExampleRestomasterDataLocalEntityMenuItemEntity(
      @NonNull final LongSparseArray<MenuItemEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipmenuItemsAscomExampleRestomasterDataLocalEntityMenuItemEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`storeId`,`name`,`description`,`price`,`category`,`image_path`,`isAvailable`,`tax_rate_type`,`is_ai_generated` FROM `menu_items` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfStoreId = 1;
      final int _cursorIndexOfName = 2;
      final int _cursorIndexOfDescription = 3;
      final int _cursorIndexOfPrice = 4;
      final int _cursorIndexOfCategory = 5;
      final int _cursorIndexOfImagePath = 6;
      final int _cursorIndexOfIsAvailable = 7;
      final int _cursorIndexOfTaxRateType = 8;
      final int _cursorIndexOfIsAiGenerated = 9;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final MenuItemEntity _item_1;
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
          _item_1 = new MenuItemEntity(_tmpId,_tmpStoreId,_tmpName,_tmpDescription,_tmpPrice,_tmpCategory,_tmpImagePath,_tmpIsAvailable,_tmpTaxRateType,_tmpIsAiGenerated);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshiporderItemsAscomExampleRestomasterDataLocalDaoOrderItemWithMenu(
      @NonNull final LongSparseArray<ArrayList<OrderItemWithMenu>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiporderItemsAscomExampleRestomasterDataLocalDaoOrderItemWithMenu(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`orderId`,`menuItemId`,`quantity`,`unitPrice`,`status`,`memo` FROM `order_items` WHERE `orderId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, true, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "orderId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfOrderId = 1;
      final int _cursorIndexOfMenuItemId = 2;
      final int _cursorIndexOfQuantity = 3;
      final int _cursorIndexOfUnitPrice = 4;
      final int _cursorIndexOfStatus = 5;
      final int _cursorIndexOfMemo = 6;
      final LongSparseArray<MenuItemEntity> _collectionMenuItem = new LongSparseArray<MenuItemEntity>();
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_cursorIndexOfMenuItemId);
        _collectionMenuItem.put(_tmpKey, null);
      }
      _cursor.moveToPosition(-1);
      __fetchRelationshipmenuItemsAscomExampleRestomasterDataLocalEntityMenuItemEntity(_collectionMenuItem);
      while (_cursor.moveToNext()) {
        final long _tmpKey_1;
        _tmpKey_1 = _cursor.getLong(_itemKeyIndex);
        final ArrayList<OrderItemWithMenu> _tmpRelation = _map.get(_tmpKey_1);
        if (_tmpRelation != null) {
          final OrderItemWithMenu _item_1;
          final OrderItemEntity _tmpItem;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpOrderId;
          _tmpOrderId = _cursor.getLong(_cursorIndexOfOrderId);
          final long _tmpMenuItemId;
          _tmpMenuItemId = _cursor.getLong(_cursorIndexOfMenuItemId);
          final int _tmpQuantity;
          _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
          final int _tmpUnitPrice;
          _tmpUnitPrice = _cursor.getInt(_cursorIndexOfUnitPrice);
          final String _tmpStatus;
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
          final String _tmpMemo;
          if (_cursor.isNull(_cursorIndexOfMemo)) {
            _tmpMemo = null;
          } else {
            _tmpMemo = _cursor.getString(_cursorIndexOfMemo);
          }
          _tmpItem = new OrderItemEntity(_tmpId,_tmpOrderId,_tmpMenuItemId,_tmpQuantity,_tmpUnitPrice,_tmpStatus,_tmpMemo);
          final MenuItemEntity _tmpMenuItem;
          final long _tmpKey_2;
          _tmpKey_2 = _cursor.getLong(_cursorIndexOfMenuItemId);
          _tmpMenuItem = _collectionMenuItem.get(_tmpKey_2);
          _item_1 = new OrderItemWithMenu(_tmpItem,_tmpMenuItem);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshiptablesAscomExampleRestomasterDataLocalEntityTableEntity(
      @NonNull final LongSparseArray<TableEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshiptablesAscomExampleRestomasterDataLocalEntityTableEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`storeId`,`tableNumber`,`token`,`isActive`,`status` FROM `tables` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfStoreId = 1;
      final int _cursorIndexOfTableNumber = 2;
      final int _cursorIndexOfToken = 3;
      final int _cursorIndexOfIsActive = 4;
      final int _cursorIndexOfStatus = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final TableEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final long _tmpStoreId;
          _tmpStoreId = _cursor.getLong(_cursorIndexOfStoreId);
          final int _tmpTableNumber;
          _tmpTableNumber = _cursor.getInt(_cursorIndexOfTableNumber);
          final String _tmpToken;
          _tmpToken = _cursor.getString(_cursorIndexOfToken);
          final boolean _tmpIsActive;
          final int _tmp;
          _tmp = _cursor.getInt(_cursorIndexOfIsActive);
          _tmpIsActive = _tmp != 0;
          final String _tmpStatus;
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
          _item_1 = new TableEntity(_tmpId,_tmpStoreId,_tmpTableNumber,_tmpToken,_tmpIsActive,_tmpStatus);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
