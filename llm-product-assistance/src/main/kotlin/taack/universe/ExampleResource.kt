package taack.universe

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import kotlinx.serialization.Serializable
import taack.universe.mistral.MistralServices

@Path("/llm")
class ExampleResource {
    @Inject
    lateinit var service: MistralServices

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(request: LlmRequest) : LlmResponse {
        return service.simpleChat(request.content)
    }

    @Serializable
    data class LlmResponse(val parentID:Long, val content: String)
}