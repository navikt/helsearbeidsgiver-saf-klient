package no.nav.client

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpStatement
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.util.toByteArray
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.utils.*
import java.util.UUID

class SafDokumentClient(
    private val url: String,
    private val httpClient: HttpClient,
    private val stsClient: AccessTokenProvider
) {
    val log = log()

    fun hentDokument(
        journalpostId: String,
        dokumentInfoId: String,
    ): ByteArray? {
        log.info("Henter dokument fra journalpostId $journalpostId, og dokumentInfoId $dokumentInfoId")
        val response = runBlocking {
            httpClient.get<HttpStatement>("$url/hentdokument/$journalpostId/$dokumentInfoId/ORIGINAL") {
                accept(ContentType.Application.Xml)
                header("Authorization", "Bearer ${stsClient.getToken()}")
                header("Nav-Callid", MDCOperations.putToMDC(MDCOperations.MDC_CALL_ID, UUID.randomUUID().toString()))
                header("Nav-Consumer-Id", "syfoinntektsmelding")
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

