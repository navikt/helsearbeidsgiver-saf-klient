package no.nav.helsearbeidsgiver.saf.client.graphql

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.expediagroup.graphql.client.types.GraphQLClientRequest
import com.expediagroup.graphql.client.types.GraphQLClientResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Journalpost
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.BrukerIdType
import java.net.URL
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Journalpost as JournalpostBruker

interface SafKlient {
    suspend fun <T : Any> execute(query: GraphQLClientRequest<T>, callId: String): GraphQLClientResponse<T>
}

interface SyncSafKlient {
    fun dokumentoversiktFagsakSync(fagsak: String, fagsystem: String, callId: String): List<Journalpost?>
    fun dokumentoversiktBrukerSync(id: String, type: BrukerIdType, callId: String): List<JournalpostBruker?>
}

class SafKlientImpl(
    url: String,
    private val accessTokenProvider: () -> String,
    httpClient: HttpClient
) : SafKlient, SyncSafKlient {
    private val graphQLClient = GraphQLKtorClient(
        url = URL(url),
        httpClient = httpClient
    )

    override suspend fun <T : Any> execute(query: GraphQLClientRequest<T>, callId: String): GraphQLClientResponse<T> =
        graphQLClient.execute(query) {
            header(HttpHeaders.Authorization, "Bearer ${accessTokenProvider()}")
            header("Nav-Callid", callId)
            header("Nav-Consumer-Id", "helsearbeidsgiver-saf-klient")
        }

    override fun dokumentoversiktFagsakSync(fagsak: String, fagsystem: String, callId: String): List<Journalpost?> {
        return runBlocking { dokumentoversiktFagsak(fagsak, fagsystem, callId) }
    }

    override fun dokumentoversiktBrukerSync(id: String, type: BrukerIdType, callId: String): List<JournalpostBruker?> {
        return runBlocking { dokumentoversiktBruker(id, type, callId) }
    }
}
