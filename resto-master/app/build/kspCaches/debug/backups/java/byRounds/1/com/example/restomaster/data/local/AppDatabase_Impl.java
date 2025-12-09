package com.example.restomaster.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.restomaster.data.local.dao.AllergenFeedbackDao;
import com.example.restomaster.data.local.dao.AllergenFeedbackDao_Impl;
import com.example.restomaster.data.local.dao.BillDao;
import com.example.restomaster.data.local.dao.BillDao_Impl;
import com.example.restomaster.data.local.dao.BillItemDao;
import com.example.restomaster.data.local.dao.BillItemDao_Impl;
import com.example.restomaster.data.local.dao.CallRequestDao;
import com.example.restomaster.data.local.dao.CallRequestDao_Impl;
import com.example.restomaster.data.local.dao.ClosingDao;
import com.example.restomaster.data.local.dao.ClosingDao_Impl;
import com.example.restomaster.data.local.dao.MasterAllergenDao;
import com.example.restomaster.data.local.dao.MasterAllergenDao_Impl;
import com.example.restomaster.data.local.dao.MasterMenuItemAllergenDao;
import com.example.restomaster.data.local.dao.MasterMenuItemAllergenDao_Impl;
import com.example.restomaster.data.local.dao.MenuDao;
import com.example.restomaster.data.local.dao.MenuDao_Impl;
import com.example.restomaster.data.local.dao.MenuTranslationDao;
import com.example.restomaster.data.local.dao.MenuTranslationDao_Impl;
import com.example.restomaster.data.local.dao.OrderDao;
import com.example.restomaster.data.local.dao.OrderDao_Impl;
import com.example.restomaster.data.local.dao.PaymentDao;
import com.example.restomaster.data.local.dao.PaymentDao_Impl;
import com.example.restomaster.data.local.dao.PaymentItemDao;
import com.example.restomaster.data.local.dao.PaymentItemDao_Impl;
import com.example.restomaster.data.local.dao.ReceiptDao;
import com.example.restomaster.data.local.dao.ReceiptDao_Impl;
import com.example.restomaster.data.local.dao.ReservationDao;
import com.example.restomaster.data.local.dao.ReservationDao_Impl;
import com.example.restomaster.data.local.dao.StoreDao;
import com.example.restomaster.data.local.dao.StoreDao_Impl;
import com.example.restomaster.data.local.dao.TableDao;
import com.example.restomaster.data.local.dao.TableDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile StoreDao _storeDao;

  private volatile TableDao _tableDao;

  private volatile MenuDao _menuDao;

  private volatile OrderDao _orderDao;

  private volatile ReservationDao _reservationDao;

  private volatile MasterAllergenDao _masterAllergenDao;

  private volatile MasterMenuItemAllergenDao _masterMenuItemAllergenDao;

  private volatile AllergenFeedbackDao _allergenFeedbackDao;

  private volatile CallRequestDao _callRequestDao;

  private volatile MenuTranslationDao _menuTranslationDao;

  private volatile BillDao _billDao;

  private volatile BillItemDao _billItemDao;

  private volatile PaymentDao _paymentDao;

  private volatile ReceiptDao _receiptDao;

  private volatile PaymentItemDao _paymentItemDao;

  private volatile ClosingDao _closingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(14) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `stores` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `address` TEXT, `phone` TEXT, `invoice_registration_number` TEXT, `postal_code` TEXT, `address_line1` TEXT, `address_line2` TEXT, `tax_included_pricing` INTEGER NOT NULL, `splash_image_path` TEXT, `theme_color` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `tables` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `storeId` INTEGER NOT NULL, `tableNumber` INTEGER NOT NULL, `token` TEXT NOT NULL, `isActive` INTEGER NOT NULL, `status` TEXT NOT NULL)");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tables_storeId_tableNumber` ON `tables` (`storeId`, `tableNumber`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `menu_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `storeId` INTEGER NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `price` INTEGER NOT NULL, `category` TEXT NOT NULL, `image_path` TEXT, `isAvailable` INTEGER NOT NULL, `tax_rate_type` TEXT NOT NULL, `is_ai_generated` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `master_allergens` (`code` TEXT NOT NULL, `labelJa` TEXT NOT NULL, `sortOrder` INTEGER NOT NULL, PRIMARY KEY(`code`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `master_menu_item_allergens` (`menuItemId` INTEGER NOT NULL, `allergenCode` TEXT NOT NULL, `source` TEXT NOT NULL, PRIMARY KEY(`menuItemId`, `allergenCode`), FOREIGN KEY(`menuItemId`) REFERENCES `menu_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`allergenCode`) REFERENCES `master_allergens`(`code`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS `orders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `storeId` INTEGER NOT NULL, `tableId` INTEGER NOT NULL, `status` TEXT NOT NULL, `peopleCount` INTEGER, `createdAt` INTEGER NOT NULL, `closed_at` INTEGER, `bill_id` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `order_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `orderId` INTEGER NOT NULL, `menuItemId` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `unitPrice` INTEGER NOT NULL, `status` TEXT NOT NULL, `memo` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `call_requests` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `storeId` INTEGER NOT NULL, `tableId` INTEGER NOT NULL, `type` TEXT NOT NULL, `status` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `handledAt` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reservations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `storeId` INTEGER NOT NULL, `date` TEXT NOT NULL, `time` TEXT NOT NULL, `name` TEXT NOT NULL, `phone` TEXT, `peopleCount` INTEGER NOT NULL, `assignedTableId` INTEGER, `note` TEXT, `status` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `allergen_feedback` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `menuItemId` INTEGER NOT NULL, `allergenCode` TEXT NOT NULL, `aiSuggested` INTEGER NOT NULL, `userAction` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `menu_translations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `menu_id` INTEGER NOT NULL, `lang` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `status` TEXT NOT NULL, `updated_at` INTEGER NOT NULL, FOREIGN KEY(`menu_id`) REFERENCES `menu_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_menu_translations_menu_id` ON `menu_translations` (`menu_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_menu_translations_lang` ON `menu_translations` (`lang`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bills` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `table_id` INTEGER NOT NULL, `status` TEXT NOT NULL, `open_at` INTEGER NOT NULL, `closed_at` INTEGER, `people_count` INTEGER, `subtotal` INTEGER NOT NULL, `tax` INTEGER NOT NULL, `total_amount` INTEGER NOT NULL, `total_tax_10` INTEGER NOT NULL, `total_tax_8` INTEGER NOT NULL, `paid_amount` INTEGER NOT NULL, `payment_method` TEXT, `note` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bill_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bill_id` INTEGER NOT NULL, `order_id` INTEGER NOT NULL, `order_item_id` INTEGER NOT NULL, `menu_item_id` INTEGER NOT NULL, `name` TEXT NOT NULL, `unit_price` INTEGER NOT NULL, `quantity` INTEGER NOT NULL, `line_total` INTEGER NOT NULL, `tax_rate` REAL NOT NULL, `tax_amount` INTEGER NOT NULL, `is_paid` INTEGER NOT NULL, FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_items_bill_id` ON `bill_items` (`bill_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `payments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bill_id` INTEGER NOT NULL, `provider` TEXT NOT NULL, `provider_type` TEXT NOT NULL, `amount` INTEGER NOT NULL, `currency` TEXT NOT NULL, `status` TEXT NOT NULL, `provider_ref_id` TEXT, `merchant_payment_id` TEXT, `paypay_code_id` TEXT, `created_at` INTEGER NOT NULL, `confirmed_at` INTEGER, `extra_json` TEXT, FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_payments_bill_id` ON `payments` (`bill_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `receipts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `billId` INTEGER NOT NULL, `receiptNumber` TEXT NOT NULL, `issuedAt` INTEGER NOT NULL, `invoiceRegistrationNumber` TEXT NOT NULL, `taxIncluded` INTEGER NOT NULL, `subtotal` INTEGER NOT NULL, `taxAmount` INTEGER NOT NULL, `totalAmount` INTEGER NOT NULL, `taxBreakdownJson` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `payment_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `payment_id` INTEGER NOT NULL, `bill_item_id` INTEGER NOT NULL, `amount` INTEGER NOT NULL, FOREIGN KEY(`payment_id`) REFERENCES `payments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`bill_item_id`) REFERENCES `bill_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_payment_items_payment_id` ON `payment_items` (`payment_id`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_payment_items_bill_item_id` ON `payment_items` (`bill_item_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bf6e4db3a3791c0efe841c024e4c29b9')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `stores`");
        db.execSQL("DROP TABLE IF EXISTS `tables`");
        db.execSQL("DROP TABLE IF EXISTS `menu_items`");
        db.execSQL("DROP TABLE IF EXISTS `master_allergens`");
        db.execSQL("DROP TABLE IF EXISTS `master_menu_item_allergens`");
        db.execSQL("DROP TABLE IF EXISTS `orders`");
        db.execSQL("DROP TABLE IF EXISTS `order_items`");
        db.execSQL("DROP TABLE IF EXISTS `call_requests`");
        db.execSQL("DROP TABLE IF EXISTS `reservations`");
        db.execSQL("DROP TABLE IF EXISTS `allergen_feedback`");
        db.execSQL("DROP TABLE IF EXISTS `menu_translations`");
        db.execSQL("DROP TABLE IF EXISTS `bills`");
        db.execSQL("DROP TABLE IF EXISTS `bill_items`");
        db.execSQL("DROP TABLE IF EXISTS `payments`");
        db.execSQL("DROP TABLE IF EXISTS `receipts`");
        db.execSQL("DROP TABLE IF EXISTS `payment_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStores = new HashMap<String, TableInfo.Column>(11);
        _columnsStores.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("invoice_registration_number", new TableInfo.Column("invoice_registration_number", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("postal_code", new TableInfo.Column("postal_code", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("address_line1", new TableInfo.Column("address_line1", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("address_line2", new TableInfo.Column("address_line2", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("tax_included_pricing", new TableInfo.Column("tax_included_pricing", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("splash_image_path", new TableInfo.Column("splash_image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStores.put("theme_color", new TableInfo.Column("theme_color", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStores = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStores = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStores = new TableInfo("stores", _columnsStores, _foreignKeysStores, _indicesStores);
        final TableInfo _existingStores = TableInfo.read(db, "stores");
        if (!_infoStores.equals(_existingStores)) {
          return new RoomOpenHelper.ValidationResult(false, "stores(com.example.restomaster.data.local.entity.StoreEntity).\n"
                  + " Expected:\n" + _infoStores + "\n"
                  + " Found:\n" + _existingStores);
        }
        final HashMap<String, TableInfo.Column> _columnsTables = new HashMap<String, TableInfo.Column>(6);
        _columnsTables.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTables.put("storeId", new TableInfo.Column("storeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTables.put("tableNumber", new TableInfo.Column("tableNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTables.put("token", new TableInfo.Column("token", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTables.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTables.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTables = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTables = new HashSet<TableInfo.Index>(1);
        _indicesTables.add(new TableInfo.Index("index_tables_storeId_tableNumber", true, Arrays.asList("storeId", "tableNumber"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoTables = new TableInfo("tables", _columnsTables, _foreignKeysTables, _indicesTables);
        final TableInfo _existingTables = TableInfo.read(db, "tables");
        if (!_infoTables.equals(_existingTables)) {
          return new RoomOpenHelper.ValidationResult(false, "tables(com.example.restomaster.data.local.entity.TableEntity).\n"
                  + " Expected:\n" + _infoTables + "\n"
                  + " Found:\n" + _existingTables);
        }
        final HashMap<String, TableInfo.Column> _columnsMenuItems = new HashMap<String, TableInfo.Column>(10);
        _columnsMenuItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("storeId", new TableInfo.Column("storeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("price", new TableInfo.Column("price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("image_path", new TableInfo.Column("image_path", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("isAvailable", new TableInfo.Column("isAvailable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("tax_rate_type", new TableInfo.Column("tax_rate_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuItems.put("is_ai_generated", new TableInfo.Column("is_ai_generated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMenuItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMenuItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMenuItems = new TableInfo("menu_items", _columnsMenuItems, _foreignKeysMenuItems, _indicesMenuItems);
        final TableInfo _existingMenuItems = TableInfo.read(db, "menu_items");
        if (!_infoMenuItems.equals(_existingMenuItems)) {
          return new RoomOpenHelper.ValidationResult(false, "menu_items(com.example.restomaster.data.local.entity.MenuItemEntity).\n"
                  + " Expected:\n" + _infoMenuItems + "\n"
                  + " Found:\n" + _existingMenuItems);
        }
        final HashMap<String, TableInfo.Column> _columnsMasterAllergens = new HashMap<String, TableInfo.Column>(3);
        _columnsMasterAllergens.put("code", new TableInfo.Column("code", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterAllergens.put("labelJa", new TableInfo.Column("labelJa", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterAllergens.put("sortOrder", new TableInfo.Column("sortOrder", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMasterAllergens = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMasterAllergens = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMasterAllergens = new TableInfo("master_allergens", _columnsMasterAllergens, _foreignKeysMasterAllergens, _indicesMasterAllergens);
        final TableInfo _existingMasterAllergens = TableInfo.read(db, "master_allergens");
        if (!_infoMasterAllergens.equals(_existingMasterAllergens)) {
          return new RoomOpenHelper.ValidationResult(false, "master_allergens(com.example.restomaster.data.local.entity.MasterAllergenEntity).\n"
                  + " Expected:\n" + _infoMasterAllergens + "\n"
                  + " Found:\n" + _existingMasterAllergens);
        }
        final HashMap<String, TableInfo.Column> _columnsMasterMenuItemAllergens = new HashMap<String, TableInfo.Column>(3);
        _columnsMasterMenuItemAllergens.put("menuItemId", new TableInfo.Column("menuItemId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterMenuItemAllergens.put("allergenCode", new TableInfo.Column("allergenCode", "TEXT", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMasterMenuItemAllergens.put("source", new TableInfo.Column("source", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMasterMenuItemAllergens = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMasterMenuItemAllergens.add(new TableInfo.ForeignKey("menu_items", "CASCADE", "NO ACTION", Arrays.asList("menuItemId"), Arrays.asList("id")));
        _foreignKeysMasterMenuItemAllergens.add(new TableInfo.ForeignKey("master_allergens", "CASCADE", "NO ACTION", Arrays.asList("allergenCode"), Arrays.asList("code")));
        final HashSet<TableInfo.Index> _indicesMasterMenuItemAllergens = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMasterMenuItemAllergens = new TableInfo("master_menu_item_allergens", _columnsMasterMenuItemAllergens, _foreignKeysMasterMenuItemAllergens, _indicesMasterMenuItemAllergens);
        final TableInfo _existingMasterMenuItemAllergens = TableInfo.read(db, "master_menu_item_allergens");
        if (!_infoMasterMenuItemAllergens.equals(_existingMasterMenuItemAllergens)) {
          return new RoomOpenHelper.ValidationResult(false, "master_menu_item_allergens(com.example.restomaster.data.local.entity.MasterMenuItemAllergen).\n"
                  + " Expected:\n" + _infoMasterMenuItemAllergens + "\n"
                  + " Found:\n" + _existingMasterMenuItemAllergens);
        }
        final HashMap<String, TableInfo.Column> _columnsOrders = new HashMap<String, TableInfo.Column>(8);
        _columnsOrders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("storeId", new TableInfo.Column("storeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("tableId", new TableInfo.Column("tableId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("peopleCount", new TableInfo.Column("peopleCount", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("closed_at", new TableInfo.Column("closed_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrders.put("bill_id", new TableInfo.Column("bill_id", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrders = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrders = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrders = new TableInfo("orders", _columnsOrders, _foreignKeysOrders, _indicesOrders);
        final TableInfo _existingOrders = TableInfo.read(db, "orders");
        if (!_infoOrders.equals(_existingOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "orders(com.example.restomaster.data.local.entity.OrderEntity).\n"
                  + " Expected:\n" + _infoOrders + "\n"
                  + " Found:\n" + _existingOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsOrderItems = new HashMap<String, TableInfo.Column>(7);
        _columnsOrderItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("orderId", new TableInfo.Column("orderId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("menuItemId", new TableInfo.Column("menuItemId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("unitPrice", new TableInfo.Column("unitPrice", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsOrderItems.put("memo", new TableInfo.Column("memo", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysOrderItems = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesOrderItems = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoOrderItems = new TableInfo("order_items", _columnsOrderItems, _foreignKeysOrderItems, _indicesOrderItems);
        final TableInfo _existingOrderItems = TableInfo.read(db, "order_items");
        if (!_infoOrderItems.equals(_existingOrderItems)) {
          return new RoomOpenHelper.ValidationResult(false, "order_items(com.example.restomaster.data.local.entity.OrderItemEntity).\n"
                  + " Expected:\n" + _infoOrderItems + "\n"
                  + " Found:\n" + _existingOrderItems);
        }
        final HashMap<String, TableInfo.Column> _columnsCallRequests = new HashMap<String, TableInfo.Column>(7);
        _columnsCallRequests.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("storeId", new TableInfo.Column("storeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("tableId", new TableInfo.Column("tableId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCallRequests.put("handledAt", new TableInfo.Column("handledAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCallRequests = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCallRequests = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCallRequests = new TableInfo("call_requests", _columnsCallRequests, _foreignKeysCallRequests, _indicesCallRequests);
        final TableInfo _existingCallRequests = TableInfo.read(db, "call_requests");
        if (!_infoCallRequests.equals(_existingCallRequests)) {
          return new RoomOpenHelper.ValidationResult(false, "call_requests(com.example.restomaster.data.local.entity.CallRequestEntity).\n"
                  + " Expected:\n" + _infoCallRequests + "\n"
                  + " Found:\n" + _existingCallRequests);
        }
        final HashMap<String, TableInfo.Column> _columnsReservations = new HashMap<String, TableInfo.Column>(10);
        _columnsReservations.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("storeId", new TableInfo.Column("storeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("time", new TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("peopleCount", new TableInfo.Column("peopleCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("assignedTableId", new TableInfo.Column("assignedTableId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReservations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReservations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReservations = new TableInfo("reservations", _columnsReservations, _foreignKeysReservations, _indicesReservations);
        final TableInfo _existingReservations = TableInfo.read(db, "reservations");
        if (!_infoReservations.equals(_existingReservations)) {
          return new RoomOpenHelper.ValidationResult(false, "reservations(com.example.restomaster.data.local.entity.ReservationEntity).\n"
                  + " Expected:\n" + _infoReservations + "\n"
                  + " Found:\n" + _existingReservations);
        }
        final HashMap<String, TableInfo.Column> _columnsAllergenFeedback = new HashMap<String, TableInfo.Column>(6);
        _columnsAllergenFeedback.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAllergenFeedback.put("menuItemId", new TableInfo.Column("menuItemId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAllergenFeedback.put("allergenCode", new TableInfo.Column("allergenCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAllergenFeedback.put("aiSuggested", new TableInfo.Column("aiSuggested", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAllergenFeedback.put("userAction", new TableInfo.Column("userAction", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAllergenFeedback.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAllergenFeedback = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAllergenFeedback = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAllergenFeedback = new TableInfo("allergen_feedback", _columnsAllergenFeedback, _foreignKeysAllergenFeedback, _indicesAllergenFeedback);
        final TableInfo _existingAllergenFeedback = TableInfo.read(db, "allergen_feedback");
        if (!_infoAllergenFeedback.equals(_existingAllergenFeedback)) {
          return new RoomOpenHelper.ValidationResult(false, "allergen_feedback(com.example.restomaster.data.local.entity.AllergenFeedbackEntity).\n"
                  + " Expected:\n" + _infoAllergenFeedback + "\n"
                  + " Found:\n" + _existingAllergenFeedback);
        }
        final HashMap<String, TableInfo.Column> _columnsMenuTranslations = new HashMap<String, TableInfo.Column>(7);
        _columnsMenuTranslations.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("menu_id", new TableInfo.Column("menu_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("lang", new TableInfo.Column("lang", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMenuTranslations.put("updated_at", new TableInfo.Column("updated_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMenuTranslations = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMenuTranslations.add(new TableInfo.ForeignKey("menu_items", "CASCADE", "NO ACTION", Arrays.asList("menu_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMenuTranslations = new HashSet<TableInfo.Index>(2);
        _indicesMenuTranslations.add(new TableInfo.Index("index_menu_translations_menu_id", false, Arrays.asList("menu_id"), Arrays.asList("ASC")));
        _indicesMenuTranslations.add(new TableInfo.Index("index_menu_translations_lang", false, Arrays.asList("lang"), Arrays.asList("ASC")));
        final TableInfo _infoMenuTranslations = new TableInfo("menu_translations", _columnsMenuTranslations, _foreignKeysMenuTranslations, _indicesMenuTranslations);
        final TableInfo _existingMenuTranslations = TableInfo.read(db, "menu_translations");
        if (!_infoMenuTranslations.equals(_existingMenuTranslations)) {
          return new RoomOpenHelper.ValidationResult(false, "menu_translations(com.example.restomaster.data.local.entity.MenuTranslationEntity).\n"
                  + " Expected:\n" + _infoMenuTranslations + "\n"
                  + " Found:\n" + _existingMenuTranslations);
        }
        final HashMap<String, TableInfo.Column> _columnsBills = new HashMap<String, TableInfo.Column>(14);
        _columnsBills.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("table_id", new TableInfo.Column("table_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("open_at", new TableInfo.Column("open_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("closed_at", new TableInfo.Column("closed_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("people_count", new TableInfo.Column("people_count", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("subtotal", new TableInfo.Column("subtotal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("tax", new TableInfo.Column("tax", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("total_amount", new TableInfo.Column("total_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("total_tax_10", new TableInfo.Column("total_tax_10", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("total_tax_8", new TableInfo.Column("total_tax_8", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("paid_amount", new TableInfo.Column("paid_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("payment_method", new TableInfo.Column("payment_method", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("note", new TableInfo.Column("note", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBills = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBills = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBills = new TableInfo("bills", _columnsBills, _foreignKeysBills, _indicesBills);
        final TableInfo _existingBills = TableInfo.read(db, "bills");
        if (!_infoBills.equals(_existingBills)) {
          return new RoomOpenHelper.ValidationResult(false, "bills(com.example.restomaster.data.local.entity.BillEntity).\n"
                  + " Expected:\n" + _infoBills + "\n"
                  + " Found:\n" + _existingBills);
        }
        final HashMap<String, TableInfo.Column> _columnsBillItems = new HashMap<String, TableInfo.Column>(12);
        _columnsBillItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("bill_id", new TableInfo.Column("bill_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("order_id", new TableInfo.Column("order_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("order_item_id", new TableInfo.Column("order_item_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("menu_item_id", new TableInfo.Column("menu_item_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("unit_price", new TableInfo.Column("unit_price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("quantity", new TableInfo.Column("quantity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("line_total", new TableInfo.Column("line_total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("tax_rate", new TableInfo.Column("tax_rate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("tax_amount", new TableInfo.Column("tax_amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("is_paid", new TableInfo.Column("is_paid", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBillItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBillItems.add(new TableInfo.ForeignKey("bills", "CASCADE", "NO ACTION", Arrays.asList("bill_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesBillItems = new HashSet<TableInfo.Index>(1);
        _indicesBillItems.add(new TableInfo.Index("index_bill_items_bill_id", false, Arrays.asList("bill_id"), Arrays.asList("ASC")));
        final TableInfo _infoBillItems = new TableInfo("bill_items", _columnsBillItems, _foreignKeysBillItems, _indicesBillItems);
        final TableInfo _existingBillItems = TableInfo.read(db, "bill_items");
        if (!_infoBillItems.equals(_existingBillItems)) {
          return new RoomOpenHelper.ValidationResult(false, "bill_items(com.example.restomaster.data.local.entity.BillItemEntity).\n"
                  + " Expected:\n" + _infoBillItems + "\n"
                  + " Found:\n" + _existingBillItems);
        }
        final HashMap<String, TableInfo.Column> _columnsPayments = new HashMap<String, TableInfo.Column>(13);
        _columnsPayments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("bill_id", new TableInfo.Column("bill_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("provider", new TableInfo.Column("provider", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("provider_type", new TableInfo.Column("provider_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("provider_ref_id", new TableInfo.Column("provider_ref_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("merchant_payment_id", new TableInfo.Column("merchant_payment_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("paypay_code_id", new TableInfo.Column("paypay_code_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("created_at", new TableInfo.Column("created_at", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("confirmed_at", new TableInfo.Column("confirmed_at", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayments.put("extra_json", new TableInfo.Column("extra_json", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPayments = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPayments.add(new TableInfo.ForeignKey("bills", "CASCADE", "NO ACTION", Arrays.asList("bill_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPayments = new HashSet<TableInfo.Index>(1);
        _indicesPayments.add(new TableInfo.Index("index_payments_bill_id", false, Arrays.asList("bill_id"), Arrays.asList("ASC")));
        final TableInfo _infoPayments = new TableInfo("payments", _columnsPayments, _foreignKeysPayments, _indicesPayments);
        final TableInfo _existingPayments = TableInfo.read(db, "payments");
        if (!_infoPayments.equals(_existingPayments)) {
          return new RoomOpenHelper.ValidationResult(false, "payments(com.example.restomaster.data.local.entity.PaymentEntity).\n"
                  + " Expected:\n" + _infoPayments + "\n"
                  + " Found:\n" + _existingPayments);
        }
        final HashMap<String, TableInfo.Column> _columnsReceipts = new HashMap<String, TableInfo.Column>(10);
        _columnsReceipts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("billId", new TableInfo.Column("billId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("receiptNumber", new TableInfo.Column("receiptNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("issuedAt", new TableInfo.Column("issuedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("invoiceRegistrationNumber", new TableInfo.Column("invoiceRegistrationNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("taxIncluded", new TableInfo.Column("taxIncluded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("subtotal", new TableInfo.Column("subtotal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("taxAmount", new TableInfo.Column("taxAmount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("totalAmount", new TableInfo.Column("totalAmount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReceipts.put("taxBreakdownJson", new TableInfo.Column("taxBreakdownJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReceipts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReceipts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReceipts = new TableInfo("receipts", _columnsReceipts, _foreignKeysReceipts, _indicesReceipts);
        final TableInfo _existingReceipts = TableInfo.read(db, "receipts");
        if (!_infoReceipts.equals(_existingReceipts)) {
          return new RoomOpenHelper.ValidationResult(false, "receipts(com.example.restomaster.data.local.entity.ReceiptEntity).\n"
                  + " Expected:\n" + _infoReceipts + "\n"
                  + " Found:\n" + _existingReceipts);
        }
        final HashMap<String, TableInfo.Column> _columnsPaymentItems = new HashMap<String, TableInfo.Column>(4);
        _columnsPaymentItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentItems.put("payment_id", new TableInfo.Column("payment_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentItems.put("bill_item_id", new TableInfo.Column("bill_item_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaymentItems.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPaymentItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysPaymentItems.add(new TableInfo.ForeignKey("payments", "CASCADE", "NO ACTION", Arrays.asList("payment_id"), Arrays.asList("id")));
        _foreignKeysPaymentItems.add(new TableInfo.ForeignKey("bill_items", "CASCADE", "NO ACTION", Arrays.asList("bill_item_id"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesPaymentItems = new HashSet<TableInfo.Index>(2);
        _indicesPaymentItems.add(new TableInfo.Index("index_payment_items_payment_id", false, Arrays.asList("payment_id"), Arrays.asList("ASC")));
        _indicesPaymentItems.add(new TableInfo.Index("index_payment_items_bill_item_id", false, Arrays.asList("bill_item_id"), Arrays.asList("ASC")));
        final TableInfo _infoPaymentItems = new TableInfo("payment_items", _columnsPaymentItems, _foreignKeysPaymentItems, _indicesPaymentItems);
        final TableInfo _existingPaymentItems = TableInfo.read(db, "payment_items");
        if (!_infoPaymentItems.equals(_existingPaymentItems)) {
          return new RoomOpenHelper.ValidationResult(false, "payment_items(com.example.restomaster.data.local.entity.PaymentItemEntity).\n"
                  + " Expected:\n" + _infoPaymentItems + "\n"
                  + " Found:\n" + _existingPaymentItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bf6e4db3a3791c0efe841c024e4c29b9", "0f80aa1c9b9047c3fff0d964b3a7d5d8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "stores","tables","menu_items","master_allergens","master_menu_item_allergens","orders","order_items","call_requests","reservations","allergen_feedback","menu_translations","bills","bill_items","payments","receipts","payment_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `stores`");
      _db.execSQL("DELETE FROM `tables`");
      _db.execSQL("DELETE FROM `menu_items`");
      _db.execSQL("DELETE FROM `master_allergens`");
      _db.execSQL("DELETE FROM `master_menu_item_allergens`");
      _db.execSQL("DELETE FROM `orders`");
      _db.execSQL("DELETE FROM `order_items`");
      _db.execSQL("DELETE FROM `call_requests`");
      _db.execSQL("DELETE FROM `reservations`");
      _db.execSQL("DELETE FROM `allergen_feedback`");
      _db.execSQL("DELETE FROM `menu_translations`");
      _db.execSQL("DELETE FROM `bills`");
      _db.execSQL("DELETE FROM `bill_items`");
      _db.execSQL("DELETE FROM `payments`");
      _db.execSQL("DELETE FROM `receipts`");
      _db.execSQL("DELETE FROM `payment_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(StoreDao.class, StoreDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TableDao.class, TableDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MenuDao.class, MenuDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(OrderDao.class, OrderDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReservationDao.class, ReservationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MasterAllergenDao.class, MasterAllergenDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MasterMenuItemAllergenDao.class, MasterMenuItemAllergenDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AllergenFeedbackDao.class, AllergenFeedbackDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CallRequestDao.class, CallRequestDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MenuTranslationDao.class, MenuTranslationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillDao.class, BillDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillItemDao.class, BillItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PaymentDao.class, PaymentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReceiptDao.class, ReceiptDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PaymentItemDao.class, PaymentItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ClosingDao.class, ClosingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public StoreDao storeDao() {
    if (_storeDao != null) {
      return _storeDao;
    } else {
      synchronized(this) {
        if(_storeDao == null) {
          _storeDao = new StoreDao_Impl(this);
        }
        return _storeDao;
      }
    }
  }

  @Override
  public TableDao tableDao() {
    if (_tableDao != null) {
      return _tableDao;
    } else {
      synchronized(this) {
        if(_tableDao == null) {
          _tableDao = new TableDao_Impl(this);
        }
        return _tableDao;
      }
    }
  }

  @Override
  public MenuDao menuDao() {
    if (_menuDao != null) {
      return _menuDao;
    } else {
      synchronized(this) {
        if(_menuDao == null) {
          _menuDao = new MenuDao_Impl(this);
        }
        return _menuDao;
      }
    }
  }

  @Override
  public OrderDao orderDao() {
    if (_orderDao != null) {
      return _orderDao;
    } else {
      synchronized(this) {
        if(_orderDao == null) {
          _orderDao = new OrderDao_Impl(this);
        }
        return _orderDao;
      }
    }
  }

  @Override
  public ReservationDao reservationDao() {
    if (_reservationDao != null) {
      return _reservationDao;
    } else {
      synchronized(this) {
        if(_reservationDao == null) {
          _reservationDao = new ReservationDao_Impl(this);
        }
        return _reservationDao;
      }
    }
  }

  @Override
  public MasterAllergenDao masterAllergenDao() {
    if (_masterAllergenDao != null) {
      return _masterAllergenDao;
    } else {
      synchronized(this) {
        if(_masterAllergenDao == null) {
          _masterAllergenDao = new MasterAllergenDao_Impl(this);
        }
        return _masterAllergenDao;
      }
    }
  }

  @Override
  public MasterMenuItemAllergenDao masterMenuItemAllergenDao() {
    if (_masterMenuItemAllergenDao != null) {
      return _masterMenuItemAllergenDao;
    } else {
      synchronized(this) {
        if(_masterMenuItemAllergenDao == null) {
          _masterMenuItemAllergenDao = new MasterMenuItemAllergenDao_Impl(this);
        }
        return _masterMenuItemAllergenDao;
      }
    }
  }

  @Override
  public AllergenFeedbackDao allergenFeedbackDao() {
    if (_allergenFeedbackDao != null) {
      return _allergenFeedbackDao;
    } else {
      synchronized(this) {
        if(_allergenFeedbackDao == null) {
          _allergenFeedbackDao = new AllergenFeedbackDao_Impl(this);
        }
        return _allergenFeedbackDao;
      }
    }
  }

  @Override
  public CallRequestDao callRequestDao() {
    if (_callRequestDao != null) {
      return _callRequestDao;
    } else {
      synchronized(this) {
        if(_callRequestDao == null) {
          _callRequestDao = new CallRequestDao_Impl(this);
        }
        return _callRequestDao;
      }
    }
  }

  @Override
  public MenuTranslationDao menuTranslationDao() {
    if (_menuTranslationDao != null) {
      return _menuTranslationDao;
    } else {
      synchronized(this) {
        if(_menuTranslationDao == null) {
          _menuTranslationDao = new MenuTranslationDao_Impl(this);
        }
        return _menuTranslationDao;
      }
    }
  }

  @Override
  public BillDao billDao() {
    if (_billDao != null) {
      return _billDao;
    } else {
      synchronized(this) {
        if(_billDao == null) {
          _billDao = new BillDao_Impl(this);
        }
        return _billDao;
      }
    }
  }

  @Override
  public BillItemDao billItemDao() {
    if (_billItemDao != null) {
      return _billItemDao;
    } else {
      synchronized(this) {
        if(_billItemDao == null) {
          _billItemDao = new BillItemDao_Impl(this);
        }
        return _billItemDao;
      }
    }
  }

  @Override
  public PaymentDao paymentDao() {
    if (_paymentDao != null) {
      return _paymentDao;
    } else {
      synchronized(this) {
        if(_paymentDao == null) {
          _paymentDao = new PaymentDao_Impl(this);
        }
        return _paymentDao;
      }
    }
  }

  @Override
  public ReceiptDao receiptDao() {
    if (_receiptDao != null) {
      return _receiptDao;
    } else {
      synchronized(this) {
        if(_receiptDao == null) {
          _receiptDao = new ReceiptDao_Impl(this);
        }
        return _receiptDao;
      }
    }
  }

  @Override
  public PaymentItemDao paymentItemDao() {
    if (_paymentItemDao != null) {
      return _paymentItemDao;
    } else {
      synchronized(this) {
        if(_paymentItemDao == null) {
          _paymentItemDao = new PaymentItemDao_Impl(this);
        }
        return _paymentItemDao;
      }
    }
  }

  @Override
  public ClosingDao closingDao() {
    if (_closingDao != null) {
      return _closingDao;
    } else {
      synchronized(this) {
        if(_closingDao == null) {
          _closingDao = new ClosingDao_Impl(this);
        }
        return _closingDao;
      }
    }
  }
}
