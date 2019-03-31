package pancordev.pl.snookie.utils.schedulers

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class SchedulerModule {

    @Binds
    @Singleton
    abstract fun bindSchedulerProvider(schedulerProvider: SchedulerProvider): BaseSchedulerProvider
}