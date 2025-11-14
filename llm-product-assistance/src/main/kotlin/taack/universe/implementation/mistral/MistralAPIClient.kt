package taack.universe.implementation.mistral

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient

@RegisterRestClient
@RegisterProvider(MistralRequestFilter::class)
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