package taack.universe

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient(configKey = "mistral-api")
@ClientHeaderParam(name = "Authorization", value = ["Bearer "])
@RegisterProvider(LoggingRequestFilter::class)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface MistralAPIClient {
    @POST
    @Path("/agents/completions")
    fun agentCompletion(request: MistralModels.ChatCompletionRequest): MistralModels.ChatCompletionResponse

    @GET
    @Path("/models")
    fun listModels(): MistralModels.ModelsResponse

}
