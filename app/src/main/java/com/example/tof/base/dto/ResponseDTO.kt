package com.example.tof.base.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDTO(val response_code: Int, val results: Collection<QuestionDTO>)