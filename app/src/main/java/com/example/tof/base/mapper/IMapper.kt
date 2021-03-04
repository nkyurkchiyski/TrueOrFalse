package com.example.tof.base.mapper

interface IMapper<F, T> {
    fun map(from: F): T
}