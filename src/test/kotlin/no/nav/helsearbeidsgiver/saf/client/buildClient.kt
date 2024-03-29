package no.nav.helsearbeidsgiver.saf.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.*
import io.ktor.utils.io.ByteReadChannel
import no.nav.helsearbeidsgiver.saf.client.rest.SafDokumentRestKlient
import no.nav.helsearbeidsgiver.saf.client.graphql.SafKlientImpl
import no.nav.helsearbeidsgiver.saf.client.graphql.SyncSafKlient
import java.util.UUID

fun buildSafDokumentRestKlient(
    status: HttpStatusCode,
    content: String
): SafDokumentRestKlient {
    return SafDokumentRestKlient(
        "",
        mockHttpClient(status, content),
        { UUID.randomUUID().toString() }
    )
}

fun buildSafKlient(
    response: String,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Headers = headersOf(HttpHeaders.ContentType, "application/json")
): SyncSafKlient {
    val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(response),
            status = status,
            headers = headers
        )
    }

    return SafKlientImpl(
        "https://saf.dev.intern.nav.no/graphql ",
        { UUID.randomUUID().toString() },
        HttpClient(mockEngine) { install(JsonFeature) }
    )
}
