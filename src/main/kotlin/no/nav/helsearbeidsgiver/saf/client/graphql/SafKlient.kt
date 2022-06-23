package no.nav.helsearbeidsgiver.saf.client.graphql

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Journalpost
import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider
import org.slf4j.LoggerFactory
import java.net.URL

interface SafKlient {
    suspend fun <T : Any> execute(query: GraphQLClientRequest<T>): GraphQLClientResponse<T>
}

interface SyncSafKlient {
    fun dokumetoversiktFagsakSync(fagsak: String, fagsystem: String): List<Journalpost?>
}

class SafKlientImpl(
    url: String,
    private val accessTokenProvider: AccessTokenProvider,
    httpClient: HttpClient
) : SafKlient, SyncSafKlient {
    private val graphQLClient = GraphQLKtorClient(
        url = URL(url),
        httpClient = httpClient
    )

    override suspend fun <T : Any> execute(query: GraphQLClientRequest<T>): GraphQLClientResponse<T> =
        graphQLClient.execute(query) {
            header(HttpHeaders.Authorization, "Bearer ${accessTokenProvider.getToken()}")
        }

    override fun dokumetoversiktFagsakSync(fagsak: String, fagsystem: String): List<Journalpost?> {
        return runBlocking { dokumentoversiktFagsak(fagsak, fagsystem) }
    }

    val logger: org.slf4j.Logger = LoggerFactory.getLogger(this::class.java)
}
