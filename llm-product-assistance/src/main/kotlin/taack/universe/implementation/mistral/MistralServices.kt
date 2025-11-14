package taack.universe.implementation.mistral

import io.quarkus.logging.Log
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import kotlinx.serialization.json.Json
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.rest.client.inject.RestClient
import taack.universe.llm.domain.LlmResource
import taack.universe.llm.domain.LlmService
import taack.universe.persistence.LlmLogsRepository

@ApplicationScoped
class MistralServices : LlmService {
    @RestClient
    lateinit var mistralClient: MistralAPIClient
    @Inject
    lateinit var  llmLogsRepository: LlmLogsRepository
    @ConfigProperty(name = "mistral.api.agent_id")
    lateinit var mistry: String
    @Transactional
    override fun startChat(userMessage: List<MistralModels.MessageRequest>): LlmResource.LlmResponse {
        val request = MistralModels.ChatCompletionRequest(
            agent_id = mistry,
            messages = userMessage.map { it -> MistralModels.Message(
                role = it.role,
                content = it.content
            ) }
        )
        val parentId:Long
        if(userMessage.size == 1) {
            val userContent : String = userMessage.first().content
            parentId = llmLogsRepository.saveLlmMetrics(mistry, userContent, MistralRole.USER)
        } else {
            val userContent : String = userMessage.last().content
            val userMessageParentID = userMessage[userMessage.size - 2].id
            parentId = llmLogsRepository.saveLlmMetrics(mistry,userContent, MistralRole.USER, userMessageParentID)
        }

        Log.info(Json.encodeToString(request))

        try {
            val response = mistralClient.agentCompletion(request)
            val agentResponse = response.choices.firstOrNull()?.message?.content!!
            val llmID = llmLogsRepository.saveLlmMetrics(mistry,agentResponse, MistralRole.SYSTEM, parentId)
            return LlmResource.LlmResponse(llmID, agentResponse ?: "No response")
        } catch (e: Exception) {
            Log.info(e.toString())
            Log.info(e.message)
            Log.info(e.cause!!.message)
            throw Exception("aïe")
        }
    }

}