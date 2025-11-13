package taack.universe

import kotlinx.serialization.Serializable

@Serializable
data class LlmRequest(val content: List<MistralModels.MessageRequest>)
