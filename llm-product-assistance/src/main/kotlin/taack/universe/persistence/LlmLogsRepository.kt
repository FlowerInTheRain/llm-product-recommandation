package taack.universe.persistence

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import taack.universe.implementation.mistral.MistralRole
import java.util.UUID

@ApplicationScoped
class LlmLogsRepository : PanacheRepository<LlmLogsEntity?> {
    fun saveLlmMetrics(llmAgentReference: String, content: String, role: MistralRole, parentId: Long? = null): Long {
        val llmAnswer = LlmLogsEntity()
        llmAnswer.prompt = content
        llmAnswer.agentReference = llmAgentReference
        llmAnswer.role = role.roleValue
        llmAnswer.reference = UUID.randomUUID().toString()
        llmAnswer.parentId = parentId
        persistAndFlush(llmAnswer)
        return llmAnswer.id!!
    }
}