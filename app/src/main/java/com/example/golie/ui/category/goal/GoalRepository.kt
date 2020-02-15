/*package com.example.golie.ui.category.goal


// Global variable to store all ToDos

val goalRepository = GoalRepository().apply{
    // Lets add two initial ToDos

    addGoal(
        "Pass my exam",
        "2020-03-25",
        false,
        100
    )

    addGoal(
        "Exercise",
        null,
        true,
        5
    )

    addGoal(
        "Win a competition",
        null,
        false,
        200
    )
}

class GoalRepository {

    private val goals = mutableListOf<Goal>()

    fun addGoal(title: String, timeSpan: String?, reOccurring: Boolean, points: Int): Int {
        val id = when {
            goals.count() == 0 -> 1
            else -> goals.last().id + 1
        }
        goals.add(
            Goal(

                title,
                timeSpan,
                reOccurring,
                points
            )
        )
        return id
    }

    fun getGoals() = goals

    fun getGoalById(id: Int) =
        goals.find {
            it.id == id
        }

    fun deleteGoalById(id: Int) =
        goals.remove(
            goals.find{
                it.id == id
            }
        )

    fun updateGoalById(id: Int, newTitle: String, newTimeSpan: String, newReOccurring: Boolean, newPoints: Int) {
        getGoalById(id)?.run {
            title = newTitle
            timeSpan = newTimeSpan
            reOccurring = newReOccurring
            points = newPoints
        }
    }

}*/