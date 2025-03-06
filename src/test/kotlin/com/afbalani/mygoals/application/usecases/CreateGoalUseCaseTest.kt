package com.afbalani.mygoals.application.usecases

import com.afbalani.mygoals.domain.entities.Goal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CreateGoalUseCaseTest {

    @InjectMocks
    lateinit var createGoalUseCase: CreateGoalUseCase

    @Test
    fun `should create a new goal`() {
        // Arrange
        val goal = Goal(null, "Learn Kotlin", 10.0, null, null)

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)
    }
}