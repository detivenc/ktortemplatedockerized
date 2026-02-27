package plugins

import ai.koog.agents.core.agent.AIAgent
import ai.koog.prompt.executor.clients.openai.OpenAIClientSettings
import ai.koog.prompt.executor.clients.openai.OpenAILLMClient
import ai.koog.prompt.executor.llms.SingleLLMPromptExecutor
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel
import io.ktor.server.application.*
import io.ktor.util.*

data class AIConfig(
    val model: String,
    val systemPrompt: String,
    val executor: SingleLLMPromptExecutor
)

val AIConfigKey = AttributeKey<AIConfig>("AIConfig")

fun Application.configureAI() {
    val config = environment.config

    val lmStudioUrlRaw = config.propertyOrNull("ai.lmstudio.url")?.getString()
        ?: System.getenv("LM_STUDIO_URL") ?: "http://localhost:1234"

    val apiKey = config.propertyOrNull("ai.lmstudio.apiKey")?.getString()
        ?: System.getenv("LM_STUDIO_API_KEY") ?: "lm-studio"

    val modelRaw = config.propertyOrNull("ai.lmstudio.model")?.getString()
        ?: System.getenv("LM_STUDIO_MODEL") ?: "local-model"

    val systemPrompt = config.propertyOrNull("ai.systemPrompt")?.getString()
        ?: "You are a helpful assistant."

    val client = OpenAILLMClient(
        apiKey = apiKey,
        settings = OpenAIClientSettings(baseUrl = lmStudioUrlRaw)
    )
    val executor = SingleLLMPromptExecutor(client)

    attributes.put(AIConfigKey, AIConfig(model = modelRaw, systemPrompt = systemPrompt, executor = executor))
}

suspend fun chatWithAI(config: AIConfig, userMessage: String): String {
    val llmModel = LLModel(
        provider = LLMProvider.OpenAI,
        id = config.model,
        capabilities = listOf(LLMCapability.Completion, LLMCapability.Temperature),
        contextLength = 1_000_000_000L,
        maxOutputTokens = 10_000_000L
    )
    val agent = AIAgent(
        promptExecutor = config.executor,
        llmModel = llmModel,
        systemPrompt = config.systemPrompt
    )
    return agent.run(userMessage)
}