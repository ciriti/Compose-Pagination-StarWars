# Who Provides the CoroutineContext?

In your DI setup (likely using Koin), the coroutineContext is typically provided by either:

```kotlin
val appModule = module {
    // Default dispatcher tied to application lifecycle
    single<CoroutineContext> { Dispatchers.Default }
    
    // OR more commonly, tied to application lifecycle:
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
    single<CoroutineContext> { get<CoroutineScope>().coroutineContext }
}
```

## Best Practice for Your Codebase:

   ```kotlin
   val appModule = module {
       // Application-scoped context
       single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.IO) }
       
       single<ConnectivityRepository> { 
           ConnectivityRepositoryImpl(
               context = get(),
               connectivityManager = get(),
               externalScope = get()
           )
       }
   }
   ```

This ensures:
 - Network monitoring survives configuration changes
 - Proper cleanup when app is terminated
 - No memory leaks from improper context binding
