# RRES (Retrofit-RxJava-EventBus-Service) Architecture Demo App #

This is a Android sample app for showing the implementation of 
 the following __Networking / Data Access architecture__ using concrete use-cases.
 
## Architecture ##
__Retrofit__ with __RxJava__ for handling the REST API calls.  
__EventBus__ to deliver the results and updates back to the presentation layer.  
Rx features to handle errors, chained or parallel calls, fallback to offline store, and more.  
The whole component is placed in a __Android Service__ which starts on-demand and stops after some idle time when there are no more ongoing tasks.
  
  
## List of use-cases implemented  ##

- Basic use-case
    - Single network call __executed on background thread__, deliver results to UI on main thread
    - __Handle screen rotation__ (configuration change): deliver result on rotated screen without restarting the call
    - __Display loading indicator__ during the call
    - Can deliver result to different screen (user navigates to a different screen during the call)
- Error handling
    - __Error message__ displayed if something went wrong. __Different messages for different errors__.    
    - _Implementation_:  
    Errors propagate through the Rx chain and interpreted at single place at the end.  
    Error event is sent to presentation layer where message is decided.
- Ongoing call handling
    - There is only one call running at a time, __initiating the call again is ignored__.
    - _Implementation_:  
    Rx Subscription reference is kept and checked if subscribed.
- Offline storage
    - When online, network call is executed and result is saved and displayed
    - __When offline, data is loaded from offline store__
    - _Implementation_:  
    Using Rx operators _doOnSuccess_ and _onErrorResumeNext_ are used to construct the appropriate chain.
- Retry
    - __Retries the call if it fails__
    - _Implementation_:  
    Using Rx operator _RetryWhen_
- Cancelling
    - The ongoing __network call can be cancelled__
    - _Implementation_:  
    Retrofit supports cancelling the call down to the network layer through the returned Observable/Single\'s _unsubscribe()_ method.
- Double load
    - __Show offline data first__ if available but __load from server too and refresh the displayed value__.
    - Execute the operations in parallel.
    - _Implementation_:  
    By combining the two Rx Observables with operator _concatEager_.
- Parallel and Chained calls
    - User initiates a complex data access, receives the end result on the UI.
    - Requires __multiple network calls__. Both __parallel and sequential executions__ might be needed.
    - Shows __dialog about the progress steps__.
    - _Implementation_:  
    Rx operator _zip_ for parallel execution and _flatMap_ for sequential execution.  
    Progress update events can be sent to the presentation layer at any point in Rx chain.
- Background sync
    - Scheduled __background data sync__ with server. Even when the app is not running or used by the user.
    - _Implementation_:  
    The Service used for on-demand data access can be started up for syncing too.  
    (Scheduling is implemented simply with AlarmManager in this PoC but JobScheduler or GcmNetworkManager is a better solution in a real app).
- HTTP Cache
    - Cache the network responses according to their _Cache-Control_ headers
    - _Implementation_:  
    Retrofit backed by OkHttp can take care of the caching, just has to be configured.  
    (Need to be un-commented in code to enable it.)


