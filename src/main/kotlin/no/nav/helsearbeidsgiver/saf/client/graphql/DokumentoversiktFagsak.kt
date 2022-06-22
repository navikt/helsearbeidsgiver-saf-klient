package no.nav.helsearbeidsgiver.saf.client.graphql

import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterFagsak
import no.nav.helsearbeidsgiver.saf.graphql.generated.inputs.FagsakInput
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Journalpost

suspend fun SafKlient.dokumentoversiktFagsak(fagsak: String, fagsystem: String): List<Journalpost?> {

    val query = DokumenterFagsak(
        DokumenterFagsak.Variables(
            fagsak = FagsakInput(fagsak, fagsystem),
            foerste = 1000
        )
    )

    val response = execute(query)
    if (response.errors != null) {
        throw ErrorException(fagsak, fagsystem, response.errors.toString())
    }
    val jounalposter = response.data?.dokumentoversiktFagsak?.journalposter
    if (jounalposter == null) return emptyList()
    return jounalposter
}

fun SafKlient.dokumetoversiktFagsakSync(fagsak: String, fagsystem: String): List<Journalpost?> {
    return runBlocking { dokumentoversiktFagsak(fagsak, fagsystem) }
}

open class SafDokumentoversiktFagsakException(feilmelding: String) : Exception(feilmelding)

open class NotAuthorizedException(fagsak: String, fagsystem: String) : SafDokumentoversiktFagsakException(
    "SAF ga ikke tilgang til å hente Dokumenter for fagsak: $fagsak og fagsystem: $fagsystem"
)

open class ErrorException(fagsak: String, fagsystem: String, errors: String) :
    SafDokumentoversiktFagsakException(
        "henting av dokumentoversiktFagsak mot saf-api feilet for fagsak: $fagsak og fagsystem: $fagsystem $errors"
    )
