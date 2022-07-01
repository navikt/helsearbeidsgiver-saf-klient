package no.nav.helsearbeidsgiver.saf.client.graphql

import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterBruker
import no.nav.helsearbeidsgiver.saf.graphql.generated.inputs.BrukerIdInput
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Journalpost
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.BrukerIdType
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalstatus
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Tema

suspend fun SafKlientImpl.dokumentoversiktBruker(id: String, type: BrukerIdType, callId: String): List<Journalpost?> {
    return dokumentoversiktBruker(buildQuery(id, type), callId)
}

fun buildQuery(
    id: String,
    type: BrukerIdType,
    forste: Int = 100,
    tema: List<Tema>? = null,
    journalstatuser: List<Journalstatus>? = null
): DokumenterBruker {
    return DokumenterBruker(
        DokumenterBruker.Variables(
            brukerId = BrukerIdInput(id = id, type = type),
            tema = tema,
            journalstatuser = journalstatuser,
            foerste = forste
        )
    )
}

suspend fun SafKlientImpl.dokumentoversiktBruker(query: DokumenterBruker, callId: String): List<Journalpost?> {
    val response = execute(query, callId)
    if (response.errors != null) {
        throw SafDokumentoversiktBrukerException("Henting av dokumentoversiktBrukermot saf-api feilet: ${response.errors}")
    }
    val jounalposter = response.data?.dokumentoversiktBruker?.journalposter
    if (jounalposter == null) return emptyList()
    return jounalposter
}
open class SafDokumentoversiktBrukerException(feilmelding: String) : Exception(feilmelding)
