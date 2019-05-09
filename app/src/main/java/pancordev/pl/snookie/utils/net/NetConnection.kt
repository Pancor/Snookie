package pancordev.pl.snookie.utils.net

import io.reactivex.Single
import pancordev.pl.snookie.di.ActivityScoped
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

@ActivityScoped
class NetConnection @Inject constructor(private val socketAddress: InetSocketAddress): NetContract {

    companion object {
        const val NO_INTERNET_CONNECTION = "NO_INTERNET_CONNECTION "
    }
    private val timeout = 1500

    override fun hasInternetConnection() = Single.fromCallable {
        try {
            val socket = Socket()
            socket.connect(socketAddress, timeout)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}