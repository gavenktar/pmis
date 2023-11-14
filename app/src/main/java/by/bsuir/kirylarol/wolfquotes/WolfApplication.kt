package by.bsuir.kirylarol.wolfquotes;

import android.app.Application;
import org.koin.android.ext.koin.androidContext;
import org.koin.core.context.startKoin;
import org.koin.dsl.module

val databaseModule = module {

}


val appModule = module {
    includes(databaseModule)
}

class WolfApplication : Application() {
    @Override
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}
