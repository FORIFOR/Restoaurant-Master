package com.example.restomaster.domain.usecase

import javax.inject.Inject

class SuggestAllergensUseCase @Inject constructor() {
    suspend operator fun invoke(name: String, description: String?): List<String> {
        val allergens = mutableListOf<String>()
        val text = "$name ${description ?: ""}"
        
        if (text.contains("卵") || text.contains("オムレツ")) allergens.add("egg")
        if (text.contains("乳") || text.contains("チーズ") || text.contains("クリーム")) allergens.add("milk")
        if (text.contains("小麦") || text.contains("パン") || text.contains("パスタ")) allergens.add("wheat")
        if (text.contains("エビ") || text.contains("海老")) allergens.add("shrimp")
        if (text.contains("カニ") || text.contains("蟹")) allergens.add("crab")
        if (text.contains("そば") || text.contains("蕎麦")) allergens.add("buckwheat")
        if (text.contains("ピーナッツ") || text.contains("落花生")) allergens.add("peanut")
        
        return allergens
    }
}
