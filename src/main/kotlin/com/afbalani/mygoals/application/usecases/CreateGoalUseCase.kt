package com.afbalani.mygoals.application.usecases

import com.afbalani.mygoals.data.repositories.GoalRepository
import com.afbalani.mygoals.domain.entities.Goal
import java.time.LocalDate

const val END_MONTH_MUST_BE_AFTER_START_MONTH = "End month must be after start month"
const val START_MONTH_IS_BEFORE_THE_CURRENT_MONTH = "Start month is before the current month"
const val THERE_IS_ALREADY_A_GOAL_FOR_THIS_CATEGORY_IN_THIS_MONTH = "There is already a goal for this category in this month"
const val MONTH_LIMIT_MUST_BE_GREATER_THAN_0 = "Month limit must be greater than 0"

class CreateGoalUseCase(
    private val goalRepository: GoalRepository
) {

    fun execute(goal: Goal): Goal {
        val calculatedGoal = calculateGoal(goal)
        validateGoalDates(calculatedGoal)
        validateGoalMonthLimit(calculatedGoal)
        checkForExistingGoals(calculatedGoal)
        return goalRepository.save(calculatedGoal)
    }

    private fun calculateGoal(goal: Goal): Goal {
        val startMonth = goal.startMonth.withDayOfMonth(1)
        val endMonth = goal.endMonth?.withDayOfMonth(1)
        return Goal(
            null,
            goal.category,
            goal.monthLimit,
            startMonth,
            endMonth
        )
    }

    private fun validateGoalDates(goal: Goal) {

        if (goal.startMonth.isBefore(LocalDate.now().withDayOfMonth(1))) {
            throw IllegalArgumentException(START_MONTH_IS_BEFORE_THE_CURRENT_MONTH)
        }

        if (goal.endMonth?.isBefore(goal.startMonth) == true) {
            throw IllegalArgumentException(END_MONTH_MUST_BE_AFTER_START_MONTH)
        }
    }

    private fun validateGoalMonthLimit(goal: Goal) {
        if (goal.monthLimit <= 0) {
            throw IllegalArgumentException(MONTH_LIMIT_MUST_BE_GREATER_THAN_0)
        }
    }

    private fun checkForExistingGoals(goal: Goal) {
        val currentCategory = goalRepository.retrieveCurrentGoalsForCategory(
            goal.category,
            goal.startMonth,
            goal.endMonth
        )

        if (currentCategory.isNotEmpty()) {
            throw IllegalArgumentException(THERE_IS_ALREADY_A_GOAL_FOR_THIS_CATEGORY_IN_THIS_MONTH)
        }
    }
}