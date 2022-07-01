package no.nav.helsearbeidsgiver.saf.client.graphql

import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterFagsak
import no.nav.helsearbeidsgiver.saf.graphql.generated.inputs.FagsakInput
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Journalpost
suspend fun SafKlientImpl.dokumentoversiktFagsak(fagsak: String, fagsystem: String, callId: String): List<Journalpost?> {

    val query = DokumenterFagsak(
        DokumenterFagsak.Variables(
            fagsak = FagsakInput(fagsak, fagsystem),
            foerste = 1000
        )
    )

    val response = execute(query, callId)
    if (response.errors != null) {
        throw ErrorException(fagsak, fagsystem, response.errors.toString())
    }
    val jounalposter = response.data?.dokumentoversiktFagsak?.journalposter
    if (jounalposter == null) return emptyList()
    return jounalposter
}
open class SafDokumentoversiktFagsakException(feilmelding: String) : Exception(feilmelding)

open class NotAuthorizedException(fagsak: String, fagsystem: String) : SafDokumentoversiktFagsakException(
    "SAF ga ikke tilgang til å hente Dokumenter for fagsak: $fagsak og fagsystem: $fagsystem"
)

open class ErrorException(fagsak: String, fagsystem: String, errors: String) :
    SafDokumentoversiktFagsakException(
        "henting av dokumentoversiktFagsak mot saf-api feilet for fagsak: $fagsak og fagsystem: $fagsystem $errors"
    )
