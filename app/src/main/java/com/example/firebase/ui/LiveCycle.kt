package com.example.firebase.ui

interface LiveCycle<V> {
    fun bind(view : V)
    fun unbind()
}