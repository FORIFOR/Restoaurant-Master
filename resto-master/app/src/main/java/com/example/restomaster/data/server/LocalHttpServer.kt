package com.example.restomaster.data.server

import android.util.Log
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import com.example.restomaster.domain.repository.MenuRepository
import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.util.AppLog
import io.ktor.server.request.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ローカルHTTPサーバ（Ktor埋め込みサーバ）
 * お客さん用スマホからのメニュー閲覧・注文受付を処理
 */
@Singleton
class LocalHttpServer @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: android.content.Context,
    private val orderRepository: OrderRepository,
    private val menuRepository: MenuRepository,
    private val tableDao: TableDao,
    private val storeDao: com.example.restomaster.data.local.dao.StoreDao,
    private val translationRepository: com.example.restomaster.data.repository.TranslationRepository,
    private val callRequestDao: com.example.restomaster.data.local.dao.CallRequestDao,
    private val billingRepository: com.example.restomaster.data.repository.BillingRepository
) {
    private var engine: ApplicationEngine? = null
    private val TAG = "LocalHttpServer"

    /**
     * サーバを起動
     * @param port ポート番号（デフォルト: 8080）
     */
    fun start(port: Int = 8080) {
        if (engine != null) {
            AppLog.server("Server already running on port=$port")
            return
        }

        AppLog.server("Starting server on port=$port, mode=HOST")

        // Capture the Android context to avoid shadowing by Ktor's context
        val appContext = context

        engine = embeddedServer(CIO, port = port) {
            // JSON シリアライゼーションを有効化
            install(ContentNegotiation) {
                json()
            }

            routing {
                // ヘルスチェック用エンドポイント
                get("/health") {
                    val remote = call.request.local.remoteHost
                    AppLog.http("GET /health from $remote")
                    call.respondText("OK", ContentType.Text.Plain)
                }

                // 静的ファイル（スプラッシュ画像）
                get("/static/splash/{filename}") {
                    val filename = call.parameters["filename"]
                    if (filename == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing filename")
                        return@get
                    }
                    
                    val splashDir = java.io.File(appContext.filesDir, "splash")
                    val file = java.io.File(splashDir, filename)
                    
                    if (!file.exists() || !file.isFile) {
                        AppLog.http("GET /static/splash/$filename -> 404 (File not found: ${file.absolutePath})")
                        call.respond(HttpStatusCode.NotFound, "File not found")
                        return@get
                    }

                    AppLog.http("GET /static/splash/$filename -> 200 (${file.length()} bytes)")
                    
                    // Add strict cache control for images too, or allow revalidation
                    call.response.headers.append(io.ktor.http.HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
                    
                    val contentType = when {
                        filename.endsWith(".jpg", true) || filename.endsWith(".jpeg", true) -> ContentType.Image.JPEG
                        filename.endsWith(".png", true) -> ContentType.Image.PNG
                        filename.endsWith(".gif", true) -> ContentType.Image.GIF
                        filename.endsWith(".webp", true) -> ContentType("image", "webp")
                        else -> ContentType.Application.OctetStream
                    }
                    
                    call.respondBytes(file.readBytes(), contentType)
                }

                // お客さん用QR入口（HTMLメニュー画面）
                get("/t/{token}") {
                    val token = call.parameters["token"]
                    val remote = call.request.local.remoteHost
                    if (token == null) {
                        AppLog.http("GET /t/<null> from $remote -> 400")
                        call.respond(HttpStatusCode.BadRequest, "Missing token")
                        return@get
                    }

                    AppLog.http("GET /t/$token from $remote")
                    
                    // テーブル存在確認
                    val table = tableDao.findByToken(token)
                    if (table == null) {
                        AppLog.http("GET /t/$token from $remote -> 404 (table not found)")
                        call.respond(HttpStatusCode.NotFound, "Table not found")
                        return@get
                    }

                    AppLog.http("GET /t/$token from $remote -> 200 table=${table.tableNumber}")

                    // Fetch store settings for theme and splash
                    val store = storeDao.getStore()
                    val themeColor = store?.themeColor
                    
                    // Convert local file path to URL path
                    val splashImageUrl = store?.splashImagePath?.let { path ->
                        val file = java.io.File(path)
                        "/static/splash/${file.name}"
                    }

                    // HTMLページを返す
                    val useSimpleDebug = call.request.queryParameters["debug"] == "1"
                    if (useSimpleDebug) {
                        AppLog.http("GET /t/$token from $remote -> serve simple debug page")
                    }

                    // Force no-cache to ensure theme color updates are seen
                    call.response.headers.append(io.ktor.http.HttpHeaders.CacheControl, "no-cache, no-store, must-revalidate")
                    call.response.headers.append(io.ktor.http.HttpHeaders.Pragma, "no-cache")
                    call.response.headers.append(io.ktor.http.HttpHeaders.Expires, "0")

                    call.respondText(
                        text = buildMenuHtmlPage(
                            token = token,
                            tableName = "テーブル ${table.tableNumber}",
                            useSimpleDebug = useSimpleDebug,
                            themeColor = themeColor,
                            splashImageUrl = splashImageUrl
                        ),
                        contentType = ContentType.Text.Html
                    )
                }

                // 公開API群（JSON）
                publicRoutes(
                    context = context,
                    menuRepository = menuRepository,
                    orderRepository = orderRepository,
                    tableDao = tableDao,
                    translationRepository = translationRepository,
                    callRequestDao = callRequestDao,
                    billingRepository = billingRepository
                )
            }
        }.start(wait = false)

        AppLog.server("Server started on port=$port")
    }

    /**
     * サーバを停止
     */
    fun stop() {
        AppLog.server("Stopping server")
        engine?.stop(1000, 2000)
        engine = null
        AppLog.server("Server stopped")
    }

    /**
     * メニュー画面HTMLを生成
     * @param token テーブルトークン
     * @param themeColor テーマカラー
     * @param splashImageUrl スプラッシュ画像URL
     * @return HTML文字列
     */
    private fun buildMenuHtmlPage(
        token: String, 
        tableName: String, 
        useSimpleDebug: Boolean, 
        themeColor: String? = null,
        splashImageUrl: String? = null
    ): String {
        return if (useSimpleDebug) {
            MenuHtmlTemplate.buildSimpleDebug(
                tableToken = token,
                tableName = tableName
            )
        } else {
            MenuHtmlTemplate.build(
                tableToken = token,
                tableName = tableName,
                themeColor = themeColor,
                splashImageUrl = splashImageUrl
            )
        }
    }
}
