package no.nav.helsearbeidsgiver.saf.client.graphql

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider
import org.slf4j.LoggerFactory
import java.net.URL

class SafKlient(
    url: URL,
    private val accessTokenProvider: AccessTokenProvider,
    httpClient: HttpClient
) {
    private val graphQLClient = GraphQLKtorClient(
        url = url,
        httpClient = httpClient
    )

    suspend fun <T : Any> execute(query: GraphQLClientRequest<T>): GraphQLClientResponse<T> =
        graphQLClient.execute(query) {
            header(HttpHeaders.Authorization, "Bearer ${accessTokenProvider.getToken()}")
        }

    val logger: org.slf4j.Logger = LoggerFactory.getLogger(this::class.java)
}
