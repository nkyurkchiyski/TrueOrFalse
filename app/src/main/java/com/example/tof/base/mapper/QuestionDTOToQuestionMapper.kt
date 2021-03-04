package com.example.tof.base.mapper

import android.text.Html
import com.example.tof.base.dto.QuestionDTO
import com.example.tof.base.model.Question

class QuestionDTOToQuestionMapper : IMapper<QuestionDTO, Question> {
    override fun map(from: QuestionDTO): Question {
        return Question(
            from.category,
            Html.fromHtml(from.question).toString(),
            from.correct_answer.toBoolean()
        )
    }

}