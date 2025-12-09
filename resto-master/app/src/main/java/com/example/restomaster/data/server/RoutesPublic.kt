package com.example.restomaster.data.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.restomaster.domain.repository.MenuRepository
import com.example.restomaster.domain.repository.OrderRepository
import com.example.restomaster.data.local.dao.TableDao
import com.example.restomaster.util.AppLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "PublicRoutes"

/**
 * お客さん向け公開API
 */
fun Routing.publicRoutes(
    context: android.content.Context,
    menuRepository: MenuRepository,
    orderRepository: OrderRepository,
    tableDao: TableDao,
    translationRepository: com.example.restomaster.data.repository.TranslationRepository,
    callRequestDao: com.example.restomaster.data.local.dao.CallRequestDao,
    billingRepository: com.example.restomaster.data.repository.BillingRepository
) {
    route("/api/public") {
        
        /**
         * GET /api/public/images/{filename}
         * 画像配信
         */
        get("/images/{filename}") {
            val filename = call.parameters["filename"]
            if (filename == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            
            // Security check: prevent directory traversal
            if (filename.contains("..") || filename.contains("/")) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val file = java.io.File(context.filesDir, filename)
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        /**
         * GET /api/public/menu?table_token={token}&lang={lang}
         * メニュー一覧取得
         */
        get("/menu") {
            val token = call.request.queryParameters["table_token"]
            val lang = call.request.queryParameters["lang"]
            val remote = call.request.local.remoteHost
            if (token == null) {
                AppLog.http("GET /api/public/menu (no token) from $remote -> 400")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "table_token is required"))
                return@get
            }

            AppLog.http("GET /api/public/menu token=$token lang=$lang from $remote -> start")
            val table = tableDao.findByToken(token)
            if (table == null) {
                AppLog.http("GET /api/public/menu token=$token from $remote -> 404")
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "table not found"))
                return@get
            }

            try {
                val menuItems = withContext(Dispatchers.IO) {
                    menuRepository.getPublicMenuForStore(table.storeId)
                }
                
                val translations = if (!lang.isNullOrBlank() && lang != "ja") {
                    withContext(Dispatchers.IO) {
                        translationRepository.getApprovedTranslationsForStore(table.storeId, lang)
                            .associateBy { it.menuId }
                    }
                } else {
                    emptyMap()
                }

                val dtoList = withContext(Dispatchers.IO) {
                    menuItems.map { item ->
                        val tr = translations[item.id]
                        val imageUrl = if (!item.imagePath.isNullOrBlank()) {
                            val file = java.io.File(item.imagePath)
                            "/api/public/images/${file.name}"
                        } else {
                            null
                        }
                        
                        PublicMenuItemDto(
                            id = item.id,
                            name = tr?.name ?: item.name,
                            description = tr?.description ?: item.description,
                            price = item.price,
                            category = item.category,
                            imageUrl = imageUrl,
                            isAiGenerated = item.isAiGenerated,
                            isAvailable = item.isAvailable,
                            allergens = menuRepository.getAllergensForItem(item.id)
                        )
                    }
                }
                
                AppLog.http("GET /api/public/menu token=$token from $remote -> 200 items=${dtoList.size}")
                call.respond(dtoList)
            } catch (e: CancellationException) {
                AppLog.http("GET /api/public/menu token=$token from $remote cancelled: ${e.message}")
                throw e
            } catch (e: Exception) {
                AppLog.error("HTTP", "GET /api/public/menu token=$token error: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        /**
         * POST /api/public/orders
         * 注文作成
         */
        post("/orders") {
            try {
                val req = call.receive<CreateOrderRequest>()
                val remote = call.request.local.remoteHost
                
                AppLog.http("POST /api/public/orders START from $remote token=${req.tableToken} people=${req.peopleCount} items=${req.items.size}")
                req.items.forEachIndexed { index, item ->
                    AppLog.http("  Item[$index]: menuItemId=${item.menuItemId}, qty=${item.quantity}, memo=${item.memo}")
                }

                val table = tableDao.findByToken(req.tableToken)
                if (table == null) {
                    AppLog.http("POST /api/public/orders token=${req.tableToken} from $remote -> 404 Table Not Found")
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "table not found"))
                    return@post
                }

                val orderId = orderRepository.createOrderFromPublic(
                    storeId = table.storeId,
                    tableId = table.id,
                    peopleCount = req.peopleCount,
                    items = req.items
                )

                AppLog.http("POST /api/public/orders SUCCESS token=${req.tableToken} from $remote -> 200 orderId=$orderId")
                
                call.respond(
                    CreateOrderResponse(
                        orderId = orderId,
                        status = "received"
                    )
                )
            } catch (e: Exception) {
                AppLog.error("HTTP", "POST /api/public/orders ERROR: ${e.message}", e)
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        /**
         * GET /api/public/tables/{token}/orders/current
         * 現在の注文状況取得
         */
        get("/tables/{token}/orders/current") {
            val token = call.parameters["token"]
            val remote = call.request.local.remoteHost
            if (token == null) {
                AppLog.http("GET /api/public/tables/<null>/orders/current from $remote -> 400")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing token"))
                return@get
            }

            try {
                val table = tableDao.findByToken(token)
                if (table == null) {
                    AppLog.http("GET /api/public/tables/$token/orders/current from $remote -> 404")
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "table not found"))
                    return@get
                }

                val status = orderRepository.getCurrentOrderStatusForTable(table.id)
                AppLog.http("GET /api/public/tables/$token/orders/current from $remote -> 200")
                call.respond(status)
            } catch (e: Exception) {
                AppLog.error("HTTP", "GET /api/public/tables/$token/orders/current error: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        /**
         * POST /api/public/tables/{token}/call
         * 店員呼び出しリクエスト
         */
        post("/tables/{token}/call") {
            val token = call.parameters["token"]
            val remote = call.request.local.remoteHost
            AppLog.http("POST /api/public/tables/$token/call START from $remote")
            
            if (token == null) {
                AppLog.http("POST /api/public/tables/<null>/call from $remote -> 400 Missing Token")
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing token"))
                return@post
            }

            try {
                val table = tableDao.findByToken(token)
                if (table == null) {
                    AppLog.http("POST /api/public/tables/$token/call from $remote -> 404 Table Not Found")
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "table not found"))
                    return@post
                }

                val callRequest = com.example.restomaster.data.local.entity.CallRequestEntity(
                    storeId = table.storeId,
                    tableId = table.id,
                    type = "call_staff",
                    status = "new",
                    createdAt = System.currentTimeMillis(),
                    handledAt = null
                )
                callRequestDao.insert(callRequest)

                AppLog.http("POST /api/public/tables/$token/call SUCCESS from $remote -> 200")
                call.respond(mapOf("status" to "success", "message" to "Staff has been notified"))
            } catch (e: Exception) {
                AppLog.error("HTTP", "POST /api/public/tables/$token/call ERROR: ${e.message}", e)
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }



        /**
         * GET /api/public/tables/{token}/status
         * テーブル状態取得 (Polling)
         */
        get("/tables/{token}/status") {
            val token = call.parameters["token"]
            if (token == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val table = tableDao.findByToken(token)
            if (table == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }
            call.respond(mapOf("status" to table.status))
        }

        /**
         * POST /api/public/tables/{token}/checkout/request
         * 会計依頼 (New)
         */
        post("/tables/{token}/checkout/request") {
            val token = call.parameters["token"]
            val remote = call.request.local.remoteHost
            if (token == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            
            try {
                val table = tableDao.findByToken(token)
                if (table == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@post
                }
                
                // Update table status
                billingRepository.requestCheckout(table.id)
                // Also create legacy CallRequest for backward compatibility/Notification
                val callRequest = com.example.restomaster.data.local.entity.CallRequestEntity(
                    storeId = table.storeId,
                    tableId = table.id,
                    type = "checkout",
                    status = "new",
                    createdAt = System.currentTimeMillis(),
                    handledAt = null
                )
                callRequestDao.insert(callRequest)

                AppLog.http("POST /checkout/request table=${table.tableNumber} -> requested")
                call.respond(mapOf("status" to "CHECKOUT_REQUESTED"))
            } catch (e: Exception) {
                AppLog.error("HTTP", "Checkout Request Error", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }

        /**
         * POST /api/public/tables/{token}/paypay/start
         * PayPay決済開始
         */
        post("/tables/{token}/paypay/start") {
            val token = call.parameters["token"]
            if (token == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            
            try {
                val table = tableDao.findByToken(token)
                if (table == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@post
                }

                val res = billingRepository.startPayPayPayment(table.id)
                call.respond(res)
            } catch (e: Exception) {
                AppLog.error("HTTP", "PayPay Start Error", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }


        /**
         * POST /api/public/tables/{token}/checkout/prepare
         * Stripe決済準備
         */
        post("/tables/{token}/checkout/prepare") {
            val token = call.parameters["token"]
            val remote = call.request.local.remoteHost
            
            if (token == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Missing token"))
                return@post
            }

            try {
                val table = tableDao.findByToken(token)
                if (table == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "table not found"))
                    return@post
                }
                
                // Request Body: { "method": "stripe" }
                // For simplicity, we assume method is stripe if this endpoint is called, 
                // or we can parse body. Let's assume default stripe for now or parse map.
                val body = call.receive<Map<String, String>>()
                val method = body["method"] ?: "stripe"
                
                // Construct return URL (Web App URL)
                // In this architecture, the Web App is served by this same server or a dev server.
                // If served by this server, it's http://<host>:8080/...
                // The user's request said: return_base_url: "https://restomaster.local" or host IP.
                // We can use the request host.
                val scheme = call.request.local.scheme
                val host = call.request.local.serverHost
                val port = call.request.local.serverPort
                val baseUrl = "$scheme://$host:$port"
                
                val checkoutUrl = billingRepository.prepareCheckout(table.id, method, baseUrl)
                
                call.respond(mapOf("checkoutUrl" to checkoutUrl))
                
            } catch (e: Exception) {
                AppLog.error("HTTP", "POST /checkout/prepare ERROR: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.message))
            }
        }
    }
    
    // Global route for Stripe Success (not under /api/public/tables/{token} necessarily, but accessible)
    route("/payments/stripe") {
        get("/success") {
            val sessionId = call.request.queryParameters["session_id"]
            if (sessionId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing session_id")
                return@get
            }
            
            try {
                val success = billingRepository.verifyStripePayment(sessionId)
                if (success) {
                    // Redirect to a success page or show success HTML
                    call.respondText("<h1>Payment Successful!</h1><p>Thank you for your payment.</p>", ContentType.Text.Html)
                } else {
                    call.respondText("<h1>Payment Verification Failed</h1>", ContentType.Text.Html)
                }
            } catch (e: Exception) {
                AppLog.error("HTTP", "GET /payments/stripe/success ERROR: ${e.message}", e)
                call.respond(HttpStatusCode.InternalServerError, "Error verifying payment")
            }
        }
        
        get("/cancel") {
            call.respondText("<h1>Payment Cancelled</h1>", ContentType.Text.Html)
        }
    }
}
