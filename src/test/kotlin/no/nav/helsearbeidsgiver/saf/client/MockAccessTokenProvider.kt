package no.nav.helsearbeidsgiver.saf.client

import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider

class MockAccessTokenProvider : AccessTokenProvider {

    override fun getToken(): String {
        return "token"
    }
}
