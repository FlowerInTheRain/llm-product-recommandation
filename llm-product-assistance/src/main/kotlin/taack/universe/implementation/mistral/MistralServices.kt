package taack.universe.implementation.mistral

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import taack.universe.llm.domain.LlmResource
import taack.universe.llm.domain.LlmService
import taack.universe.persistence.LlmLogsRepository

@ApplicationScoped
class MistralServices (
    @RestClient private val mistralClient: MistralAPIClient,
    @Inject var  llmLogsRepository: LlmLogsRepository,
) : LlmService {
    @ConfigProperty(name = "mistral.api.agent_id")
    lateinit var mistry: String
    companion object {
        const val FORMATTER = "From : %s  && With content :  %s"
    }
    @Override
    @Transactional
    fun startChat(userMessage: List<MistralModels.MessageRequest>): LlmResource.LlmResponse {
        val request = MistralModels.ChatCompletionRequest(
            agent_id = mistry,
            messages = userMessage
        )
        val userContentFormatted : String
        val parentId:Long
        if(userMessage.size == 1) {
            userContentFormatted = String.format(FORMATTER, MistralRole.USER.roleValue, userMessage
                .first().content)
            parentId = llmLogsRepository.saveLlmMetrics(mistry,userContentFormatted)
        } else {
            userContentFormatted = String.format(FORMATTER, MistralRole.USER.roleValue, userMessage
                .last().content)
            val userMessageParentID = userMessage[userMessage.size - 2].id
            parentId = llmLogsRepository.saveLlmMetrics(mistry,userContentFormatted, userMessageParentID)
        }

        try {
            val response = mistralClient.agentCompletion(request)
            val agentResponse = response.choices.firstOrNull()?.message?.content
            val systemContentFormatted = String.format(FORMATTER, MistralRole.USER.roleValue, agentResponse)
            val llmID = llmLogsRepository.saveLlmMetrics(mistry,systemContentFormatted, parentId)
            return LlmResource.LlmResponse(llmID, agentResponse ?: "No response")
        } catch (e: Exception) {
            "Error: ${e.message}"
            throw Exception("aïe")
        }
    }

}