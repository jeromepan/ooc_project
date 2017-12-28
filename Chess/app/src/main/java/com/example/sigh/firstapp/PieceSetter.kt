package com.example.sigh.firstapp

interface PieceSetter{
    fun checkStraight(): Boolean
    fun rule(): Int
    fun move(): Boolean
    fun checkDiagonal(): Boolean
}