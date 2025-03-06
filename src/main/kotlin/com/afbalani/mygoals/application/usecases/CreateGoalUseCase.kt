package com.afbalani.mygoals.application.usecases

import com.afbalani.mygoals.domain.entities.Goal

class CreateGoalUseCase() {
    fun execute(goal: Goal): Goal {
        return goal
    }
}