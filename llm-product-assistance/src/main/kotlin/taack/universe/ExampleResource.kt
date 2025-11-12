package taack.universe

import io.quarkus.logging.Log
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/llm")
class ExampleResource {
    @Inject
    lateinit var service: MistralServices

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun hello(request:LlmRequest) : String {
        Log.info(service.answerQuestion(request.content))
        return service.answerQuestion(request.content)
    }
}