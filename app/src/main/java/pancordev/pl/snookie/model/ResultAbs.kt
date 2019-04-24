package pancordev.pl.snookie.model


abstract class ResultAbs {

    abstract val isSuccessful: Boolean

    abstract val code: String
}