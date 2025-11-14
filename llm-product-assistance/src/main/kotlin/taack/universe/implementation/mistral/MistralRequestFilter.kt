package taack.universe.implementation.mistral

import com.fasterxml.jackson.databind.ObjectMapper
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
        Log.info(requestContext.uri)
        Log.info(requestContext.entity)
        Log.info(requestContext.headers["Authorization"])
        Log.info(requestContext.headers["Content-Type"])
        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(requestContext.entity)
        Log.info("ACTUAL JSON SENT: $json")
        requestContext.headers.add("Authorization", authorization)
    }
}