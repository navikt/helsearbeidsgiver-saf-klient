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
import no.nav.helsearbeidsgiver.saf.utils.log
import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider

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
    private val stsClient: AccessTokenProvider
) {
    val log = log()

    fun hentDokument(
        journalpostId: String,
        dokumentInfoId: String,
        callId: String
    ): ByteArray? {
        log.info("Henter dokument fra journalpostId $journalpostId, og dokumentInfoId $dokumentInfoId")
        val response = runBlocking {
            httpClient.get<HttpStatement>("$url/hentdokument/$journalpostId/$dokumentInfoId/ORIGINAL") {
                accept(ContentType.Application.Xml)
                header("Authorization", "Bearer ${stsClient.getToken()}")
                header("Nav-Callid", callId)
                header("Nav-Consumer-Id", "helsearbeidsgiver-saf-klient")
            }.execute()
        }
        if (response.status != HttpStatusCode.OK) {
            log.info("Saf returnerte: httpstatus {}", response.status)
            return null
        }
        return runBlocking {
            response.content.toByteArray()
        }
    }
}
