package pancordev.pl.snookie.utils.net

import io.reactivex.Single


interface NetContract {

    fun hasInternetConnection(): Single<Boolean>
}