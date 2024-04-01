package com.baris.fakestore.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import java.lang.StringBuilder
import java.text.DecimalFormat

/**
 * Created on 26.02.2024.
 * @author saycicek
 */
object Utils {
    fun formatPrice(price: Double): String {
        return if (price.toString().contains(".0")) {
            DecimalFormat("##,##0").format(price)
        } else {
            DecimalFormat("##,##0.00").format(price)
        }
    }

    fun formatCreditCard(value: String): TransformedText {
        val clearNumber = value.replace("-", "").replace(".", "").replace(",", "").replace(" ", "")
        val divider = "-"
        val sb = StringBuilder("")
        clearNumber.take(16).forEachIndexed { index, c ->
            sb.append(c)
            if(index != 15 && index % 4 == 3) {
                sb.append(divider)
            }
        }

        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }

        return TransformedText(AnnotatedString(sb.toString()), creditCardOffsetTranslator)
    }
}

fun main() {
    println(Utils.formatCreditCard("1234567890123456"))
}