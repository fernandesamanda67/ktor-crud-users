package com.example.util

import com.example.exception.MinimumAge
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private const val MINIMUM_AGE_YEARS = 18L

fun verifyAge(birthday: LocalDate) {
    val requiredDate = LocalDate.now().minusYears(MINIMUM_AGE_YEARS)
    if (birthday.isAfter(requiredDate)) {
        throw MinimumAge("Only over 18 years are allowed")
    }
}

fun parseToLocalDateType(dateToParse: String): LocalDate {
    return (LocalDate.parse(dateToParse, DateTimeFormatter.ofPattern("dd.MM.yyyy")))
}

fun isValidCpf(cpf: String): Boolean {
    if (cpf.isEmpty()) return false

    val numbers = cpf.filter { it.isDigit() }.map {
        it.toString().toInt()
    }

    if (numbers.size != 11) return false

    if (numbers.all { it == numbers[0] }) return false

    val dv1 = ((0..8).sumOf { (it + 1) * numbers[it] }).rem(11).let {
        if (it >= 10) 0 else it
    }

    val dv2 = ((0..8).sumOf { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
        if (it >= 10) 0 else it
    }

    return numbers[9] == dv1 && numbers[10] == dv2
}
