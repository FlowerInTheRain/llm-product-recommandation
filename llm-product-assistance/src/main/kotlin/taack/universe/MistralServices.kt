package taack.universe;

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import taack.universe.persistence.LlmMetricsEntity
import taack.universe.persistence.LlmMetricsRepository
import java.util.UUID

@ApplicationScoped
class MistralServices (
        @RestClient private val mistralClient: MistralAPIClient
) {
    @ConfigProperty(name = "mistral.api.agent_id")
    lateinit var mistry: String
    @Inject
    lateinit var llmMetricsRepository: LlmMetricsRepository

    @Transactional
    fun simpleChat(userMessage: List<MistralModels.MessageRequest>): String {
        val request = MistralModels.ChatCompletionRequest(
            agent_id = mistry,
            messages = userMessage
        )
        val toSave = LlmMetricsEntity()
        toSave.prompt = userMessage.last().content
        toSave.agentReference = mistry
        toSave.reference = UUID.randomUUID().toString()
        llmMetricsRepository.persistAndFlush(toSave)
        Log.info(toSave.id)
        return try {
            val response = mistralClient.agentCompletion(request)
            val llmAnswer = LlmMetricsEntity()
            llmAnswer.prompt = response.choices.firstOrNull()?.message?.content
            llmAnswer.agentReference = mistry
            llmAnswer.reference = UUID.randomUUID().toString()
            llmAnswer.parentId = toSave.id
            llmMetricsRepository.persistAndFlush(llmAnswer)
            response.choices.firstOrNull()?.message?.content ?: "No response"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

}
