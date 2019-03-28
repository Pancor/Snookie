package pancordev.pl.snookie.base


interface BasePresenterContract<T> {

    fun onStart(view: T)

    fun onStop()
}