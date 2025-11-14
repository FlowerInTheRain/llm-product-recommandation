package taack.universe.implementation.mistral

import kotlinx.serialization.Serializable

@Serializable
data class LlmRequest(val content: List<MistralModels.MessageRequest>)