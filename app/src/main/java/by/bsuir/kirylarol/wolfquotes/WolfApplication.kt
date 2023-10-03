package by.bsuir.kirylarol.wolfquotes;

import android.app.Application;
import org.koin.android.ext.koin.androidContext;
import org.koin.core.context.startKoin;



class WolfApplication : Application() {
    @Override
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules()
        }
    }
}
