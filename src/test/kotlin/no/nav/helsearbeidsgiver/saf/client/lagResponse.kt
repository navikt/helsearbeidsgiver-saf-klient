package no.nav.helsearbeidsgiver.saf.client

import com.expediagroup.graphql.client.serialization.types.KotlinxGraphQLResponse
import no.nav.helsearbeidsgiver.saf.graphql.generated.DokumenterFagsak
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.DokumentInfo
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Dokumentoversikt
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Dokumentvariant
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Journalpost
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.Sak
import no.nav.helsearbeidsgiver.saf.graphql.generated.dokumenterfagsak.SideInfo
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalposttype
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Journalstatus
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Kanal
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Tema
import no.nav.helsearbeidsgiver.saf.graphql.generated.enums.Variantformat

fun lagGyldigResponse() =
    KotlinxGraphQLResponse(
        data = DokumenterFagsak.Result(
            Dokumentoversikt(
                journalposter = journalposter,
                sideInfo = SideInfo(finnesNesteSide = false)
            )
        )
    )

val journalposter = listOf<Journalpost>(
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
