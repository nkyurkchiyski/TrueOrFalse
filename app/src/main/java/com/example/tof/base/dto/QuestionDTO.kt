package com.example.tof.base.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: Collection<String>
)