package pancordev.pl.snookie.utils.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class SchedulerProvider : BaseSchedulerProvider {

    override fun computation() = Schedulers.computation()

    override fun io() = Schedulers.io()

    override fun ui() = AndroidSchedulers.mainThread()
}