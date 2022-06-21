package no.nav.helsearbeidsgiver.saf.client

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import no.nav.helsearbeidsgiver.saf.client.graphql.ErrorException
import no.nav.helsearbeidsgiver.saf.client.graphql.dokumentoversiktFagsak
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
        val resultat = runBlocking { safKlient.dokumentoversiktFagsak("12345", "Test") }
        val journalpostId = resultat?.get(0)?.journalpostId
        assertEquals("1234", journalpostId)
    }

    @Test
    fun `Forventer gyldig feilmelding`() {
        val response = getResourceAsText("error.json")
        val safKlient = buildSafKlient(response)
        assertThrows<ErrorException> {
            val resultat = runBlocking { safKlient.dokumentoversiktFagsak("12345", "Test") }
        }
    }
}
