package no.nav.helsearbeidsgiver.saf.client

import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class SafDokumentRestKlientTest {

    @Test
    fun `Test hentDokument`() {
        val safDokumentRestKlient = buildSafDokumentRestKlient(HttpStatusCode.OK, "Test String")
        val response = safDokumentRestKlient.hentDokumentSync("123", "123", "ORIGINAL", "testCallid")
        assertEquals("Test String", response.decodeToString())
    }
}
