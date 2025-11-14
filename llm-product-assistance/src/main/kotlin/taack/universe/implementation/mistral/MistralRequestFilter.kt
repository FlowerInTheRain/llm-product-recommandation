package taack.universe.implementation.mistral

import io.quarkus.logging.Log
import jakarta.ws.rs.client.ClientRequestContext
import jakarta.ws.rs.client.ClientRequestFilter
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.config.inject.ConfigProperty

@Provider
class MistralRequestFilter : ClientRequestFilter {
    @ConfigProperty(name = "mistral.api.key")
    lateinit var mistralKey: String
    override fun filter(requestContext: ClientRequestContext) {
        val authorization = String.format("Bearer %s", mistralKey)
        Log.info("Authorization: $authorization")
        requestContext.headers.add("Authorization", authorization)
    }
}