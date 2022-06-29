package no.nav.helsearbeidsgiver.saf.client

import com.expediagroup.graphql.client.serialization.types.KotlinxGraphQLResponse
import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterBruker
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.DokumentInfo
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Dokumentvariant
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Journalpost
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Sak
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterbruker.Dokumentoversikt as DokumentoversiktBruker
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalposttype
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalstatus
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Kanal
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Tema
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Variantformat

fun lagGyldigDokumentBrukerResponse() = KotlinxGraphQLResponse(
    data = DokumenterBruker.Result(
        DokumentoversiktBruker(
            journalposter = brukerJournalposter
        )
    )
)

val brukerJournalposter = listOf<Journalpost>(
    Journalpost(
        "1234",
        "Test Navn",
        "Test Tittel",
        Journalposttype.I,
        Journalstatus.MOTTATT,
        Tema.SYK,
        Sak("12345"),
        Kanal.ALTINN,
        "2022-05-15",
        listOf<DokumentInfo>(
            DokumentInfo(
                dokumentInfoId = "1234",
                tittel = "Test Dokument",
                brevkode = "Test",
                listOf(Dokumentvariant(Variantformat.ORIGINAL))
            )
        )

    )

)
