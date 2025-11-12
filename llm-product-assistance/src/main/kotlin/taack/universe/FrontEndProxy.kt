package taack.universe

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.net.http.*;


@Path("/front")
class FrontEndProxy {
    private val client: HttpClient = HttpClient.newHttpClient()

    @GET
    @Path("/{path: .*}")
    fun proxyFrontend(@PathParam("path") path: String): Response {
        return try {
            val target = URI.create("http://localhost:5173/$path")
            val req = HttpRequest.newBuilder(target).GET().build()
            val resp = client.send(req, HttpResponse.BodyHandlers.ofByteArray())

            // detect proper content type from vite
            val contentType = resp.headers().firstValue("content-type").orElse("application/javascript")

            Response.status(resp.statusCode())
                .header("Content-Type", contentType)
                .header("Cache-Control", "no-cache")
                .entity(resp.body())
                .build()

        } catch (e: Exception) {
            e.printStackTrace()
            Response.status(Response.Status.BAD_GATEWAY)
                .entity("Proxy error: ${e.message}")
                .type("text/plain")
                .build()
        }
    }
}