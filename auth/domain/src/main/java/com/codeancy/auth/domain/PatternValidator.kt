package com.codeancy.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}