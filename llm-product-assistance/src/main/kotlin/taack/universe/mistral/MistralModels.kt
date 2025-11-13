package taack.universe.mistral

import kotlinx.serialization.Serializable
import taack.universe.Role

class MistralModels {

    @Serializable
    data class ChatCompletionRequest(
        val messages: List<MessageRequest>,
        val agent_id: String? = null


    ) {
        override fun toString(): String {
            return "ChatCompletionRequest(messages=$messages, agent_id=$agent_id)"
        }
    }
    @Serializable
    data class MessageRequest(
        val id:Long,
        val role: String = Role.USER.roleValue,
        val content: String
    )
    @Serializable
    data class Message(
        val role: String = "user",
        val tool_calls: String? = null,
        val content: String
    )
    @Serializable
    data class ChatCompletionResponse(
        val id: String,
        val `object`: String,
        val model: String,
        val created: Long,
        val choices: List<Choice>,
        val usage: Usage
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