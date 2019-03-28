package pancordev.pl.snookie.base


interface BasePresenterContract<T> {

    fun onSetView(view: T)

    fun onStop()
}