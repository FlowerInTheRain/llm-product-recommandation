package taack.universe.mistral

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import taack.universe.ExampleResource
import taack.universe.Role
import taack.universe.persistence.LlmLogsEntity
import taack.universe.persistence.LlmLogsRepository
import java.util.UUID

@ApplicationScoped
class MistralServices (
    @RestClient private val mistralClient: MistralAPIClient,
    @Inject var  llmLogsRepository: LlmLogsRepository,
) {
    @ConfigProperty(name = "mistral.api.agent_id")
    lateinit var mistry: String
    companion object {
        const val FORMATTER = "From : %s  && With content :  %s"
    }
    @Transactional
    fun simpleChat(userMessage: List<MistralModels.MessageRequest>): ExampleResource.LlmResponse {
        val request = MistralModels.ChatCompletionRequest(
            agent_id = mistry,
            messages = userMessage
        )
        val userContentFormatted : String
        val parentId:Long
        if(userMessage.size == 1) {
            userContentFormatted = String.format(FORMATTER, Role.USER.roleValue, userMessage
                .first().content)
            parentId = saveLlmMetrics(userContentFormatted)
        } else {
            userContentFormatted = String.format(FORMATTER, Role.USER.roleValue, userMessage
                .last().content)
            val userMessageParentID = userMessage[userMessage.size - 2].id
            parentId = saveLlmMetrics(userContentFormatted, userMessageParentID)
        }

        try {
            val response = mistralClient.agentCompletion(request)
            val agentResponse = response.choices.firstOrNull()?.message?.content
            val systemContentFormatted = String.format(FORMATTER, Role.USER.roleValue, agentResponse)
            val llmID = saveLlmMetrics(systemContentFormatted, parentId)
            return ExampleResource.LlmResponse(llmID, agentResponse ?: "No response")
        } catch (e: Exception) {
            "Error: ${e.message}"
            throw Exception("aïe")
        }
    }

    private fun saveLlmMetrics(content: String, parentId:Long? = null): Long {
        val llmAnswer = LlmLogsEntity()
        llmAnswer.prompt = content
        llmAnswer.agentReference = mistry
        llmAnswer.reference = UUID.randomUUID().toString()
        llmAnswer.parentId = parentId
        llmLogsRepository.persistAndFlush(llmAnswer)
        return llmAnswer.id!!
    }
}