package no.nav.helsearbeidsgiver.saf.client.rest

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpStatement
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.util.toByteArray
import kotlinx.coroutines.runBlocking

/**
 * Klient som henter dokument fra saf
 *
 * Dokumentasjon
 * https://confluence.adeo.no/display/BOA/saf+-+REST+hentdokument
 *
 *
 * Swagger
 * https://saf-q1.dev.intern.nav.no/swagger-ui/index.html
 *
 */

class SafDokumentRestKlient(
    private val url: String,
    private val httpClient: HttpClient,
    private val accessTokenProvider: () -> String,
) {
    fun hentDokumentSync(
        journalpostId: String,
        dokumentInfoId: String,
        variantFormat: String,
        callId: String
    ): ByteArray {
        return runBlocking { hentDokument(journalpostId, dokumentInfoId, callId, variantFormat) }
    }

    suspend fun hentDokument(
        journalpostId: String,
        dokumentInfoId: String,
        variantFormat: String,
        callId: String
    ): ByteArray {
        val response = httpClient.get<HttpStatement>("$url/hentdokument/$journalpostId/$dokumentInfoId/$variantFormat") {
            accept(ContentType.Application.Xml)
            header("Authorization", "Bearer ${accessTokenProvider()}")
            header("Nav-Callid", callId)
            header("Nav-Consumer-Id", "helsearbeidsgiver-saf-klient")
        }.execute()
        if (response.status != HttpStatusCode.OK) {
            throw SafDokumentRestException(
                "Saf returnerte: httpstatus  ${response.status}" +
                    " for journalpostId $journalpostId, og dokumentInfoId $dokumentInfoId"
            )
        }
        return response.content.toByteArray()
    }
}
open class SafDokumentRestException(feilmelding: String) : RuntimeException(feilmelding)
