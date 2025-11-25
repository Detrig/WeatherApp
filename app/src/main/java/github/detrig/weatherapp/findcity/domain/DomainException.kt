package github.detrig.weatherapp.findcity.domain

sealed class DomainException : Exception()

data object NoInternetException : DomainException() {
    private fun readResolve(): Any = NoInternetException
}

data class GenericDomainException(
    val origin: Throwable
) : DomainException()