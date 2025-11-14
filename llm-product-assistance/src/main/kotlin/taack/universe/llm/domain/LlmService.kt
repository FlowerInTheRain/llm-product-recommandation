package taack.universe.llm.domain

import taack.universe.implementation.mistral.MistralModels

interface LlmService {
    fun startChat(userMessage: List<MistralModels.MessageRequest>): LlmResource.LlmResponse
}