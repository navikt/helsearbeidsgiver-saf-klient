package no.nav.helsearbeidsgiver.utils

interface AccessTokenProvider {
    fun getToken(): String
}
