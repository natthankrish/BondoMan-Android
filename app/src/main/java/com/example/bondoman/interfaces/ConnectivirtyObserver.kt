package com.example.bondoman.interfaces

import kotlinx.coroutines.flow.Flow


interface ConnectivirtyObserver {
    fun observe() : Flow<Status>

    enum class Status{
        Available, Unavailable, Losing, Lost
    }
}