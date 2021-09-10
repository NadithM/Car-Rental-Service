# Read Me #

## Design Principles ##

1. Car-Rental-Serivce is currently working for below Types (I called it **MessageType**).
> * REGISTER_CAR("REGCAR", "Register Car")
> * REGISTER_USER("REGUSR", "Register User")
> * REGISTER_AVAILABILITY_CAR("REGAVLCAR", "Register Availability Car")
> * SEARCH_AVAILABLE_CARS("SRCAVLCAR", "Search Available Cars")
> * GET_CAR("GETCAR", "Retrieve Car")
> * GET_USER("GETUSR", "Retrieve User")
> * CREATE_ORDER("CRTORDVHI", "Create Order")
> * GET_ORDER("GETORDVHI", "Retrieve Order")


2. There's External directory where application configurations defined per environment, ( **\config\\** ). we can define
   different application-configurations based on the environment. Which profile to be activated taken by environment
   variable(PROFILE) default is "dev". 

3. **requestId** is a unique Id along the flow for a given request, we can identify request flow using that id in the
   logs. Also we can use **MessageType** also to support proper logging. then we can filter logs by messageType also.

4. I use the **DeferredResult** which is Callable for asynchronous request processing method to support the constant
   response time. If the request could not process within given time, request will time out. This is with the idea of if
   car rental service will have to call some other microservices(may for to deal with a payment gateway). then this async processing coming handy with this
   timeout. also, we can configure these timeouts based on the flow. Related Configuration below. general timeout will
   be default to every api if it's not specifically configured
    1. **rest.request.timeout.general -> TimeUnit ms**
    2. **rest.request.timeout.vehicle-search -> TimeUnit ms**
    3. **rest.request.timeout.order-vehicle -> TimeUnit ms**
   
   we can add timeout configurations as needed depend on the response time for each flow.

5. Main focus on flexibility to support change requests and new functionalities.

    1. System can support different vehicle type in the future adhering to principle in SOLID. There will be
       minimum changes to code.
       
    2. Search functionality is working for basic key-controls, I have extended search functionality not only to for specific date range & max rental price per hour, but also to support any parameters in the future for any given vehicle type adhering to SOLID principles.
        1. FilterParam.PARAM_VEHICLE_TYPE    -> we can search vehicles and filter based on different vehicle types
        2. FilterParam.PARAM_BRAND           -> we can search vehicles and filter based on different vehicle's brand
        3. FilterParam.PARAM_TYPE_OF_FUEL    -> we can search vehicles and filter based on different vehicle's fuel type
        4. FilterParam.PARAM_TRANSMISSION    -> we can search vehicles and filter based on different vehicle's transmission
        5. FilterParam.PARAM_FEATURES        -> we can search vehicles and filter based on different features of vehicle
        
        6. FilterParam.PARAM_AVAIL_DATE_RANGE   -> we can search vehicles and filter based on date range that user need the vehicle
        7. FilerParam.PARAM_MINIMUM_RATE_PER_HOUR  -> we can search vehicles and filter based on minimum rate for rental that user is confortable with
        8. FilterParam.PARAM_MAXIMUM_RATE_PER_HOUR -> we can search vehicles and filter based on maximum rate for rental that user is confortable with
        
        9. FilterParam.PARAM_CAR_BODY_CONFIGURATION -> we can search vehicles and filter based on car body type configurations. This only specific to vehicle type CAR

   
   we can extend search functionality for them extending **KeyControl.class, BasicVehicleHandler.class**
   by implementing abstract methods without changing the existing methods.
   
    3. **Search-Vehicles funtionality** works as follows,
  
        1. if a vehicle attributes contains single-valued fields (such as Brand,FuelType), corresponding key-control
           field values will act as OR operations.
           
        2. if a vehicle attributes contains multi-valued fields (such as Features), corresponding key-control field
           values will act as AND operations.
           
        3. if key-controls fields are empty in the search criteria, ANY value of vehicle's corresponding attribute, will
           be match for search.
           
    4. when we order the **Vehicle** for a given date range, those date Range will be black-out from availability. So won't
       be showing in the search criteria until it gets unblocked.


6. I'm using Spring integration to handle the overwhelming requests.it will queue all the request in a **LinkedQueue** until
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

7. All **@Transactional** methods are ideally should be atomic(if we introduced DB). should be careful order of retrieving
   records from different tables and order of saving them inside **@Transactional** Method in the future modification. otherwise Rollbacks,DeadLocks,
   should be handled on partial success or error scenarios properly.

8. based on the Server resources(No of Cores,based on CPU/IO intensive thread) and requirements we have to tweak these thread pool
   properties to get efficient thread pool configurations.

9. Added unit tests to one @Controller to cover certain aspects as the time permits.

10. Controller advice will handle client errors and server errors.
11. Once we register a **Vehicle** it will be in inactive state until we allocate availability. Inactive **Vehicle** is not showing in the search results.

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
