package pancordev.pl.snookie.model

data class Result(override val isSuccessful: Boolean, override val code: String): ResultAbs()