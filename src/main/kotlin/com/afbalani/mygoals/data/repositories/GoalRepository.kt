package com.afbalani.mygoals.data.repositories

import com.afbalani.mygoals.domain.entities.Goal
import java.time.LocalDate

class GoalRepository {
    fun retrieveCurrentGoalsForCategory(
        category: String,
        startMonth: LocalDate,
        endMonth: LocalDate?
    ): List<Goal> {
        throw Exception("Not implemented")
    }

    fun save(goal: Goal): Goal {
            TODO("Not yet implemented")
    }
}