package io.github.zmunm.search.repository

class KnownThrowable(
    message: String,
    cause: Throwable,
) : Throwable(
    message,
    cause
)
