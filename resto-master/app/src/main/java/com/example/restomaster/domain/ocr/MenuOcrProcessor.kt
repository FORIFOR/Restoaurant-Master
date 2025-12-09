package com.example.restomaster.domain.ocr

import android.graphics.Rect
import com.example.restomaster.data.model.MenuCandidate
import com.example.restomaster.data.model.OcrTextLine
import javax.inject.Inject
import kotlin.math.abs
import java.text.Normalizer

class MenuOcrProcessor @Inject constructor() {

    private val noiseWords = listOf(
        "税込", "税抜", "おすすめ", "限定", "人気", "円", "yen", "YEN", "No.", "Op",
        "写真はイメージです", "※", "allergy", "kcal"
    )

    // Characters often misread as numbers in price context
    private val priceReplacements = mapOf(
        "O" to "0", "o" to "0",
        "I" to "1", "l" to "1",
        "S" to "5", "s" to "5",
        "Z" to "2", "z" to "2",
        "¥" to "", "," to "",
        "ー" to "", "-" to ""
    )

    // Regex to find price-like strings (digits, potentially with commas)
    // We capture the number part. We allow some surrounding noise which we'll clean.
    private val priceRegex = Regex("([0-9０-９]+)")

    fun process(lines: List<OcrTextLine>): List<MenuCandidate> {
        // Debug: log raw OCR lines to understand what ML Kit recognized
        lines.forEachIndexed { idx, line ->
            android.util.Log.i("OCR_LINE", "line#$idx text='${line.text}' bbox=${line.boundingBox}")
        }
        // 1. Normalize and Clean individual lines
        val cleanedLines = lines.map { cleanLine(it) }.filter { it.text.isNotBlank() }

        // 2. Group by visual rows
        val rowGroups = groupLinesByRow(cleanedLines)

        // 3. Parse each row group into a partial candidate (Name+Price, Name only, or Price only)
        val partialCandidates = rowGroups.mapNotNull { parseRowGroup(it) }

        // 4. Merge vertical splits (Name line followed by Price line)
        return mergeVerticalCandidates(partialCandidates)
    }

    private fun cleanLine(line: OcrTextLine): OcrTextLine {
        // 1. Basic normalization
        val text = normalizeName(line.text)
        return line.copy(text = text)
    }

    private fun normalizeName(raw: String): String {
        var s = raw.trim()
        // Remove leading dots/bullets often misread
        s = s.trimStart('・', '･', '•', '•', '・', '.', '＊', '･')

        // Normalize width (Full-width to Half-width for alphanumeric, but keep Kana full)
        // Actually, Normalizer.NFKC converts full-width Kana to full-width (mostly) but alphanumeric to half.
        s = Normalizer.normalize(s, Normalizer.Form.NFKC)

        // Filter to keep only Japanese-like characters, vowels (for extenders), and basic punctuation
        val filtered = s.filter { ch ->
            isJapaneseChar(ch) || ch == 'ー' || ch.isWhitespace() || ch.isDigit() || ch == '円'
        }

        return filtered.trim()
    }

    private fun isJapaneseChar(ch: Char): Boolean {
        val code = ch.code
        return (code in 0x3040..0x309F) ||    // Hiragana
               (code in 0x30A0..0x30FF) ||    // Katakana
               (code in 0x4E00..0x9FFF) ||    // Kanji
               (code in 0xFF10..0xFF19)       // Full-width digits
    }

    // Simple Levenshtein distance
    private fun levenshtein(lhs: CharSequence, rhs: CharSequence): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        var cost = IntArray(lhsLength + 1) { it }
        var newCost = IntArray(lhsLength + 1) { 0 }

        for (i in 1..rhsLength) {
            newCost[0] = i
            for (j in 1..lhsLength) {
                val match = if (lhs[j - 1] == rhs[i - 1]) 0 else 1
                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1
                newCost[j] = minOf(costInsert, costDelete, costReplace)
            }
            val swap = cost
            cost = newCost
            newCost = swap
        }
        return cost[lhsLength]
    }

    private val MENU_DICTIONARY = listOf(
        "チーズフライ", "厚揚げ", "ごぼう揚げ", "ポテトフライ", "だし巻き卵", "唐揚げ",
        "枝豆", "冷奴", "キムチ", "サラダ", "刺身", "焼き鳥", "ビール", "ハイボール",
        "ウーロン茶", "コーラ", "焼きそば", "おにぎり", "味噌汁", "漬物",
        "軟骨唐揚げ", "タコの唐揚げ", "フライドポテト", "シーザーサラダ", "大根サラダ",
        "ホッケ焼き", "エイヒレ", "ししゃも", "手羽先", "つくね", "砂肝", "皮",
        "レバー", "ハツ", "ねぎま", "もも", "ぼんじり", "生ビール", "瓶ビール",
        "レモンサワー", "グレープフルーツサワー", "緑茶ハイ", "ウーロンハイ",
        "日本酒", "焼酎", "ワイン", "梅酒", "ソフトドリンク"
    )

    private fun suggestFromDictionary(normalized: String): String {
        var best: String? = null
        var bestScore = Int.MAX_VALUE

        for (candidate in MENU_DICTIONARY) {
            val d = levenshtein(normalized, candidate)
            if (d < bestScore) {
                bestScore = d
                best = candidate
            }
        }
        // If distance is within 40% of length, suggest it
        return if (best != null && bestScore <= normalized.length * 0.4) {
            best
        } else {
            normalized
        }
    }

    private fun groupLinesByRow(lines: List<OcrTextLine>): List<List<OcrTextLine>> {
        if (lines.isEmpty()) return emptyList()

        // Sort by Y coordinate (top to bottom)
        val sortedLines = lines.sortedBy { it.boundingBox?.centerY() ?: 0 }
        
        val groups = mutableListOf<MutableList<OcrTextLine>>()
        var currentGroup = mutableListOf<OcrTextLine>()
        
        // Calculate average line height to determine threshold
        val avgHeight = lines.mapNotNull { it.boundingBox?.height() }.average()
        val threshold = if (avgHeight > 0) avgHeight * 0.5 else 20.0 // Slightly relaxed threshold

        for (line in sortedLines) {
            if (currentGroup.isEmpty()) {
                currentGroup.add(line)
            } else {
                val lastLine = currentGroup.last()
                val lastY = lastLine.boundingBox?.centerY() ?: 0
                val currentY = line.boundingBox?.centerY() ?: 0
                
                if (abs(currentY - lastY) < threshold) {
                    currentGroup.add(line)
                } else {
                    groups.add(currentGroup)
                    currentGroup = mutableListOf(line)
                }
            }
        }
        if (currentGroup.isNotEmpty()) {
            groups.add(currentGroup)
        }
        
        return groups
    }

    private fun parseRowGroup(group: List<OcrTextLine>): PartialCandidate? {
        if (group.isEmpty()) return null

        // Sort left to right
        val sortedGroup = group.sortedBy { it.boundingBox?.centerX() ?: 0 }

        // Try to identify price blocks
        val priceBlocks = mutableListOf<OcrTextLine>()
        val nameBlocks = mutableListOf<OcrTextLine>()

        for (block in sortedGroup) {
            if (isPriceBlock(block.text)) {
                priceBlocks.add(block)
            } else {
                nameBlocks.add(block)
            }
        }

        // Case 1: Horizontal Pair (Name ... Price)
        if (priceBlocks.isNotEmpty() && nameBlocks.isNotEmpty()) {
            // Assume the right-most price is the valid one for this row
            val priceBlock = priceBlocks.last()
            val price = parsePrice(priceBlock.text)
            val name = nameBlocks.joinToString(" ") { it.text }
            
            return PartialCandidate(
                name = name,
                price = price,
                originalLines = group,
                boundingBox = combineRects(group.mapNotNull { it.boundingBox })
            )
        }

        // Case 2: Price Only
        if (priceBlocks.isNotEmpty() && nameBlocks.isEmpty()) {
             val priceBlock = priceBlocks.last()
             val price = parsePrice(priceBlock.text)
             return PartialCandidate(
                 name = "", // Missing name
                 price = price,
                 originalLines = group,
                 boundingBox = combineRects(group.mapNotNull { it.boundingBox })
             )
        }

        // Case 3: Name Only
        if (priceBlocks.isEmpty() && nameBlocks.isNotEmpty()) {
            val name = nameBlocks.joinToString(" ") { it.text }
            // Filter out very short garbage
            if (name.length <= 1) return null
            
            return PartialCandidate(
                name = name,
                price = null, // Missing price
                originalLines = group,
                boundingBox = combineRects(group.mapNotNull { it.boundingBox })
            )
        }

        return null
    }

    private fun isPriceBlock(text: String): Boolean {
        // Apply replacements to check if it looks like a price
        var processed = text
        priceReplacements.forEach { (k, v) -> processed = processed.replace(k, v) }
        
        // Check if it contains digits and is short enough
        return priceRegex.containsMatchIn(processed) && processed.length < 10
    }

    private fun parsePrice(raw: String): Int? {
        var processed = raw
        priceReplacements.forEach { (k, v) -> processed = processed.replace(k, v) }
        
        // Extract the first number found
        val match = priceRegex.find(processed) ?: return null
        return match.value.toIntOrNull()
    }

    private fun mergeVerticalCandidates(candidates: List<PartialCandidate>): List<MenuCandidate> {
        val result = mutableListOf<MenuCandidate>()
        var pendingName: PartialCandidate? = null

        for (candidate in candidates) {
            if (candidate.price != null && candidate.name.isNotEmpty()) {
                // Complete candidate
                if (pendingName != null) {
                    // Flush pending name as a name-only item (or maybe it was a header?)
                    // For now, let's ignore name-only items if they weren't paired, 
                    // OR we can add them with 0 price. Let's add them with null price to be safe.
                    // But MenuCandidate requires price? Let's check model.
                    // Assuming we want valid items.
                    result.add(candidate.toMenuCandidate())
                    pendingName = null
                } else {
                    result.add(candidate.toMenuCandidate())
                }
            } else if (candidate.price != null && candidate.name.isEmpty()) {
                // Price only line
                if (pendingName != null) {
                    // Pair with pending name!
                    // Check vertical distance? For now assume sequential flow.
                    result.add(
                        MenuCandidate(
                            name = pendingName.name,
                            price = candidate.price,
                            originalLines = pendingName.originalLines + candidate.originalLines
                        )
                    )
                    pendingName = null
                } else {
                    // Orphaned price... ignore or add as "Unknown"?
                    // Ignore for now.
                }
            } else if (candidate.price == null && candidate.name.isNotEmpty()) {
                // Name only line
                if (pendingName != null) {
                    // Previous pending name was never paired. 
                    // Maybe it was a header or multi-line name.
                    // Let's treat it as a separate item (maybe price is missing).
                    // Or merge? "Grilled" \n "Chicken"
                    // Simple logic: Overwrite pending name (assuming previous was noise or header)
                    // Better logic: Merge names?
                    val mergedName = pendingName.name + " " + candidate.name
                    pendingName = pendingName.copy(
                        name = mergedName,
                        originalLines = pendingName.originalLines + candidate.originalLines,
                        boundingBox = combineRects(listOfNotNull(pendingName.boundingBox, candidate.boundingBox))
                    )
                } else {
                    pendingName = candidate
                }
            }
        }
        
        // If there's a leftover pending name, we might want to add it if we allow price-less items
        // But for a menu order system, price is usually required.
        
        return result
    }

    private fun combineRects(rects: List<Rect>): Rect {
        if (rects.isEmpty()) return Rect(0,0,0,0)
        val left = rects.minOf { it.left }
        val top = rects.minOf { it.top }
        val right = rects.maxOf { it.right }
        val bottom = rects.maxOf { it.bottom }
        return Rect(left, top, right, bottom)
    }

    private data class PartialCandidate(
        val name: String,
        val price: Int?,
        val originalLines: List<OcrTextLine>,
        val boundingBox: Rect
    )

    private fun PartialCandidate.toMenuCandidate(): MenuCandidate {
        val suggested = suggestFromDictionary(name)
        return MenuCandidate(
            id = 0L,
            name = name,
            price = price,
            suggestedName = if (suggested != name) suggested else null,
            originalLines = originalLines
        )
    }
}
