package pancordev.pl.snookie.utils.net

import dagger.Module
import dagger.Provides
import pancordev.pl.snookie.di.ActivityScoped
import java.net.InetSocketAddress
import java.net.Socket

@Module
class NetModule {

    @Provides
    @ActivityScoped
    fun provideSocketAddress() = InetSocketAddress("8.8.8.8", 53)
}