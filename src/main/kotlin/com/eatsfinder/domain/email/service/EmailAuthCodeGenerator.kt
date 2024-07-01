package com.eatsfinder.domain.email.service

import java.util.*


class EmailAuthCodeGenerator {
    private val authCodeLength = 10
    private val characterTable = charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    )

    fun executeGenerate(): String {
        val random = Random(System.currentTimeMillis())
        val buffer = StringBuffer()

        for (i in 0 until authCodeLength) buffer.append(characterTable[random.nextInt(characterTable.size)])

        return buffer.toString()
    }
}