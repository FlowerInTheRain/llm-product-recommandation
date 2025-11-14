package taack.universe

import jakarta.ws.rs.*
import jakarta.ws.rs.core.*
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Path("/app")
class SolidJSProxyResource {

    @Inject
    lateinit var page: Template

    @ConfigProperty(name = "solidjs.dev.server", defaultValue = "http://localhost:5173")
    lateinit var devServerUrl: String

    @ConfigProperty(name = "quarkus.profile")
    lateinit var profile: String

    private val httpClient = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build()

    @GET
    @Produces(MediaType.TEXT_HTML)
    fun app(): TemplateInstance {
        return solidapp
            .data("title", "My LLM Chat")
            .data("userData", """{"userId": "123", "name": "John"}""")
            .data("apiConfig", """{"apiUrl": "/api"}""")
    }

    @GET
    @Path("/{path:.*}")
    @Produces(MediaType.WILDCARD)
    fun proxyAssets(
        @PathParam("path") path: String,
        @Context uriInfo: UriInfo
    ): Response {
        val queryString = uriInfo.requestUri.query?.let { "?$it" } ?: ""
        val targetUrl = "$devServerUrl/$path$queryString"

        val request = HttpRequest.newBuilder()
            .uri(URI.create(targetUrl))
            .GET()
            .build()

        return try {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray())

            val responseBuilder = Response
                .status(response.statusCode())
                .entity(response.body())

            response.headers().map().forEach { (key, values) ->
                if (key.lowercase() !in listOf("transfer-encoding", "connection", "content-length")) {
                    values.forEach { value -> responseBuilder.header(key, value) }
                }
            }

            responseBuilder.build()
        } catch (e: Exception) {
            Response.status(502).entity("Proxy error: ${e.message}").build()
        }
    }
}