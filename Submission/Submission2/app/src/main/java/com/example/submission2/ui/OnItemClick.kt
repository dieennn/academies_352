package com.example.submission2.ui

interface OnItemClick<T, BINDING> {
    fun onClick(data: T, binding: BINDING)
}