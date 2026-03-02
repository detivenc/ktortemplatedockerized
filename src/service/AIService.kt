package service

import config.ai.AIConfig
import config.ai.chatWithAI

class AIService(private val aiConfig: AIConfig) {
    suspend fun chat(message: String): String {
        return chatWithAI(aiConfig, message)
    }
}
