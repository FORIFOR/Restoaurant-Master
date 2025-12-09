package com.example.restomaster.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Drop/createを安全に行うため一時的にFKを無効化
            db.execSQL("PRAGMA foreign_keys=OFF")

            recreateTable(
                db = db,
                table = "stores",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `stores_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `address` TEXT,
                        `phone` TEXT
                    )
                """.trimIndent(),
                columns = listOf("id", "name", "address", "phone")
            )

            recreateTable(
                db = db,
                table = "tables",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `tables_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `storeId` INTEGER NOT NULL,
                        `tableNumber` INTEGER NOT NULL,
                        `token` TEXT NOT NULL,
                        `isActive` INTEGER NOT NULL
                    )
                """.trimIndent(),
                columns = listOf("id", "storeId", "tableNumber", "token", "isActive")
            )
            db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tables_storeId_tableNumber` ON `tables` (`storeId`, `tableNumber`)")

            recreateTable(
                db = db,
                table = "menu_items",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `menu_items_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `storeId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT,
                        `price` INTEGER NOT NULL,
                        `category` TEXT NOT NULL,
                        `imagePath` TEXT,
                        `isAvailable` INTEGER NOT NULL
                    )
                """.trimIndent(),
                columns = listOf(
                    "id",
                    "storeId",
                    "name",
                    "description",
                    "price",
                    "category",
                    "imagePath",
                    "isAvailable"
                )
            )

            recreateTable(
                db = db,
                table = "orders",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `orders_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `storeId` INTEGER NOT NULL,
                        `tableId` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `peopleCount` INTEGER,
                        `createdAt` INTEGER NOT NULL,
                        `closedAt` INTEGER
                    )
                """.trimIndent(),
                columns = listOf(
                    "id",
                    "storeId",
                    "tableId",
                    "status",
                    "peopleCount",
                    "createdAt",
                    "closedAt"
                )
            )

            recreateTable(
                db = db,
                table = "order_items",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `order_items_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `orderId` INTEGER NOT NULL,
                        `menuItemId` INTEGER NOT NULL,
                        `quantity` INTEGER NOT NULL,
                        `unitPrice` INTEGER NOT NULL,
                        `status` TEXT NOT NULL,
                        `memo` TEXT
                    )
                """.trimIndent(),
                columns = listOf(
                    "id",
                    "orderId",
                    "menuItemId",
                    "quantity",
                    "unitPrice",
                    "status",
                    "memo"
                )
            )

            recreateTable(
                db = db,
                table = "call_requests",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `call_requests_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `storeId` INTEGER NOT NULL,
                        `tableId` INTEGER NOT NULL,
                        `type` TEXT NOT NULL,
                        `status` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `handledAt` INTEGER
                    )
                """.trimIndent(),
                columns = listOf(
                    "id",
                    "storeId",
                    "tableId",
                    "type",
                    "status",
                    "createdAt",
                    "handledAt"
                )
            )

            recreateTable(
                db = db,
                table = "reservations",
                createSql = """
                    CREATE TABLE IF NOT EXISTS `reservations_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `storeId` INTEGER NOT NULL,
                        `date` TEXT NOT NULL,
                        `time` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `phone` TEXT,
                        `peopleCount` INTEGER NOT NULL,
                        `assignedTableId` INTEGER,
                        `note` TEXT,
                        `status` TEXT NOT NULL
                    )
                """.trimIndent(),
                columns = listOf(
                    "id",
                    "storeId",
                    "date",
                    "time",
                    "name",
                    "phone",
                    "peopleCount",
                    "assignedTableId",
                    "note",
                    "status"
                )
            )

            // アレルゲン関連はv2で追加された可能性が高いため、空の新規作成のみ行う
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `master_allergens` (
                    `code` TEXT NOT NULL,
                    `labelJa` TEXT NOT NULL,
                    `sortOrder` INTEGER NOT NULL,
                    PRIMARY KEY(`code`)
                )
                """.trimIndent()
            )

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `master_menu_item_allergens` (
                    `menuItemId` INTEGER NOT NULL,
                    `allergenCode` TEXT NOT NULL,
                    `source` TEXT NOT NULL,
                    PRIMARY KEY(`menuItemId`, `allergenCode`),
                    FOREIGN KEY(`menuItemId`) REFERENCES `menu_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                    FOREIGN KEY(`allergenCode`) REFERENCES `master_allergens`(`code`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent()
            )

            db.execSQL("PRAGMA foreign_keys=ON")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `allergen_feedback` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `menuItemId` INTEGER NOT NULL,
                    `allergenCode` TEXT NOT NULL,
                    `aiSuggested` INTEGER NOT NULL,
                    `userAction` TEXT NOT NULL,
                    `timestamp` INTEGER NOT NULL
                )
                """.trimIndent()
            )
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `menu_translations` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `menu_id` INTEGER NOT NULL,
                    `lang` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `description` TEXT,
                    `status` TEXT NOT NULL,
                    `updated_at` INTEGER NOT NULL,
                    FOREIGN KEY(`menu_id`) REFERENCES `menu_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
                """.trimIndent()
            )
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_menu_translations_menu_id` ON `menu_translations` (`menu_id`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_menu_translations_lang` ON `menu_translations` (`lang`)")
        }
    }

    val MIGRATION_4_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `menu_items` ADD COLUMN `is_ai_generated` INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Bills
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bills` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `table_id` INTEGER NOT NULL,
                    `opened_at` INTEGER NOT NULL,
                    `closed_at` INTEGER,
                    `status` TEXT NOT NULL,
                    `people_count` INTEGER,
                    `memo` TEXT
                )
            """.trimIndent())

            // Bill Items
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bill_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `menu_item_id` INTEGER NOT NULL,
                    `name` TEXT NOT NULL,
                    `price` INTEGER NOT NULL,
                    `quantity` INTEGER NOT NULL,
                    `amount` INTEGER NOT NULL,
                    `settled_payment_id` INTEGER,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_items_bill_id` ON `bill_items` (`bill_id`)")

            // Payments
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `payments` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `amount` INTEGER NOT NULL,
                    `method` TEXT NOT NULL,
                    `provider` TEXT,
                    `status` TEXT NOT NULL,
                    `paid_at` INTEGER,
                    `transaction_id` TEXT,
                    `meta_json` TEXT,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_payments_bill_id` ON `payments` (`bill_id`)")

            // Receipts
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `receipts` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `issued_at` INTEGER NOT NULL,
                    `receipt_number` TEXT NOT NULL,
                    `content_json` TEXT NOT NULL,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_receipts_bill_id` ON `receipts` (`bill_id`)")
        }
    }

    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Add bill_id to orders
            database.execSQL("ALTER TABLE `orders` ADD COLUMN `bill_id` INTEGER")

            // 2. Recreate bills table with new schema
            // Drop old tables that depend on bills
            database.execSQL("DROP TABLE IF EXISTS `payments`")
            database.execSQL("DROP TABLE IF EXISTS `bill_items`")
            database.execSQL("DROP TABLE IF EXISTS `receipts`")
            database.execSQL("DROP TABLE IF EXISTS `bills`")

            // Create new bills table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bills` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `table_id` INTEGER NOT NULL,
                    `status` TEXT NOT NULL,
                    `open_at` INTEGER NOT NULL,
                    `closed_at` INTEGER,
                    `subtotal` INTEGER NOT NULL,
                    `tax` INTEGER NOT NULL,
                    `total_amount` INTEGER NOT NULL,
                    `payment_method` TEXT,
                    `note` TEXT
                )
            """.trimIndent())

            // Recreate payments table with new schema
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `payments` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `provider` TEXT NOT NULL,
                    `provider_type` TEXT NOT NULL,
                    `amount` INTEGER NOT NULL,
                    `currency` TEXT NOT NULL,
                    `status` TEXT NOT NULL,
                    `provider_ref_id` TEXT,
                    `created_at` INTEGER NOT NULL,
                    `confirmed_at` INTEGER,
                    `extra_json` TEXT,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_payments_bill_id` ON `payments` (`bill_id`)")

            // Recreate bill_items (schema didn't change in user request, but we dropped it)
            // We'll keep the one from MIGRATION_5_6 but we need to recreate it since we dropped it
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bill_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `menu_item_id` INTEGER NOT NULL,
                    `name` TEXT NOT NULL,
                    `price` INTEGER NOT NULL,
                    `quantity` INTEGER NOT NULL,
                    `amount` INTEGER NOT NULL,
                    `settled_payment_id` INTEGER,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_items_bill_id` ON `bill_items` (`bill_id`)")

            // Recreate receipts (schema didn't change)
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `receipts` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `issued_at` INTEGER NOT NULL,
                    `receipt_number` TEXT NOT NULL,
                    `content_json` TEXT NOT NULL,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_receipts_bill_id` ON `receipts` (`bill_id`)")

            // 3. Create bill_splits table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bill_splits` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `label` TEXT,
                    `amount` INTEGER NOT NULL,
                    `payment_id` INTEGER,
                    `status` TEXT NOT NULL,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                    FOREIGN KEY(`payment_id`) REFERENCES `payments`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_splits_bill_id` ON `bill_splits` (`bill_id`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_splits_payment_id` ON `bill_splits` (`payment_id`)")
        }
    }

    val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Add people_count and paid_amount to bills
            database.execSQL("ALTER TABLE `bills` ADD COLUMN `people_count` INTEGER")
            database.execSQL("ALTER TABLE `bills` ADD COLUMN `paid_amount` INTEGER NOT NULL DEFAULT 0")

            // 2. Drop and recreate bill_items with new schema
            database.execSQL("DROP TABLE IF EXISTS `bill_items`")
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `bill_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `bill_id` INTEGER NOT NULL,
                    `order_id` INTEGER NOT NULL,
                    `order_item_id` INTEGER NOT NULL,
                    `menu_item_id` INTEGER NOT NULL,
                    `name` TEXT NOT NULL,
                    `unit_price` INTEGER NOT NULL,
                    `quantity` INTEGER NOT NULL,
                    `line_total` INTEGER NOT NULL,
                    `tax_rate` REAL NOT NULL,
                    `is_paid` INTEGER NOT NULL DEFAULT 0,
                    FOREIGN KEY(`bill_id`) REFERENCES `bills`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_items_bill_id` ON `bill_items` (`bill_id`)")

            // 3. Create payment_items table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `payment_items` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `payment_id` INTEGER NOT NULL,
                    `bill_item_id` INTEGER NOT NULL,
                    `amount` INTEGER NOT NULL,
                    FOREIGN KEY(`payment_id`) REFERENCES `payments`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE,
                    FOREIGN KEY(`bill_item_id`) REFERENCES `bill_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                )
            """.trimIndent())
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_payment_items_payment_id` ON `payment_items` (`payment_id`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_payment_items_bill_item_id` ON `payment_items` (`bill_item_id`)")

            // 4. Drop bill_splits (superseded by payment_items)
            database.execSQL("DROP TABLE IF EXISTS `bill_splits`")
        }
    }

    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // 1. Add invoice-related columns to stores
            database.execSQL("ALTER TABLE `stores` ADD COLUMN `invoice_registration_number` TEXT")
            database.execSQL("ALTER TABLE `stores` ADD COLUMN `postal_code` TEXT")
            database.execSQL("ALTER TABLE `stores` ADD COLUMN `address_line1` TEXT")
            database.execSQL("ALTER TABLE `stores` ADD COLUMN `address_line2` TEXT")

            // 2. Fix orders table column name (closedAt -> closed_at) if it exists with wrong name
            // SQLite doesn't support RENAME COLUMN directly in older versions, so we need to recreate the table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `orders_new` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `storeId` INTEGER NOT NULL,
                    `tableId` INTEGER NOT NULL,
                    `status` TEXT NOT NULL,
                    `peopleCount` INTEGER,
                    `createdAt` INTEGER NOT NULL,
                    `closed_at` INTEGER,
                    `bill_id` INTEGER
                )
            """.trimIndent())
            database.execSQL("""
                INSERT INTO `orders_new` (`id`, `storeId`, `tableId`, `status`, `peopleCount`, `createdAt`, `closed_at`, `bill_id`)
                SELECT `id`, `storeId`, `tableId`, `status`, `peopleCount`, `createdAt`, `closedAt`, `bill_id`
                FROM `orders`
            """.trimIndent())
            database.execSQL("DROP TABLE `orders`")
            database.execSQL("ALTER TABLE `orders_new` RENAME TO `orders`")

            // 3. Drop and recreate receipts table with new schema
            database.execSQL("DROP TABLE IF EXISTS `receipts`")
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `receipts` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `billId` INTEGER NOT NULL,
                    `receiptNumber` TEXT NOT NULL,
                    `issuedAt` INTEGER NOT NULL,
                    `invoiceRegistrationNumber` TEXT NOT NULL,
                    `taxIncluded` INTEGER NOT NULL,
                    `subtotal` INTEGER NOT NULL,
                    `taxAmount` INTEGER NOT NULL,
                    `totalAmount` INTEGER NOT NULL,
                    `taxBreakdownJson` TEXT
                )
            """.trimIndent())
        }
    }

    val MIGRATION_9_10 = object : Migration(9, 10) {
        override fun migrate(db: SupportSQLiteDatabase) {
            val hasClosedAtSnake = hasColumn(db, "orders", "closed_at")
            val hasClosedAtCamel = hasColumn(db, "orders", "closedAt")
            val hasBillId = hasColumn(db, "orders", "bill_id")
            val closedAtSelect = when {
                hasClosedAtSnake -> "closed_at"
                hasClosedAtCamel -> "closedAt"
                else -> "NULL"
            }
            val billIdSelect = if (hasBillId) "bill_id" else "NULL"

            // Ensure menu_items has tax_rate_type column
            if (!hasColumn(db, "menu_items", "tax_rate_type")) {
                db.execSQL(
                    "ALTER TABLE `menu_items` ADD COLUMN `tax_rate_type` TEXT NOT NULL DEFAULT 'STANDARD_10'"
                )
            }

            // Ensure bills has tax breakdown columns (added in newer schema)
            if (!hasColumn(db, "bills", "total_tax_10")) {
                db.execSQL("ALTER TABLE `bills` ADD COLUMN `total_tax_10` INTEGER NOT NULL DEFAULT 0")
            }
            if (!hasColumn(db, "bills", "total_tax_8")) {
                db.execSQL("ALTER TABLE `bills` ADD COLUMN `total_tax_8` INTEGER NOT NULL DEFAULT 0")
            }
            if (!hasColumn(db, "bills", "people_count")) {
                db.execSQL("ALTER TABLE `bills` ADD COLUMN `people_count` INTEGER")
            }
            if (!hasColumn(db, "bills", "paid_amount")) {
                db.execSQL("ALTER TABLE `bills` ADD COLUMN `paid_amount` INTEGER NOT NULL DEFAULT 0")
            }
            if (!hasColumn(db, "bill_items", "tax_amount")) {
                db.execSQL("ALTER TABLE `bill_items` ADD COLUMN `tax_amount` INTEGER NOT NULL DEFAULT 0")
            }

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `orders_new` (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    `storeId` INTEGER NOT NULL,
                    `tableId` INTEGER NOT NULL,
                    `status` TEXT NOT NULL,
                    `peopleCount` INTEGER,
                    `createdAt` INTEGER NOT NULL,
                    `closed_at` INTEGER,
                    `bill_id` INTEGER
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                INSERT INTO `orders_new` (id, storeId, tableId, status, peopleCount, createdAt, closed_at, bill_id)
                SELECT id, storeId, tableId, status, peopleCount, createdAt, $closedAtSelect, $billIdSelect
                FROM `orders`
                """.trimIndent()
            )
            db.execSQL("DROP TABLE IF EXISTS `orders`")
            db.execSQL("ALTER TABLE `orders_new` RENAME TO `orders`")
        }
    }

    val MIGRATION_10_11 = object : Migration(10, 11) {
        override fun migrate(db: SupportSQLiteDatabase) {
            if (!hasColumn(db, "stores", "tax_included_pricing")) {
                db.execSQL("ALTER TABLE `stores` ADD COLUMN `tax_included_pricing` INTEGER NOT NULL DEFAULT 1")
            }
        }
    }

    val MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Add PayPay-specific columns to payments table
            if (!hasColumn(db, "payments", "merchant_payment_id")) {
                db.execSQL("ALTER TABLE `payments` ADD COLUMN `merchant_payment_id` TEXT")
            }
            if (!hasColumn(db, "payments", "paypay_code_id")) {
                db.execSQL("ALTER TABLE `payments` ADD COLUMN `paypay_code_id` TEXT")
            }
        }
    }

    val MIGRATION_12_13 = object : Migration(12, 13) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Add customer web app customization columns to stores table
            if (!hasColumn(db, "stores", "splash_image_path")) {
                db.execSQL("ALTER TABLE `stores` ADD COLUMN `splash_image_path` TEXT")
            }
            if (!hasColumn(db, "stores", "theme_color")) {
                db.execSQL("ALTER TABLE `stores` ADD COLUMN `theme_color` TEXT")
            }
        }
    }

    val MIGRATION_13_14 = object : Migration(13, 14) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Add status column to tables
            if (!hasColumn(db, "tables", "status")) {
                db.execSQL("ALTER TABLE `tables` ADD COLUMN `status` TEXT NOT NULL DEFAULT 'ACTIVE'")
            }
        }
    }

    private fun recreateTable(
        db: SupportSQLiteDatabase,
        table: String,
        createSql: String,
        columns: List<String>
    ) {
        db.execSQL(createSql)
        val newTable = "${table}_new"
        val joinedColumns = columns.joinToString(separator = ",")
        if (hasAllColumns(db, table, columns)) {
            db.execSQL(
                "INSERT INTO `$newTable` ($joinedColumns) SELECT $joinedColumns FROM `$table`"
            )
        }
        db.execSQL("DROP TABLE IF EXISTS `$table`")
        db.execSQL("ALTER TABLE `$newTable` RENAME TO `$table`")
    }

    private fun hasAllColumns(
        db: SupportSQLiteDatabase,
        table: String,
        columns: List<String>
    ): Boolean {
        val existing = mutableSetOf<String>()
        db.query("PRAGMA table_info(`$table`)").use { cursor ->
            val nameIndex = cursor.getColumnIndex("name")
            while (cursor.moveToNext() && nameIndex >= 0) {
                existing.add(cursor.getString(nameIndex))
            }
        }
        return columns.all { existing.contains(it) }
    }

    private fun hasColumn(db: SupportSQLiteDatabase, table: String, column: String): Boolean {
        db.query("PRAGMA table_info(`$table`)").use { cursor ->
            val nameIndex = cursor.getColumnIndex("name")
            while (cursor.moveToNext() && nameIndex >= 0) {
                if (cursor.getString(nameIndex) == column) {
                    return true
                }
            }
        }
        return false
    }
}
