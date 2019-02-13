package pancordev.pl.snookie.base


interface BasePresenter<T> {

    fun onStart(view: T)

    fun onStop()
}