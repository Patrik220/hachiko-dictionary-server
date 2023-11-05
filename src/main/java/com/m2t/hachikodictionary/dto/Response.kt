package com.m2t.hachikodictionary.dto

import java.time.LocalDateTime

data class Response(
    val timestamp: LocalDateTime?,
    val success: Boolean?,
    val message: String?,
    val data: Any? = null
) {
    constructor(success: Boolean, message: String) : this(
        timestamp = LocalDateTime.now(),
        success = success,
        message = message,
    )

    constructor(success: Boolean, message: String, data: Any) : this(
        timestamp = LocalDateTime.now(),
        success = success,
        message = message,
        data = data
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true // If the object is compared with itself then return true
        if (other !is Response) return false // If the object is not an instance of Response then return false
        return other.success == this.success && other.message == this.message && other.data == this.data
    }
}