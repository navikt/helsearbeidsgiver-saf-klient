package no.nav.helsearbeidsgiver.saf.client.graphql

import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterBruker
import no.nav.helsearbeidsgiver.saf.graphql.generated.inputs.BrukerIdInput
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Journalpost
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.BrukerIdType
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalstatus
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Tema
import no.nav.helsearbeidsgiver.saf.utils.MDCOperations
import no.nav.helsearbeidsgiver.saf.utils.MDCOperations.Companion.MDC_CALL_ID

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
    MDCOperations.putToMDC(MDC_CALL_ID, callId)
    val response = execute(query)
    if (response.errors != null) {
        throw SafDokumentoversiktBrukerException("Henting av dokumentoversiktBrukermot saf-api feilet: ${response.errors}")
    }
    val jounalposter = response.data?.dokumentoversiktBruker?.journalposter
    MDCOperations.remove(MDC_CALL_ID)
    if (jounalposter == null) return emptyList()
    return jounalposter
}
open class SafDokumentoversiktBrukerException(feilmelding: String) : Exception(feilmelding)
