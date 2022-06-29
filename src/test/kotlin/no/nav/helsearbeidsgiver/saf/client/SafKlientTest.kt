package no.nav.helsearbeidsgiver.saf.client

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.helsearbeidsgiver.saf.client.graphql.ErrorException
import no.nav.helsearbeidsgiver.saf.client.graphql.SafDokumentoversiktBrukerException
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.BrukerIdType
import no.nav.helsearbeidsgiver.saf.utils.MDCOperations
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SafKlientTest {
    private fun getResourceAsText(filename: String) =
        this::class.java.classLoader.getResource("responses/$filename")!!.readText()
    @Test
    fun `Forventer gyldig response fra DokumentoversiktFagsak`() {
        val response = Json.encodeToString(lagGyldigResponse())
        val safKlient = buildSafKlient(response)
        val resultat = safKlient.dokumentoversiktFagsakSync("12345", "Test", MDCOperations.generateCallId())
        assertEquals("1234", resultat.get(0)?.journalpostId)
    }
    @Test
    fun `Forventer gyldig response fra DokumentoversiktBruker`() {
        val response = Json.encodeToString(lagGyldigDokumentBrukerResponse())
        val safKlient = buildSafKlient(response)
        val resultat = safKlient.dokumentoversiktBrukerSync("12345", BrukerIdType.AKTOERID, MDCOperations.generateCallId())
        assertEquals("1234", resultat.get(0)?.journalpostId)
    }

    @Test
    fun `Forventer gyldig feilmelding for dokumentFagsak`() {
        val response = getResourceAsText("error.json")
        val safKlient = buildSafKlient(response)
        assertThrows<ErrorException> {
            safKlient.dokumentoversiktFagsakSync("12345", "Test", MDCOperations.generateCallId())
        }
    }
    @Test
    fun `Forventer gyldig feilmelding for dokumentBruker`() {
        val response = getResourceAsText("error.json")
        val safKlient = buildSafKlient(response)
        assertThrows<SafDokumentoversiktBrukerException> {
            safKlient.dokumentoversiktBrukerSync("12345", BrukerIdType.AKTOERID, MDCOperations.generateCallId())
        }
    }
}
