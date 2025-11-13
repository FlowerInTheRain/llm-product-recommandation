package taack.universe.persistence

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class LlmLogsRepository: PanacheRepository<LlmLogsEntity?> {
}