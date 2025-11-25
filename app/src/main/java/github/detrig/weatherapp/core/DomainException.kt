package github.detrig.weatherapp.core

sealed class DomainException : Exception()

data object NoInternetException : DomainException() {
    private fun readResolve(): Any = NoInternetException
}

data class GenericDomainException(
    val origin: Throwable
) : DomainException()