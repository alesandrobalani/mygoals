package com.afbalani.mygoals.domain.entities

import java.time.LocalDate

data class Goal(
    val id: String?,
    val category: String,
    val monthLimit: Double,
    val startMonth: LocalDate,
    val endMonth: LocalDate?
)