package com.trivaris.encryptiontool.networking.messagehandlers

enum class MessageType {
    PLAIN,
    KEY_EXCHANGE_REQUEST,
    KEY_EXCHANGE_RESPONSE;

    fun getMessage(message: String) = "$this::$message"

    companion object {
        fun fromRaw(raw: String): MessageType {
            val typeName = raw.substringBefore("::")
            return valueOf(typeName)
        }
    }
}