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
    lateinit var service: MistralService

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(request:LlmRequest) : String {
        Log.info(service.test(request.content))
        return service.test(request.content)
    }
}