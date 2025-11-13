package taack.universe

import jakarta.ws.rs.client.ClientRequestContext
import jakarta.ws.rs.client.ClientRequestFilter
import jakarta.ws.rs.ext.Provider

@Provider
class LoggingRequestFilter : ClientRequestFilter {
    override fun filter(requestContext: ClientRequestContext) {
        println("Outgoing request to: ${requestContext.uri}")
        println("HTTP method: ${requestContext.method}")
        println("Headers:")
        requestContext.headers.forEach { (k, v) -> println("$k: $v") }
        println("Entity/body: ${requestContext.entity}")
    }
}