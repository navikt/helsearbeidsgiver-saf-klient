package no.nav.helsearbeidsgiver.saf.client

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.helsearbeidsgiver.saf.client.graphql.ErrorException
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
        val resultat = safKlient.dokumetoversiktFagsakSync("12345", "Test")
        assertEquals("1234", resultat?.get(0)?.journalpostId)
    }

    @Test
    fun `Forventer gyldig feilmelding`() {
        val response = getResourceAsText("error.json")
        val safKlient = buildSafKlient(response)
        assertThrows<ErrorException> {
            safKlient.dokumetoversiktFagsakSync("12345", "Test")
        }
    }
}
