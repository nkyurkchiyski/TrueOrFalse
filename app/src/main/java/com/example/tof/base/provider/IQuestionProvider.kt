package com.example.tof.base.provider

import com.example.tof.base.model.Question

interface IQuestionProvider {
    fun get(): Question?
}