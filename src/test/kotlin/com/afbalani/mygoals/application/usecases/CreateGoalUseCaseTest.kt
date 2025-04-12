package com.afbalani.mygoals.application.usecases

import com.afbalani.mygoals.data.repositories.GoalRepository
import com.afbalani.mygoals.domain.entities.Goal
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.verify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CreateGoalUseCaseTest {

    @MockK
    lateinit var goalRepository: GoalRepository

    private var createGoalUseCase: CreateGoalUseCase

    init {
        MockKAnnotations.init(this)
        createGoalUseCase = spyk(CreateGoalUseCase(goalRepository))
    }

    @Test
    fun `should create a new goal null end month`(){
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            null)

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        every {
            goalRepository.save(any())
        } returns goal

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                null)
        }

        verify(exactly = 1) {
            goalRepository.save(withArg {
                assertTrue(goal.category == it.category &&
                        goal.monthLimit == it.monthLimit &&
                        goal.startMonth.withDayOfMonth(1) == it.startMonth &&
                        goal.endMonth == it.endMonth)
            })
        }
    }

    @Test
    fun `should create a new goal not null end date`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            LocalDate.now())

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        every {
            goalRepository.save(any())
        } returns goal

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                goal.endMonth?.withDayOfMonth(1))
        }

        verify(exactly = 1) {
            goalRepository.save(withArg {
                assertTrue(goal.category == it.category &&
                        goal.monthLimit == it.monthLimit &&
                        goal.startMonth.withDayOfMonth(1) == it.startMonth &&
                        goal.endMonth?.withDayOfMonth(1) == it.endMonth)
            })
        }
    }

    @Test
    fun `should create a new goal with start date today`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            null)

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        every {
            goalRepository.save(any())
        } returns goal

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                goal.endMonth)
        }

        verify(exactly = 1) {
            goalRepository.save(withArg {
                assertTrue(goal.category == it.category &&
                        goal.monthLimit == it.monthLimit &&
                        goal.startMonth.withDayOfMonth(1) == it.startMonth &&
                        goal.endMonth == it.endMonth)
            })
        }
    }

    @Test
    fun `should create a new goal with start date after today`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now().plusDays(1),
            null)

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        every {
            goalRepository.save(any())
        } returns goal

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                goal.endMonth)
        }

        verify(exactly = 1) {
            goalRepository.save(withArg {
                assertTrue(goal.category == it.category &&
                        goal.monthLimit == it.monthLimit &&
                        goal.startMonth.withDayOfMonth(1) == it.startMonth &&
                        goal.endMonth == it.endMonth)
            })
        }
    }

    @Test
    fun `should create a new goal with start date same than end date`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            LocalDate.now())

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns emptyList()

        every {
            goalRepository.save(any())
        } returns goal

        // Act
        val result = createGoalUseCase.execute(goal)

        // Assert
        assertEquals(goal, result)

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                goal.endMonth?.withDayOfMonth(1))
        }

        verify(exactly = 1) {
            goalRepository.save(withArg {
                assertTrue(goal.category == it.category &&
                        goal.monthLimit == it.monthLimit &&
                        goal.startMonth.withDayOfMonth(1) == it.startMonth &&
                        goal.endMonth?.withDayOfMonth(1) == it.endMonth)
            })
        }
    }

    @Test
    fun `should except a new goal with end date before start date`(){
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            LocalDate.now().minusMonths(1))

        // Act & Assert
        assertFailsWith<IllegalArgumentException>(END_MONTH_MUST_BE_AFTER_START_MONTH) {
            createGoalUseCase.execute(goal)
        }

        verify(exactly = 0) {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any())
        }

        verify(exactly = 0) {
            goalRepository.save(any())
        }
    }

    @Test
    fun `should except a new goal with start date before current month`(){
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now().minusMonths(1),
            null)

        // Act & Assert
        assertFailsWith<IllegalArgumentException>(START_MONTH_IS_BEFORE_THE_CURRENT_MONTH) {
            createGoalUseCase.execute(goal)
        }

        verify(exactly = 0) {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any())
        }

        verify(exactly = 0) {
            goalRepository.save(any())
        }
    }

    @Test
    fun `should except a new goal with category existing in another current goal`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            10.0,
            LocalDate.now(),
            null)

        every {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any()
            )
        } returns listOf(Goal(null, "Learn Kotlin", 10.0, LocalDate.now(), null))

        // Act & Assert
        assertFailsWith<IllegalArgumentException>(THERE_IS_ALREADY_A_GOAL_FOR_THIS_CATEGORY_IN_THIS_MONTH) {
            createGoalUseCase.execute(goal)
        }

        verify(exactly = 1) {
            goalRepository.retrieveCurrentGoalsForCategory(
                goal.category,
                goal.startMonth.withDayOfMonth(1),
                goal.endMonth)
        }

        verify(exactly = 0) {
            goalRepository.save(any())
        }
    }

    @Test
    fun `should except a new goal with negative month limit`() {
        // Arrange
        val goal = Goal(
            null,
            "Learn Kotlin",
            -10.0,
            LocalDate.now(),
            null)

        // Act & Assert
        assertFailsWith<IllegalArgumentException>(MONTH_LIMIT_MUST_BE_GREATER_THAN_0) {
            createGoalUseCase.execute(goal)
        }

        verify(exactly = 0) {
            goalRepository.retrieveCurrentGoalsForCategory(
                any(),
                any(),
                any())
        }

        verify(exactly = 0) {
            goalRepository.save(any())
        }
    }
}
