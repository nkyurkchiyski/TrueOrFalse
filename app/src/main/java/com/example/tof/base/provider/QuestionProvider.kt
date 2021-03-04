package com.example.tof.base.provider

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tof.Constants
import com.example.tof.base.dto.ResponseDTO
import com.example.tof.base.mapper.QuestionDTOToQuestionMapper
import com.example.tof.base.model.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

class QuestionProvider(
    private val context: Context,
    private val successConsumer: () -> Unit,
    private val errorConsumer: () -> Unit
) : IQuestionProvider {
    private val queue: Queue<Question> = LinkedList();
    private val requestQueue: RequestQueue = Volley.newRequestQueue(this.context)

    init {
        loadUpQuestions()
    }


    override fun get(): Question? {
        if (queue.count() <= Constants.QUESTION_COUNT_LIMIT) {
            loadUpQuestions()
        }
        return queue.poll();
    }

    private fun loadUpQuestions() {
        val request = StringRequest(
            Request.Method.GET,
            Constants.DEFAULT_API_ENDPOINT,
            handleSuccessfulResponse(),
            Response.ErrorListener {
                errorConsumer.invoke()
            }
        );
        requestQueue.add(request)
    }

    private fun handleSuccessfulResponse(): Response.Listener<String> {
        return Response.Listener { response ->
            if (response != null) {
                val parsedResponse = Json.decodeFromString<ResponseDTO>(response)
                val mapper = QuestionDTOToQuestionMapper()
                val results = parsedResponse.results.map { x -> mapper.map(x) }.toList()
                queue.addAll(results)
                successConsumer.invoke();
            }
        }
    }
}