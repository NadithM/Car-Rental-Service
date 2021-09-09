# Read Me #

## Design Principles ##

1. There's External directory where application configurations defined per environment, ( **\config\\** ). we can define
   different application-configurations based on the environment. Which profile to be activated taken by environment
   variable(PROFILE) default is "dev".

2. **requestId** is a unique Id along the flow for a given request, we can identify request flow using that id in the
   logs. Also we can use MessageType also to support proper logging. then we can filter logs by messageType also.

3. I use the **DeferredResult** which is Callable for asynchronous request processing method to support the constant
   response time. If the request could not process within given time, request will time out. This is with the idea of if
   car rental service will have to call some other microservices. then this async processing coming handy with this
   timeout. also, we can configure these timeouts based on the flow. Related Configuration below. general timeout will
   be default to every api if it's not specifically configured
    1. **rest.request.timeout.general -> TimeUnit ms**
    2. **rest.request.timeout.vehicle-search -> TimeUnit ms**
    3. **rest.request.timeout.order-vehicle -> TimeUnit ms**

we can add timeout configuration as needed depends on the response time for each flow.

4. Main focus on flexibility to support change requests and new functionalities.

    1. System can support different vehicle type in the future based on open/close principle in SOLID. There will be
       minimum changes to code.
       
    2. Search functionality is working for basic key-controls
        1. FilterParam.PARAM_VEHICLE_TYPE
        2. FilterParam.PARAM_BRAND
        3. FilterParam.PARAM_TYPE_OF_FUEL
        4. FilterParam.PARAM_TRANSMISSION
        5. FilterParam.PARAM_FEATURES
        6. FilterParam.PARAM_AVAIL_DATE_RANGE
        7. FilerParam.PARAM_MINIMUM_RATE_PER_HOUR
        8. FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR

   and specific key-controls for given vehicle type (in the case of CAR,CarBodyConfiguration is specific key-control to
   CAR(FilterParam)). we can extend search functionality for them extending KeyControl.class, BasicVehicleHandler.class
   by implementing abstract methods without changing the existing methods.
   
    3. Search-Vehicles works as follows,
  
        1. if a vehicle attributes contains single-valued fields (such as Brand,FuelType), corresponding key-control
           field values will act as OR operations.
           
        2. if a vehicle attributes contains multi-valued fields (such as Features), corresponding key-control field
           values will act as AND operations.
           
        3. if key-controls fields are empty in the search criteria, ANY value of vehicle's corresponding attribute, will
           be match for search.
           
    4. when we order the Vehicle for a given DateRange, those date Range will be black-out from availability. So won't
       be showing in the search criteria until it gets unblocked.


5. I'm using Spring integration to handle the overwhelming requests.it will queue all the request in a LinkedQueue until
   it serve by the service class. we can tweak and configure Thread pool to support required load. there are separate
   thread pools which will use for processing happens in the service class. Related configuration are below.
    1. **taskExecutor.corePoolSize.searchOperation**
    2. **taskExecutor.maxPoolSize.searchOperation**
    3. **taskExecutor.keepAliveTime.searchOperation -> TimeUnit s**

    4. **taskExecutor.corePoolSize.orderOperation**
    5. **taskExecutor.maxPoolSize.orderOperation**
    6. **taskExecutor.keepAliveTime.orderOperation -> TimeUnit s**

    7. **taskExecutor.corePoolSize.userOperation**
    8. **taskExecutor.maxPoolSize.userOperation**
    9. **taskExecutor.keepAliveTime.userOperation -> TimeUnit s**

    10. **taskExecutor.corePoolSize.vehicleOperation**
    11. **taskExecutor.maxPoolSize.vehicleOperation**
    12. **taskExecutor.keepAliveTime.vehicleOperation -> TimeUnit s**

6. All @Transactional methods are ideally should be atomic(if we introduced DB). should be careful order of retrieving
   records from different tables and order of saving them inside @Transactional Method. otherwise Rollbacks,DeadLocks,
   should be handled on partial success or error scenarios properly.

7. based on the Server resources(No of Cores,based on CPU/IO intensive thread) and requirements we have to tweak these
   properties to get efficient thread pool configurations.

8. Added unit tests to one @Controller to cover certain aspects as the time permits

9. Controller advice will handle client errors and server errors.

## How to build and run ##

1. Set the following environment variable for active profile, if possible

> ***PROFILE***
>
possible values ("dev","qa","prod"). ***Default value is "dev"***

2. Go to the directory where *"POM.xml"* present.


3. Run These commands.

> *mvn clean install*

> *mvn spring-boot:run*

4. POSTMAN sample request collections can be taken from
   https://www.getpostman.com/collections/e2d572c366b143b07e79

### What's missing ###

1. Security (authentication and authorization) due to time constrains. Can use token base approach for users.
2. Not all rest request are validated by javax.
3. ideally all classes should have unit test.
4. 
