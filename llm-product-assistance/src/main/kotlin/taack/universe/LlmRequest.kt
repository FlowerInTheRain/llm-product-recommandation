package taack.universe

import kotlinx.serialization.Serializable
import taack.universe.mistral.MistralModels

@Serializable
data class LlmRequest(val content: List<MistralModels.MessageRequest>)
