# MVP for Codulate test task

The task is to build simple zone validation service

## Run as Docker Compose build

Service consists of
1) Admin service - admin-service. Used for input Zone coordinates, store them in DB, and listening to ActiveMq broker.
2) Fake Map Api - map-api. Let's not invoke real Google Api Map API :) Fake Map Api service uses JTS Topology Suite,
which is good approximation for Google Map Api. It can create Polygon,
let's assume that this is equal to Zone from task description.
Then we can check if user input location point is inside this Polygon/Zone.
3) ActiveMq integraion broker.
4) Postgres database

## How to run.
Just input this commands in terminal.

1) cd ./admin-service
2) gradlew build
3) cd ../mapapi
4) gradlew build
5) cd ..
6) docker build --tag=admin-service:latest -f Dockerfile-Admin .
7) docker build --tag=map-api:latest -f Dockerfile-Map .
8) docker compose up -d


![Containers](https://user-images.githubusercontent.com/108343174/194169640-11985d17-c55d-4067-b111-3e0ed2a9eece.png)

##How to test

1) Open swagger UI http://127.0.0.1:18080/swagger-ui.html
![Swagger UI](https://user-images.githubusercontent.com/108343174/194169957-121ca7e5-43a1-41be-b723-c4b9785e127b.png)

For example, input Bermuda Triangle Zone coordinates array
```
[{"x": 25.774, "y": -80.19},
{"x": 18.466, "y":-66.118},
{"x": 32.321, "y": -64.757},
{"x": 25.774, "y": -80.19}]
```
Point in `{"x":26.45,  "y":-66.29}`

Point out `{"x":23.985, "y":-53.855}`

Albuqerque Zone coordinates array
```
[{"x": 37.00, "y": -109.05},
{"x": 36.93, "y": -103.07},
{"x": 32.08, "y": -102.98},
{"x": 32.15, "y": -109.00},
{"x": 37.00, "y": -109.05}]
```
Point in `{"x":34.54,  "y":-105.71}`

Point out `{"x":34.17, "y":-99.07}`

![Bermuda Triangle](https://user-images.githubusercontent.com/108343174/194170647-5da9e6c8-2114-49e0-8482-9891bcfdb1d2.png)

2) Open ActiveMq Admin UI http://127.0.0.1:8161/admin/send.jsp

Send for example point `{"x":26.45,  "y":-66.29}`, that is inside Bermuda triangle

![Send ActiveMq message](https://user-images.githubusercontent.com/108343174/194170831-87257269-8135-4586-b239-6b8a9f16fd98.png)

3) In MQTT Explorer check if violation message received

![Violation message](https://user-images.githubusercontent.com/108343174/194171115-72bf6cf3-3ab4-4d07-bcd2-697c95d241a4.png)


![MQTT Connection](https://user-images.githubusercontent.com/108343174/194171198-354f45ec-d381-4544-8f00-6b07f02f6577.png)