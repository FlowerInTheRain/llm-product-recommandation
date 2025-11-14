package taack.universe.implementation.mistral

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dev.langchain4j.agent.tool.Tool
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
class MistralModels {

    @Serializable
    data class ChatCompletionRequest(
        val messages: List<Message>,
        val agent_id: String? = null


    ) {
        override fun toString(): String {
            return "ChatCompletionRequest(messages=$messages, agent_id=$agent_id)"
        }
    }
    @Serializable
    @JsonIgnoreUnknownKeys
    data class Message(
        val role: String = MistralRole.USER.roleValue,
        val content: String
    )

    @Serializable
    data class MessageRequest(
        val id:Long? = null,
        val role: String = MistralRole.USER.roleValue,
        val content: String
    )
    @Serializable
    @JsonIgnoreUnknownKeys
    data class ChatCompletionResponse(
        val id: String,
        val `object`: String,
        val model: String,
        val created: Long,
        val choices: List<Choice>,
        val usage: Usage,
    )
    @Serializable
    data class Choice(
        val index: Int,
        val message: Message,
        val finish_reason: String
    )
    @Serializable
    data class Usage(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
    )

    data class ModelsResponse(
        val `object`: String,
        val data: List<Model>
    )

    data class Model(
        val id: String,
        val `object`: String,
        val created: Long,
        val owned_by: String
    )
}