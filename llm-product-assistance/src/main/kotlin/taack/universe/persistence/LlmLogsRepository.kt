package taack.universe.persistence

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import java.util.UUID

@ApplicationScoped
class LlmLogsRepository: PanacheRepository<LlmLogsEntity?> {
     fun saveLlmMetrics(llmAgentReference: String, content: String, parentId:Long? = null): Long {
        val llmAnswer = LlmLogsEntity()
        llmAnswer.prompt = content
        llmAnswer.agentReference = llmAgentReference
        llmAnswer.reference = UUID.randomUUID().toString()
        llmAnswer.parentId = parentId
        persistAndFlush(llmAnswer)
        return llmAnswer.id!!
    }
}