package taack.universe.persistence

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "llm_prompts")
class LlmMetricsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    var id: Long? = null
    @Column(name = "prompt", columnDefinition = "text", nullable = false)
    var prompt: String? = null
    @Column(name = "parent_id", columnDefinition = "integer", nullable = true)
    var parentId: Long? = null
    @Column(name = "sent_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
    var sentAt: Timestamp? = null
    @Column(name = "agent_reference", columnDefinition = "varchar(120)", nullable = false)
    var agentReference: String? = null
    @Column(name = "reference", columnDefinition = "bpchar(32)", nullable = false, unique = true, updatable = false)
    var reference: String? = null
    override fun toString(): String {
        return "LlmMetricsEntity(id=$id, prompt=$prompt, parentId=$parentId, sentAt=$sentAt, agentReference=$agentReference, reference=$reference)"
    }


}