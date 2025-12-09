package com.example.restomaster.data.server

/**
 * お客さん用メニュー画面のHTMLテンプレート
 * 入店〜退店までを意識したリッチUI（人数入力・注文済み合計・呼出/会計・カート確認）
 */
object MenuHtmlTemplate {

    fun build(tableToken: String, tableName: String, themeColor: String? = null, splashImageUrl: String? = null): String {
        val primaryColor = if (!themeColor.isNullOrBlank()) themeColor else "#D35400"
        val splashImageTag = splashImageUrl?.let {
            """<img src="$it" alt="" class="branding-logo">"""
        }
        val brandingVisual = splashImageTag ?: """<div class="splash-logo">ようこそ</div>"""
        val guestSplashVisual = splashImageTag ?: ""
        return """
<!doctype html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>テーブルオーダー</title>
  <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" />
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; -webkit-tap-highlight-color: transparent; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", "Yu Gothic", sans-serif; }
    body { background-color: #F5F5F7; color: #2C3E50; padding-bottom: 140px; overflow-x: hidden; }
    button { cursor: pointer; outline: none; border: none; touch-action: manipulation; }
    :root {
      --primary: $primaryColor;
      --accent: #F39C12;
      --text: #2C3E50;
      --bg-white: #FFFFFF;
      --bg-gray: #ECF0F1;
      --danger: #E74C3C;
    }
    /* Order Status Card CSS removed */
    .order-history { border-top: 1px dashed #E5E7E9; padding-top: 8px; }
    .history-title { font-weight: 800; font-size: 14px; margin-bottom: 6px; }
    .history-body { display: flex; flex-direction: column; gap: 10px; }
    .history-empty { color: #95A5A6; font-size: 13px; padding: 6px 0; }
    .history-order { background: #FDFDFD; border: 1px solid #F0F0F0; border-radius: 12px; padding: 12px; }
    .history-order-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
    .pill { display: inline-block; padding: 4px 8px; border-radius: 999px; font-size: 11px; font-weight: 800; }
    .pill-open { background: #EAF2FF; color: #2C3E50; }
    .pill-checkout_requested { background: #FFF4E5; color: #D35400; }
    .pill-closed { background: #ECF0F1; color: #7F8C8D; }
    .history-item { display: flex; justify-content: space-between; gap: 10px; align-items: center; padding: 6px 0; border-bottom: 1px dashed #F0F0F0; }
    .history-item:last-child { border-bottom: none; }
    .history-name { font-weight: 700; font-size: 13px; }
    .history-meta { font-size: 12px; color: #7F8C8D; }
    .history-price { font-weight: 800; }
    .item-pill { margin-left: 6px; padding: 2px 6px; background: #EEF8F2; color: #2E8B57; border-radius: 8px; font-size: 10px; font-weight: 800; }
    .total-inline { display: flex; justify-content: space-between; font-weight: 800; font-size: 13px; margin-top: 6px; }
    /* Branding Splash (initial) */
    #branding-splash { position: fixed; inset: 0; background: var(--bg-white); z-index: 10000; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 24px; transition: opacity 0.5s; cursor: pointer; }
    #branding-splash.fade-out { opacity: 0; pointer-events: none; }
    .branding-logo { max-width: 80%; max-height: 180px; object-fit: contain; margin-bottom: 30px; }
    .branding-text { font-size: 28px; font-weight: 800; color: var(--primary); margin-bottom: 10px; }
    .branding-hint { font-size: 14px; color: #95A5A6; animation: pulse 2s infinite; }
    @keyframes pulse { 0%, 100% { opacity: 0.5; } 50% { opacity: 1; } }
    /* Guest Selection Splash */
    #splash-screen { position: fixed; inset: 0; background: var(--bg-white); z-index: 9999; display: none; flex-direction: column; align-items: center; justify-content: center; padding: 24px; transition: opacity 0.4s; }
    #splash-screen.visible { display: flex; }
    #splash-screen.fade-out { opacity: 0; pointer-events: none; }
    .splash-logo { font-size: 22px; font-weight: 800; margin-bottom: 6px; }
    .splash-sub { font-size: 14px; color: #7F8C8D; margin-bottom: 28px; text-align: center; }
    .guest-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; width: 100%; max-width: 420px; }
    .guest-btn { background: var(--bg-white); border: 2px solid var(--bg-gray); padding: 18px 0; border-radius: 12px; font-size: 18px; font-weight: 700; color: var(--text); box-shadow: 0 4px 10px rgba(0,0,0,0.05); transition: 0.2s; }
    .guest-btn:active { background: var(--primary); color: #fff; border-color: var(--primary); transform: scale(0.96); }
    /* Header / Service */
    header { background: var(--bg-white); position: sticky; top: 0; z-index: 100; box-shadow: 0 2px 5px rgba(0,0,0,0.05); }
    .top-bar { display: flex; justify-content: space-between; align-items: center; padding: 12px 16px; background: var(--primary); color: #fff; }
    .table-num { font-size: 13px; background: rgba(255,255,255,0.2); color: #fff; padding: 4px 8px; border-radius: 4px; font-weight: 700; border: 1px solid rgba(255,255,255,0.4); }
    .lang-switch { display: flex; gap: 8px; }
    .lang-btn { font-size: 13px; font-weight: 700; color: rgba(255,255,255,0.7); background: none; }
    .lang-btn.active { color: #fff; text-decoration: underline; }
    .service-bar { display: flex; gap: 12px; padding: 12px 16px; background: var(--bg-white); overflow-x: auto; }
    .svc-btn { flex: 1; display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 10px; border-radius: 8px; background: #F8F9FA; color: var(--text); font-size: 12px; font-weight: 700; gap: 4px; border: 1px solid #E5E7E9; min-width: 100px; }
    .svc-btn:active { background: #E8E8E8; }
    .svc-icon { width: 20px; height: 20px; }
    /* Category */
    .category-nav { display: flex; overflow-x: auto; padding: 8px 16px 16px; gap: 8px; background: var(--bg-white); }
    .cat-pill { padding: 8px 16px; border-radius: 20px; font-size: 13px; font-weight: 700; background: var(--bg-gray); color: #7F8C8D; white-space: nowrap; }
    .cat-pill.active { background: var(--text); color: #fff; }
    /* Menu grid */
    .menu-container { padding: 16px; display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 16px; }
    .menu-item { background: var(--bg-white); border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); position: relative; min-height: 220px; display: flex; flex-direction: column; }
    .item-img { width: 100%; height: 140px; object-fit: cover; background: #eee; }
    .item-info { padding: 12px; flex: 1; display: flex; flex-direction: column; }
    .item-name { font-weight: 700; font-size: 14px; line-height: 1.3; margin-bottom: 4px; flex: 1; }
    .item-price { color: var(--primary); font-weight: 800; font-size: 15px; }
    .add-badge { position: absolute; bottom: 12px; right: 12px; background: var(--text); color: #fff; width: 32px; height: 32px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-weight: bold; box-shadow: 0 4px 6px rgba(0,0,0,0.2); }
    /* Footer cart */
    .sticky-footer { position: fixed; bottom: 20px; left: 16px; right: 16px; background: var(--text); color: #fff; border-radius: 50px; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 8px 20px rgba(44,62,80,0.4); z-index: 500; transform: translateY(150%); transition: transform 0.25s cubic-bezier(0.175, 0.885, 0.32, 1.275); }
    .sticky-footer.visible { transform: translateY(0); }
    .cart-summary { display: flex; align-items: center; gap: 12px; }
    .cart-qty { background: var(--danger); padding: 2px 8px; border-radius: 10px; font-size: 12px; font-weight: bold; }
    .cart-price { font-size: 17px; font-weight: 800; }
    /* Modals */
    .modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.6); z-index: 1000; opacity: 0; pointer-events: none; transition: 0.3s; display: flex; align-items: flex-end; justify-content: center; }
    .modal-mask.active { opacity: 1; pointer-events: auto; }
    .sheet { background: #fff; width: 100%; max-width: 640px; max-height: 90vh; border-radius: 20px 20px 0 0; padding: 24px; transform: translateY(100%); transition: transform 0.3s; display: flex; flex-direction: column; }
    .modal-mask.active .sheet { transform: translateY(0); }
    .detail-img { width: 100%; height: 200px; object-fit: cover; border-radius: 12px; margin-bottom: 16px; }
    .detail-title { font-size: 20px; font-weight: 800; margin-bottom: 8px; }
    .detail-desc { font-size: 14px; color: #7F8C8D; line-height: 1.6; margin-bottom: 16px; }
    .allergy-tags { display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 16px; }
    .tag { font-size: 11px; background: #FFF3CD; color: #856404; padding: 4px 8px; border-radius: 4px; font-weight: bold; }
    .stepper { display: flex; justify-content: space-between; align-items: center; background: #F5F5F7; padding: 10px 20px; border-radius: 12px; margin-bottom: 16px; }
    .step-btn { font-size: 22px; width: 40px; color: var(--text); background: none; }
    .step-val { font-size: 18px; font-weight: 800; }
    .primary-btn { width: 100%; background: var(--primary); color: #fff; padding: 16px; border-radius: 12px; font-weight: 700; font-size: 16px; box-shadow: 0 4px 12px rgba(211,84,0,0.3); }
    .cart-items-box { flex: 1; overflow-y: auto; margin: 16px 0; min-height: 100px; }
    .cart-item-row { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px dashed #eee; align-items: center; }
    .cart-empty-btn { margin-top: 12px; background: transparent; color: var(--danger); font-size: 13px; padding: 12px; border: 1px solid #eee; border-radius: 12px; width: 100%; }
    .order-confirm-area { background: #FAFAFA; padding: 16px; border-radius: 12px; margin-bottom: 12px; }
    .total-row { display: flex; justify-content: space-between; font-weight: 800; font-size: 16px; margin-top: 8px; }
    /* Sold Out */
    .menu-item.sold-out .item-img { opacity: 0.6; filter: grayscale(100%); }
    .menu-item.sold-out .item-name { color: #95A5A6; }
    .menu-item.sold-out .item-price { color: #95A5A6; }
    .sold-out-overlay { position: absolute; top: 0; left: 0; right: 0; height: 140px; display: flex; align-items: center; justify-content: center; background: rgba(0,0,0,0.3); color: #fff; font-weight: 800; font-size: 18px; letter-spacing: 1px; z-index: 1; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }
  </style>
</head>
<body>
  <!-- Branding Splash: 最初に表示される画面 -->
  <div id="branding-splash" onclick="showGuestSelection()">
    $brandingVisual
    <div class="branding-hint">タップして開始</div>
  </div>

  <!-- Guest Selection Splash: ブランディング後に表示 -->
  <div id="splash-screen">
    $guestSplashVisual
    <div class="splash-logo">ようこそ</div>
    <div class="splash-sub" id="lbl-how-many">何名様でご利用ですか？</div>
    <div class="guest-grid">
      <button class="guest-btn" onclick="setGuests(1)">1名</button>
      <button class="guest-btn" onclick="setGuests(2)">2名</button>
      <button class="guest-btn" onclick="setGuests(3)">3名</button>
      <button class="guest-btn" onclick="setGuests(4)">4名</button>
      <button class="guest-btn" onclick="setGuests(5)">5名</button>
      <button class="guest-btn" onclick="setGuests(6)">6名+</button>
    </div>
  </div>

  <header>
    <div class="top-bar">
      <div class="table-num">$tableName</div>
      <div class="lang-switch">
        <button class="lang-btn active" onclick="switchLang('ja', event)">JP</button>
        <button class="lang-btn" onclick="switchLang('en', event)">EN</button>
      </div>
    </div>
    <div class="service-bar">
      <button class="svc-btn" onclick="callStaff()">
        <svg class="svc-icon" viewBox="0 0 24 24"><path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5S10.5 3.17 10.5 4v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2z"/></svg>
        <span id="lbl-call">店員を呼ぶ</span>
      </button>
      <button class="svc-btn" onclick="requestCheckout()">
        <svg class="svc-icon" viewBox="0 0 24 24"><path d="M3 6h18v12H3V6zm2 2v8h14V8H5zm4 2h6v2H9v-2z"/></svg>
        <span id="lbl-checkout">お会計</span>
      </button>
      <button class="svc-btn" onclick="openHistoryPage()">
        <svg class="svc-icon" viewBox="0 0 24 24"><path d="M13 3a9 9 0 0 0-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42A8.954 8.954 0 0 0 13 21a9 9 0 0 0 0-18zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/></svg>
        <span id="lbl-history">注文履歴</span>
      </button>
    </div>
    <nav class="category-nav" id="category-nav"></nav>
  </header>

  <main class="menu-container" id="menu-grid"></main>

  <!-- フッターカート -->
  <div class="sticky-footer" id="cart-footer" onclick="openCartModal()">
    <div class="cart-summary">
      <span class="cart-qty" id="footer-qty">0</span>
      <span class="cart-price" id="footer-total">¥0</span>
    </div>
    <div style="font-weight:bold; font-size:14px;" id="lbl-view-order">注文を確認する →</div>
  </div>

  <!-- 詳細モーダル -->
  <div class="modal-mask" id="detail-modal" onclick="closeDetailModal(event)">
    <div class="sheet" onclick="event.stopPropagation()">
      <img src="" class="detail-img" id="dm-img" alt="menu image">
      <h2 class="detail-title" id="dm-title">Title</h2>
      <div class="allergy-tags" id="dm-allergy"></div>
      <p class="detail-desc" id="dm-desc">Desc</p>
      <p id="dm-disclaimer" style="display:none; font-size:11px; color:#7F8C8D; margin-bottom:12px;">※画像はイメージです。実際の盛り付けと異なる場合があります</p>
      <div style="margin-top:auto;">
        <div class="stepper">
          <button class="step-btn" onclick="changeQty(-1)">−</button>
          <span class="step-val" id="dm-qty">1</span>
          <button class="step-btn" onclick="changeQty(1)">＋</button>
        </div>
        <button class="primary-btn" onclick="addToCart()" id="btn-add-cart">カートに追加</button>
      </div>
    </div>
  </div>

  <!-- カートモーダル -->
  <div class="modal-mask" id="cart-modal" onclick="closeCartModal(event)">
    <div class="sheet" onclick="event.stopPropagation()">
      <h2 class="detail-title" id="lbl-cart-title">現在の注文</h2>
      <div class="cart-items-box" id="cart-list"></div>
      <div class="order-confirm-area">
        <div class="total-row">
          <span id="lbl-total">合計</span>
          <span id="cart-final-total">¥0</span>
        </div>
      </div>
      <button class="primary-btn" onclick="sendOrder()" id="btn-order">注文を送信する</button>
      <button class="cart-empty-btn" onclick="emptyCart()" id="btn-empty">カートを空にする</button>
    </div>
  </div>

  <!-- 注文履歴ページモーダル -->
  <div class="modal-mask" id="history-modal" onclick="closeHistoryPage(event)">
    <div class="sheet" onclick="event.stopPropagation()" style="max-height:95vh;">
      <div style="display:flex; align-items:center; margin-bottom:16px;">
        <button onclick="closeHistoryPage()" style="background:none; border:none; padding:8px; margin:-8px 8px -8px -8px; cursor:pointer;">
          <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor"><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>
        </button>
        <h2 class="detail-title" id="lbl-history-page-title" style="margin:0;">注文履歴</h2>
      </div>
      
      <div style="background:#F8F9FA; padding:16px; border-radius:12px; margin-bottom:16px;">
        <div style="display:flex; justify-content:space-between; margin-bottom:12px;">
          <div>
            <div class="status-label" id="lbl-history-total">合計金額</div>
            <div class="status-amount" id="history-total-amount">¥0</div>
          </div>
          <div style="text-align:right;">
            <div class="status-label" id="lbl-history-per-person">おひとりあたり</div>
            <div class="per-person-amount" id="history-per-person-amount">--</div>
          </div>
        </div>
        <div style="font-size:12px; color:#7F8C8D; text-align:center;" id="history-people-count"></div>
      </div>

      <div class="cart-items-box" style="flex:1; overflow-y:auto;">
        <div id="history-orders-list"></div>
      </div>
    </div>
  </div>

  <!-- 支払い方法選択モーダル -->
  <div class="modal-mask" id="payment-modal" onclick="closePaymentModal(event)">
    <div class="sheet" onclick="event.stopPropagation()">
      <h2 class="detail-title" id="lbl-payment-title">お支払い方法</h2>
      <p class="detail-desc" id="lbl-payment-desc">お支払い方法を選択してください。</p>
      
      <div style="background:#F8F9FA; padding:16px; border-radius:12px; margin-bottom:24px;">
        <div style="display:flex; justify-content:space-between; align-items:center;">
          <span style="font-weight:bold;" id="lbl-pay-total">合計</span>
          <span class="status-amount" id="pay-total-amount" style="font-size:20px;">¥0</span>
        </div>
      </div>

      <div style="display:flex; flex-direction:column; gap:12px;">
        <button class="primary-btn" onclick="payOnline()" style="background:#6772E5;">
          <span id="btn-pay-online">カードで支払う (Stripe)</span>
        </button>
        <button class="primary-btn" onclick="payPayPay()" style="background:#FF0033;">
          <span id="btn-pay-paypay">📱 PayPay で支払う</span>
        </button>
        <button class="primary-btn" onclick="payRegister()" style="background:#95A5A6;">
          <span id="btn-pay-register">レジで支払う</span>
        </button>
      </div>
      <button class="cart-empty-btn" onclick="closePaymentModal()" style="margin-top:16px; border:none;">キャンセル</button>
    </div>
  </div>

  <script>
    const TABLE_TOKEN = "$tableToken";
    const DEFAULT_IMAGE = "/static/images/menu/default.png";
    const langData = {
      ja: { splash: "何名様でご利用ですか？", call: "店員を呼ぶ", checkout: "お会計", history: "注文履歴", viewOrder: "注文を確認する →", addCart: "カートに追加", cartTitle: "現在の注文", total: "合計", sendOrder: "注文を送信する", emptyCart: "カートを空にする", alertCall: "店員をお呼びしました。少々お待ちください。", alertBill: "お会計をお持ちします。テーブルにてお待ちください。", alertEmpty: "カートを空にしました。", alertSent: "注文を承りました！", orderHistoryEmpty: "まだご注文はありません。メニューから商品をお選びください。", orderedTotalLabel: "ご注文済み合計", orderHistoryTitle: "注文履歴", perPersonLabel: "おひとりあたり", splitLabel: "割り勘人数", splitHint: "人数を変えると金額に反映されます", cartEmpty: "カートは空です", perPersonZero: "人数を設定してください", historyPageTitle: "注文履歴", historyTotalLabel: "合計金額", historyPerPersonLabel: "おひとりあたり", historyPeopleLabel: "名様", payTitle: "お支払い方法", payDesc: "お支払い方法を選択してください。", payOnline: "カードで支払う (Stripe)", payRegister: "レジで支払う" },
      en: { splash: "How many guests?", call: "Call Staff", checkout: "Check Bill", history: "Order History", viewOrder: "View Order →", addCart: "Add to Cart", cartTitle: "Your Order", total: "Total", sendOrder: "Place Order", emptyCart: "Empty Cart", alertCall: "Staff has been notified.", alertBill: "We will bring the bill shortly.", alertEmpty: "Cart cleared.", alertSent: "Order Sent!", orderHistoryEmpty: "No orders yet. Please add items.", orderedTotalLabel: "Ordered Total", orderHistoryTitle: "Order History", perPersonLabel: "Per Person", splitLabel: "Split People", splitHint: "Adjust the headcount to update amount", cartEmpty: "Cart is empty", perPersonZero: "Set headcount first", historyPageTitle: "Order History", historyTotalLabel: "Total Amount", historyPerPersonLabel: "Per Person", historyPeopleLabel: "people", payTitle: "Payment Method", payDesc: "Choose payment method.", payOnline: "Pay Online (Stripe)", payRegister: "Pay at Register" }
    };
    
    let currentLang = "ja";
    let guestCount = 0;
    let menuItems = [];
    let cart = [];
    let tempItem = null;
    let tempQty = 1;
    let orderedTotal = 0;
    let splitCount = 0;

    function formatCurrency(v) { return `¥${ '$' }{(v || 0).toLocaleString()}`; }
    
    function showGuestSelection() {
      const branding = document.getElementById('branding-splash');
      const guest = document.getElementById('splash-screen');
      if (branding) {
        branding.classList.add('fade-out');
        setTimeout(() => { branding.style.display = 'none'; }, 400);
      }
      if (guest) {
        guest.style.display = 'flex';
        requestAnimationFrame(() => guest.classList.add('visible'));
      }
    }

    function setGuests(count) {
      guestCount = count;
      splitCount = count;
      const guest = document.getElementById('splash-screen');
      if (guest) {
        guest.classList.add('fade-out');
        setTimeout(() => { guest.style.display = 'none'; }, 400);
      }
      loadOrderSummary();
    }

    function setText(id, text) {
      const el = document.getElementById(id);
      if (el) el.innerText = text;
    }

    function switchLang(lang, evt) {
      currentLang = lang;
      document.querySelectorAll('.lang-btn').forEach(b => b.classList.remove('active'));
      evt.target.classList.add('active');
      updateTexts();
      renderMenu();
      if (document.getElementById('cart-modal').classList.contains('active')) renderCart();
    }

    function updateTexts() {
      const t = langData[currentLang];
      setText('lbl-how-many', t.splash);
      setText('lbl-call', t.call);
      setText('lbl-checkout', t.checkout);
      setText('lbl-history', t.history);
      setText('lbl-view-order', t.viewOrder);
      setText('btn-add-cart', t.addCart);
      setText('lbl-cart-title', t.cartTitle);
      setText('lbl-total', t.total);
      setText('btn-order', t.sendOrder);
      setText('btn-empty', t.emptyCart);
      
      // These may not exist if order-status-card is removed
      // setText('lbl-ordered-total', t.orderedTotalLabel); // Removed
      setText('lbl-history-title', t.orderHistoryTitle);
      // setText('lbl-per-person', t.perPersonLabel); // Removed
      // setText('lbl-split-label', t.splitLabel); // Removed
      // setText('lbl-split-hint', t.splitHint); // Removed

      if (document.getElementById('lbl-history-page-title')) {
        setText('lbl-history-page-title', t.historyPageTitle);
        setText('lbl-history-total', t.historyTotalLabel);
        setText('lbl-history-per-person', t.historyPerPersonLabel);
      }
      if (document.getElementById('lbl-payment-title')) {
        setText('lbl-payment-title', t.payTitle);
        setText('lbl-payment-desc', t.payDesc);
        setText('btn-pay-online', t.payOnline);
        setText('btn-pay-register', t.payRegister);
      }
      // renderSplitAmount(); // Removed
    }

    async function loadMenu() {
      try {
        const url = new URL(window.location.origin + "/api/public/menu");
        url.searchParams.set("table_token", TABLE_TOKEN);
        url.searchParams.set("lang", currentLang);
        const res = await fetch(url.toString());
        if (!res.ok) { console.error("menu status", res.status); return; }
        menuItems = await res.json();
        buildCategories();
        renderMenu();
      } catch (e) { console.error("menu error", e); }
    }

    function buildCategories() {
      const nav = document.getElementById('category-nav');
      const cats = new Set(["ALL"]);
      menuItems.forEach(i => { if (i.category) cats.add(i.category); });
      nav.innerHTML = "";
      cats.forEach(cat => {
        const div = document.createElement("div");
        div.className = "cat-pill" + (cat === currentCategory ? " active" : "");
        div.textContent = cat === "ALL" ? "All" : cat;
        div.onclick = () => { currentCategory = cat; buildCategories(); renderMenu(); };
        nav.appendChild(div);
      });
    }
    let currentCategory = "ALL";

    function renderMenu() {
      const grid = document.getElementById("menu-grid");
      const filtered = menuItems.filter(i => currentCategory === "ALL" || (i.category || "") === currentCategory);
      grid.innerHTML = filtered.map(item => {
        const name = item.name || "";
        const img = item.image_url || item.imageUrl || DEFAULT_IMAGE;
        const isSoldOut = item.isAvailable === false;
        const soldOutClass = isSoldOut ? " sold-out" : "";
        const badgeHtml = isSoldOut ? "" : '<div class="add-badge">＋</div>';
        const overlayHtml = isSoldOut ? '<div class="sold-out-overlay">SOLD OUT</div>' : "";
        
        return `
          <div class="menu-item${'$'}{soldOutClass}" onclick="openDetail(${'$'}{item.id})">
            <img src="${'$'}{img}" class="item-img" alt="">
            ${'$'}{overlayHtml}
            <div class="item-info">
              <div class="item-name">${'$'}{name}</div>
              <div class="item-price">¥${'$'}{(item.price || 0).toLocaleString()}</div>
            </div>
            ${'$'}{badgeHtml}
          </div>
        `;
      }).join("");
    }

    function openDetail(id) {
      tempItem = menuItems.find(p => p.id === id);
      if (!tempItem) return;
      tempQty = 1;
      document.getElementById('dm-img').src = tempItem.image_url || tempItem.imageUrl || DEFAULT_IMAGE;
      document.getElementById('dm-title').innerText = tempItem.name || "";
      document.getElementById('dm-desc').innerText = tempItem.description || "";
      document.getElementById('dm-qty').innerText = tempQty;
      const alg = document.getElementById('dm-allergy');
      const allergens = tempItem.allergens || [];
      alg.innerHTML = allergens.length ? allergens.map(a => `<span class="tag">${ '$' }{a}</span>`).join('') : "";
      
      const disclaimer = document.getElementById('dm-disclaimer');
      if (tempItem.isAiGenerated) {
        disclaimer.style.display = 'block';
      } else {
        disclaimer.style.display = 'none';
      }
      
      const btnAdd = document.getElementById('btn-add-cart');
      if (tempItem.isAvailable === false) {
        btnAdd.disabled = true;
        btnAdd.innerText = "SOLD OUT";
        btnAdd.style.background = "#95A5A6";
        btnAdd.style.boxShadow = "none";
      } else {
        btnAdd.disabled = false;
        btnAdd.innerText = langData[currentLang].addCart;
        btnAdd.style.background = "var(--primary)";
        btnAdd.style.boxShadow = "0 4px 12px rgba(211,84,0,0.3)";
      }
      
      document.getElementById('detail-modal').classList.add('active');
    }

    function closeDetailModal(e) {
      if (e.target.id === 'detail-modal') document.getElementById('detail-modal').classList.remove('active');
    }

    function changeQty(delta) {
      if (tempQty + delta >= 1) {
        tempQty += delta;
        document.getElementById('dm-qty').innerText = tempQty;
      }
    }

    function addToCart() {
      if (!tempItem) return;
      const existing = cart.find(i => i.id === tempItem.id);
      if (existing) existing.qty += tempQty;
      else cart.push({ id: tempItem.id, name: tempItem.name, price: tempItem.price || 0, qty: tempQty });
      document.getElementById('detail-modal').classList.remove('active');
      updateFooter();
    }

    function getCartTotal() {
      return cart.reduce((s,i) => s + (i.price * i.qty), 0);
    }

    function updateFooter() {
      const totalQty = cart.reduce((s,i) => s + i.qty, 0);
      const totalPrice = getCartTotal();
      document.getElementById('footer-qty').innerText = totalQty;
      document.getElementById('footer-total').innerText = `¥${ '$' }{totalPrice.toLocaleString()}`;
      const footer = document.getElementById('cart-footer');
      if (totalQty > 0) footer.classList.add('visible'); else footer.classList.remove('visible');
      // renderSplitAmount(); call removed
    }

    function openCartModal() {
      renderCart();
      document.getElementById('cart-modal').classList.add('active');
    }
    function closeCartModal(e) {
      if (e.target.id === 'cart-modal') document.getElementById('cart-modal').classList.remove('active');
    }

    function renderCart() {
      const list = document.getElementById('cart-list');
      let total = 0;
      if (cart.length === 0) {
        list.innerHTML = `<div style="text-align:center; color:#999; padding:20px;">${'$'}{langData[currentLang].cartEmpty}</div>`;
      } else {
        list.innerHTML = cart.map(item => {
          total += item.price * item.qty;
          return `
            <div class="cart-item-row">
              <div>
                <div style="font-weight:bold;">${ '$' }{item.name}</div>
                <div style="font-size:12px; color:#666; display:flex; align-items:center; gap:8px;">
                  <span>¥${ '$' }{item.price.toLocaleString()}</span>
                  <div style="display:flex; align-items:center; gap:6px;">
                    <button style="width:28px; height:28px; border:1px solid #ddd; background:#fff; border-radius:6px;" onclick="changeCartQty(${ '$' }{item.id}, -1)">-</button>
                    <span style="min-width:20px; text-align:center;">${ '$' }{item.qty}</span>
                    <button style="width:28px; height:28px; border:1px solid #ddd; background:#fff; border-radius:6px;" onclick="changeCartQty(${ '$' }{item.id}, 1)">+</button>
                  </div>
                </div>
              </div>
              <div style="font-weight:bold;">¥${ '$' }{(item.price * item.qty).toLocaleString()}</div>
            </div>
          `;
        }).join('');
      }
      document.getElementById('cart-final-total').innerText = `¥${ '$' }{total.toLocaleString()}`;
    }

    function changeCartQty(id, delta) {
      const item = cart.find(i => i.id === id);
      if (!item) return;
      item.qty += delta;
      if (item.qty <= 0) {
        cart = cart.filter(i => i.id !== id);
      }
      updateFooter();
      renderCart();
    }

    function emptyCart() {
      if (!confirm("カート内の商品をすべて削除します。よろしいですか？")) return;
      cart = [];
      updateFooter();
      document.getElementById('cart-modal').classList.remove('active');
      alert(langData[currentLang].alertEmpty);
    }

    async function sendOrder() {
      if (!cart.length) return;
      try {
        const body = {
          tableToken: TABLE_TOKEN,
          peopleCount: guestCount > 0 ? guestCount : null,
          items: cart.map(i => ({ menuItemId: i.id, quantity: i.qty }))
        };
        const res = await fetch("/api/public/orders", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(body)
        });
        if (!res.ok) { alert("注文に失敗しました"); return; }
        alert(langData[currentLang].alertSent);
        cart = [];
        updateFooter();
        document.getElementById('cart-modal').classList.remove('active');
        loadOrderSummary();
      } catch (e) { alert("注文の送信中にエラーが発生しました: " + e); }
    }

    async function callStaff() {
      if (!confirm("店員をお呼びしますか？")) return;
      try { await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/call`, { method: "POST" }); alert(langData[currentLang].alertCall); }
      catch (e) { alert("呼び出しに失敗しました: " + e); }
    }

    async function requestCheckout() {
      // Show Payment Modal instead of direct confirm
      const totalEl = document.getElementById('pay-total-amount');
      if (totalEl) totalEl.innerText = formatCurrency(orderedTotal);
      
      document.getElementById('payment-modal').classList.add('active');
    }

    function closePaymentModal(e) {
      if (!e || e.target.id === 'payment-modal' || !e.target.id) {
        document.getElementById('payment-modal').classList.remove('active');
      }
    }

    async function payOnline() {
      // Call /checkout/prepare
      try {
        const res = await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/checkout/prepare`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ method: "stripe" })
        });
        if (!res.ok) { alert("エラーが発生しました"); return; }
        const data = await res.json();
        if (data.checkoutUrl) {
          window.location.href = data.checkoutUrl;
        } else {
          alert("支払いURLの取得に失敗しました");
        }
      } catch (e) {
        alert("通信エラー: " + e);
      }
    }

    async function payRegister() {
      // Call legacy /checkout
      const headcount = splitCount || guestCount || 1;
      try { 
        await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/checkout`, { 
          method: "POST", 
          headers: { "Content-Type": "application/json" }, 
          body: JSON.stringify({ peopleCount: headcount || null }) 
        }); 
        alert(langData[currentLang].alertBill); 
        closePaymentModal();
      }
      catch (e) { alert("お会計の依頼に失敗しました: " + e); }
    }

    async function payPayPay() {
      // PayPay: Show instruction and notify staff
      const headcount = splitCount || guestCount || 1;
      try {
        // Notify staff that customer wants to pay with PayPay
        await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/checkout`, { 
          method: "POST", 
          headers: { "Content-Type": "application/json" }, 
          body: JSON.stringify({ peopleCount: headcount || null, paymentMethod: "PAYPAY" }) 
        });
        closePaymentModal();
        // Show PayPay instruction modal/alert
        alert("PayPayでお支払い\\n\\n店舗のPayPay QRコードを読み取ってお支払いください。\\nスタッフがお伺いいたします。");
      } catch (e) { 
        alert("お会計の依頼に失敗しました: " + e); 
      }
    }

    async function loadOrderSummary() {
      try {
        const res = await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/orders/current`);
        if (!res.ok) return;
        const data = await res.json();
        orderedTotal = data.totalAmount || 0;
        if (!splitCount) {
          if (guestCount > 0) splitCount = guestCount;
          else if (data.perPersonAmount && data.totalAmount) {
            splitCount = Math.max(1, Math.round(data.totalAmount / data.perPersonAmount));
          }
        }
        renderOrderHistory(data.orders || []);
        // updateOrderedTotalHeader(); // Removed
      } catch (e) { console.log("order summary error", e); }
    }

    // Functions updateOrderedTotalHeader, renderSplitAmount, adjustSplit, setSplitFromInput removed

    function formatOrderStatus(status) {
      const jp = { open: "受付済み", checkout_requested: "会計待ち", closed: "会計済み" };
      const en = { open: "Received", checkout_requested: "Checkout", closed: "Closed" };
      const map = currentLang === "ja" ? jp : en;
      return map[status] || status;
    }

    function formatItemStatus(status) {
      const jp = { received: "受付", in_progress: "調理中", served: "提供済み" };
      const en = { received: "Received", in_progress: "Cooking", served: "Served" };
      const map = currentLang === "ja" ? jp : en;
      return map[status] || status;
    }

    function renderOrderHistory(orders) {
      const section = document.getElementById("history-orders-list");
      if (!orders || !orders.length) {
        section.innerHTML = `<div class="history-empty">${'$'}{langData[currentLang].orderHistoryEmpty}</div>`;
        return;
      }
      section.innerHTML = orders.map(order => {
        const total = (order.items || []).reduce((s, i) => s + (i.quantity * i.unitPrice), 0);
        const items = (order.items || []).map(item => `
          <div class="history-item">
            <div>
              <div class="history-name">${'$'}{item.name}</div>
              <div class="history-meta">¥${'$'}{item.unitPrice.toLocaleString()} × ${'$'}{item.quantity}<span class="item-pill">${'$'}{formatItemStatus(item.status)}</span></div>
            </div>
            <div class="history-price">¥${'$'}{(item.unitPrice * item.quantity).toLocaleString()}</div>
          </div>
        `).join("");
        return `
          <div class="history-order">
            <div class="history-order-head">
              <div>#${'$'}{order.orderId}</div>
              <span class="pill pill-${'$'}{order.status}">${'$'}{formatOrderStatus(order.status)}</span>
            </div>
            ${'$'}{items}
            <div class="total-inline"><span>${'$'}{langData[currentLang].total}</span><span>¥${'$'}{total.toLocaleString()}</span></div>
          </div>
        `;
      }).join("");
    }

    function openHistoryPage() {
      const modal = document.getElementById('history-modal');
      const totalEl = document.getElementById('history-total-amount');
      const perPersonEl = document.getElementById('history-per-person-amount');
      const peopleEl = document.getElementById('history-people-count');
      const listEl = document.getElementById('history-orders-list');
      
      totalEl.textContent = formatCurrency(orderedTotal);
      
      const headcount = splitCount || guestCount || 0;
      if (headcount > 0) {
        const perPerson = Math.floor(orderedTotal / headcount);
        perPersonEl.textContent = formatCurrency(perPerson);
        peopleEl.textContent = `${'$'}{headcount}${'$'}{langData[currentLang].historyPeopleLabel}`;
      } else {
        perPersonEl.textContent = langData[currentLang].perPersonZero;
        peopleEl.textContent = '';
      }
      
      // Fetch and render order history
      fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/orders/current`)
        .then(res => res.json())
        .then(data => {
          const orders = data.orders || [];
          if (!orders.length) {
            listEl.innerHTML = `<div class=\"history-empty\">${'$'}{langData[currentLang].orderHistoryEmpty}</div>`;
          } else {
            listEl.innerHTML = orders.map(order => {
              const total = (order.items || []).reduce((s, i) => s + (i.quantity * i.unitPrice), 0);
              const items = (order.items || []).map(item => `
                <div class=\"history-item\">
                  <div>
                    <div class=\"history-name\">${'$'}{item.name}</div>
                    <div class=\"history-meta\">¥${'$'}{item.unitPrice.toLocaleString()} × ${'$'}{item.quantity}<span class=\"item-pill\">${'$'}{formatItemStatus(item.status)}</span></div>
                  </div>
                  <div class=\"history-price\">¥${'$'}{(item.unitPrice * item.quantity).toLocaleString()}</div>
                </div>
              `).join("");
              return `
                <div class=\"history-order\">
                  <div class=\"history-order-head\">
                    <div>#${'$'}{order.orderId}</div>
                    <span class=\"pill pill-${'$'}{order.status}\">${'$'}{formatOrderStatus(order.status)}</span>
                  </div>
                  ${'$'}{items}
                  <div class=\"total-inline\"><span>${'$'}{langData[currentLang].total}</span><span>¥${'$'}{total.toLocaleString()}</span></div>
                </div>
              `;
            }).join("");
          }
        })
        .catch(err => {
          listEl.innerHTML = `<div class=\"history-empty\">エラーが発生しました</div>`;
          console.error('History load error:', err);
        });
      
      modal.classList.add('active');
    }

    function closeHistoryPage(e) {
      if (!e || e.target.id === 'history-modal') {
        document.getElementById('history-modal').classList.remove('active');
      }
    }

    // メニュー更新チェック（売り切れ反映用）
    let menuVersionHash = "";
    async function checkMenuUpdate() {
      if (!guestCount) return; // 人数選択前はスキップ
      try {
        const url = new URL(window.location.origin + "/api/public/menu");
        url.searchParams.set("table_token", TABLE_TOKEN);
        url.searchParams.set("lang", currentLang);
        const res = await fetch(url.toString());
        if (!res.ok) return;
        const newItems = await res.json();
        
        // ハッシュ比較（売り切れ状態のみチェック）
        const newHash = newItems.map(i => `${'$'}{i.id}:${'$'}{i.isAvailable}`).join(",");
        if (menuVersionHash && menuVersionHash !== newHash) {
          // メニューが変更された場合、更新
          menuItems = newItems;
          buildCategories();
          renderMenu();
          console.log("メニュー更新を検出しました");
        }
        menuVersionHash = newHash;
      } catch (e) { console.log("menu update check error", e); }
    }

      
      // 定期更新（5秒間隔）
      setInterval(loadOrderSummary, 5000);
      setInterval(checkMenuUpdate, 10000);
      setInterval(checkTableStatus, 3000); // Check status every 3s
      
      loadMenu();
      loadOrderSummary();
      
      // Auto-hide branding after 3s if not tapped
      setTimeout(() => {
        const branding = document.getElementById('branding-splash');
        if (branding && branding.style.display !== 'none') {
           // Optional: auto show guest selection? No, let user tap.
        }
      }, 3000);
    });

    // Table Status Polling
    let lastTableStatus = "ACTIVE";
    async function checkTableStatus() {
        try {
            const res = await fetch(`/api/public/tables/${'$'}{encodeURIComponent(TABLE_TOKEN)}/status`);
            if (!res.ok) return;
            const data = await res.json();
            const status = data.status;
            
            if (status !== lastTableStatus) {
                lastTableStatus = status;
                updateStatusBanner(status);
            }
        } catch(e) { console.error("Status check failed", e); }
    }

    function updateStatusBanner(status) {
        // Remove existing banner if any
        const existing = document.getElementById('status-banner');
        if (existing) existing.remove();

        let banner = null;
        if (status === "CHECKOUT_REQUESTED" || status === "PAYMENT_IN_PROGRESS") {
            const color = status === "CHECKOUT_REQUESTED" ? "#D35400" : "#2980B9";
            const text = status === "CHECKOUT_REQUESTED" ? "お会計待ち" : "決済手続き中...";
            
            banner = document.createElement("div");
            banner.id = "status-banner";
            banner.style.cssText = `
                position: fixed; top: 0; left: 0; right: 0;
                background: ${'$'}{color}; color: white;
                text-align: center; padding: 4px; font-weight: bold; font-size: 12px;
                z-index: 10001; animation: slideDown 0.3s;
            `;
            banner.innerText = text;
        } else if (status === "PAID") {
             banner = document.createElement("div");
            banner.id = "status-banner";
            banner.style.cssText = `
                position: fixed; top: 0; left: 0; right: 0;
                background: #27AE60; color: white;
                text-align: center; padding: 8px; font-weight: bold; font-size: 14px;
                z-index: 10001; animation: slideDown 0.3s;
            `;
            banner.innerText = "お支払いが完了しました。ありがとうございました！";
            
            // Auto close payment modal if open
            closePaymentModal();
        }

        if (banner) {
             document.body.appendChild(banner);
        }
    }
      // If branding visual is missing (default text only), auto-skip to guest selection
      // But user wanted splash -> guest flow. The build logic ensures some branding visual exists.
      // So we just ensure branding is shown first.
      
      if (branding && guest) {
        branding.classList.remove('fade-out');
        branding.style.display = 'flex';
        
        guest.style.display = 'none';
        guest.classList.remove('visible');
      }
      
      updateTexts();
      // Preload menu immediately so it's ready when splash fades
      loadMenu();
      loadOrderSummary();
      
      // 30秒ごとにメニュー更新チェック
      setInterval(checkMenuUpdate, 30000);
    });
  </script>
</body>
</html>
    """.trimIndent()
    }

    /**
     * デバッグ用（API呼び出し確認）超簡易版
     */
    fun buildSimpleDebug(tableToken: String, tableName: String): String = """
<!doctype html>
<html lang="ja">
<head>
  <meta charset="utf-8" />
  <title>モバイルオーダー - $tableName</title>
  <meta name="viewport" content="width=device-width,initial-scale=1" />
</head>
<body>
  <h1>テーブル: $tableName</h1>
  <div id="status">メニューを読み込み中...</div>
  <pre id="debug" style="background:#f5f5f5; padding:8px;"></pre>
  <div id="list"></div>
  <script>
    var TABLE_TOKEN = "${tableToken}";
    function appendDebug(line) { document.getElementById('debug').textContent += line + "\\n"; }
    function setStatus(text) { document.getElementById('status').textContent = text; }
    function renderMenu(items) {
      if (!items || !items.length) { setStatus("メニューが登録されていません"); return; }
      var html = "<ul>"; for (var i=0;i<items.length;i++){ var it=items[i]; html += "<li>"+it.name+" - ¥"+it.price+"</li>"; } html+="</ul>";
      document.getElementById('list').innerHTML = html; setStatus("メニュー取得完了 ("+items.length+" 件)");
    }
    function loadMenu() {
      var menuUrl = "/api/public/menu?table_token=" + encodeURIComponent(TABLE_TOKEN);
      setStatus("GET " + menuUrl); appendDebug("fetch " + menuUrl);
      fetch(menuUrl).then(function(res){ appendDebug("status=" + res.status); if(!res.ok) throw new Error("status="+res.status); return res.json(); })
        .then(function(data){ appendDebug("items length=" + data.length); renderMenu(data); })
        .catch(function(err){ setStatus("エラー: " + err); appendDebug("error: " + err); });
    }
    window.addEventListener("load", loadMenu);
  </script>
</body>
</html>
    """.trimIndent()
}
