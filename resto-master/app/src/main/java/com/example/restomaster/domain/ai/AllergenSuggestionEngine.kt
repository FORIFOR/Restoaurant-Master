package com.example.restomaster.domain.ai

import javax.inject.Inject

data class AllergenSuggestion(
    val allergenCode: String,
    val confidence: Float
)

interface AllergenSuggestionEngine {
    suspend fun suggest(
        name: String,
        description: String?
    ): List<AllergenSuggestion>
}

class RuleBasedAllergenSuggestionEngine @Inject constructor() : AllergenSuggestionEngine {

    private val patterns: Map<String, List<String>> = mapOf(
        "egg" to listOf("卵", "玉子", "たまご", "オムレツ", "マヨネーズ", "タルタル", "親子丼", "カスタード", "プリン", "茶碗蒸し", "天津飯", "カルボナーラ"),
        "milk" to listOf("乳", "牛乳", "ミルク", "チーズ", "ホイップ", "バター", "クリーム", "ラテ", "グラタン", "カルボナーラ", "シチュー", "ヨーグルト", "練乳"),
        "wheat" to listOf("小麦", "パン", "パスタ", "ラーメン", "うどん", "ピザ", "フライ", "天ぷら", "餃子", "醤油", "焼きそば", "クッキー", "唐揚げ", "カツ", "麺"),
        "buckwheat" to listOf("そば", "蕎麦"),
        "peanut" to listOf("落花生", "ピーナッツ", "ジーマーミ豆腐"),
        "shrimp" to listOf("エビ", "海老", "シュリンプ", "天ぷら", "エビフライ", "エビマヨ", "トムヤムクン"),
        "crab" to listOf("カニ", "蟹", "クラブ", "かに玉"),
        "salmon" to listOf("サケ", "鮭", "サーモン", "イクラ", "石狩鍋"),
        "mackerel" to listOf("サバ", "鯖"),
        "soybean" to listOf("大豆", "豆腐", "納豆", "味噌", "醤油", "枝豆", "きな粉", "油揚げ"),
        "chicken" to listOf("鶏", "チキン", "唐揚げ", "親子丼", "焼き鳥", "つくね"),
        "pork" to listOf("豚", "ポーク", "カツ", "ハム", "ベーコン", "チャーシュー", "ソーセージ"),
        "beef" to listOf("牛", "ビーフ", "ステーキ", "焼肉", "ローストビーフ", "ハンバーグ"),
        "sesame" to listOf("ごま", "胡麻", "セサミ", "坦々麺"),
        "gelatin" to listOf("ゼリー", "ゼラチン", "ババロア"),
        "apple" to listOf("りんご", "リンゴ", "アップル"),
        "banana" to listOf("バナナ"),
        "orange" to listOf("オレンジ", "みかん"),
        "peach" to listOf("もも", "桃", "ピーチ"),
        "kiwi" to listOf("キウイ"),
        "squid" to listOf("イカ", "烏賊"),
        "octopus" to listOf("タコ", "蛸"),
        "yam" to listOf("山芋", "やまいも", "とろろ"),
        "walnut" to listOf("くるみ", "クルミ", "ウォールナット")
    )

    override suspend fun suggest(
        name: String,
        description: String?
    ): List<AllergenSuggestion> {
        val text = (name + " " + (description ?: "")).lowercase()
        val suggestions = mutableListOf<AllergenSuggestion>()

        for ((code, keywords) in patterns) {
            val hitCount = keywords.count { kw -> text.contains(kw.lowercase()) }
            if (hitCount > 0) {
                // Simple confidence calculation: 0.7 base + 0.1 per extra keyword, max 0.95
                val conf = (0.7f + 0.1f * (hitCount - 1)).coerceAtMost(0.95f)
                suggestions += AllergenSuggestion(allergenCode = code, confidence = conf)
            }
        }
        return suggestions
    }
}
