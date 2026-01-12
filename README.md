# Preloading the data included
There is a json data file included for the demo.
The data file and loading script is located in the `items` folder.
To load the data to mongodb, you need:
* A mongodb (cloud / self host version)
* A python runtime with virtual environment setup by
  ```shell
  python -m venv .venv
  source .venv/bin/activate
  pip install -r requirements.txt # install dependencies
  ```
* set the mongodb connection string and db name as environment variable
  ```shell
  export db_url={connection string for mongodb}
  export db_name={the mongodb name}
  ```
* run the `upload.py` to insert or reinsert the data
  ```shell
  python upload.py
  ```


# Architecture
The project is a pure springboot application.
The application 
* provide functionality to do searching on the flight schedule in mongodb
* use spring web mvc as the base web framework for RESTAPI
* contains 3 level of cache mechanism
  * L2 cache with cache to mongodb accessing layer, preventing duplicating mongodb query with in single request
    * L2 cache normally built-in on the Hibernate framework, but not existing in the spring-data-mongodb, I build a own CacheWrapper
    * Please see the `EnableL2Cache.class`
  * Global Cache with the standard `Spring Cache` component to do a application level cache
    * The cache is designed to support both local cache for development and test, please see `EnableCaffeineCache.class`
    * The cache is designed to support global (cross server with `redis`) for production usage on reducing frequent computation for request, please see `EnableGlobalCache.class`
  * ETag Cache with both `Caffeine` and `Redis` option, on reducing unnecessary network traffic
    * The ETag is computed and stored after digesting the request response body and attached to `ETag` header
    * The ETag is natively supported by main stream browser, which is effectively reducing redundant network traffic and boots up the SPA loading speed
    * Please see `EnableEtagCacheRedis.class` and `EnableETagCacheCaffeine.class`


# Build
The project pure maven project, to build and test the project by standard maven command.

## Test Project
```shell
mvn test
```

## Compile Project
```shell
mvn clean compile
```

## Build Jar
After execute the command, a jar with name `demo-{version}.jar` is built.
The jar can be executed directly by command `java -Dspring.profiles.active={dev|prod} -jar demo-{version}.jar `
```shell
mvn clean package
```

# Execution
But for execution, we need to
* define the run profile of springboot (options of `dev`, `prod}`, can be passed in java command, see the `Dockerfile` script for detail)
* define the mongodb url for data connection (`export DB_URL={redis connection}`)
* defind the redis url (`export REDIS_URL={redis connection}`)
  * optional, if running in dev env or single node, can use `caffeine` cache instead

# 
```shell
# For production usage with redis as cache layer
java -Dspring.profiles.active=prod -jar demo-{version}.jar

# For development usage with caffeine as cache layer
java -Dspring.profiles.active=dev -jar demo-{version}.jar
```

# Deployment
The project deployed as docker image.
The image can be deployed as container based application.

## Set up env variable
Two variable is required to be passed to the build stage.

Before execute `docker build` command, we first setup variable
* image_name <- the name of image to be build
* CORS_URL   <- the target accessing app (normal the frontend app)
  * The file `src/main/java/me/xethh/libs/web/Const.java` will be edited to change the CORS_URL
  * By default, for development, the value is `http://localhost:5173` (the react application local url)
  * assume the frontend app is deployed on www.abc.com, the CORS_URL should https://www.abc.com

```shell
export image_name="{frontend-image}"
export CORS_URL="{CORS_URL}"
```
## Build
After the setup the environment variables, execute below commands to:
* build a docker image locally on the dev machine
* push the container image to the container image repository (e.g. dockerhub)

```shell
docker build --build-arg CORS_URL="$CORS_URL" -t $image_name .
docker push $image_name
```
