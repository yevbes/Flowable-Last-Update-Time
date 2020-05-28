# Acme Last update time Flowable Core
## Abstract
En el Flowable existe la forma de consultar la información sobre la actualización de procesos y tareas.

In the Flowable there is a way to consult information about updating processes and tasks. But this information is updated in certain cases and is not propagated at the process level. For that reason a listener has been created to capture the desired events and also record the time at which the event has been captured. All relevant information is stored in the database table `flw_hi_last_update_time'.

## Database changes
PostgresSQL engine used in this example.
This project is required a table

```sql
CREATE TABLE flw_hi_last_update_time (
    id_ serial primary key,
    proc_id_ varchar(64) not null,
    last_update_time_ TIMESTAMP not null
);
```

## Diagram
This diagram represent different events captured by listener in different processes. Once event is captured listener automatically stores the date in the `flw_hi_last_update_time` table.

Note that in the database table, **only the insert operation is performed**.

![Diagram](/acme-lastupdatetime-app/src/main/resources/diagram/diagram.svg)

## Housekeeping
In this project it's not implemented but it necessary to delete old events.

## Buffering
With many inserts at the same time the performance can drop significantly. For that reason it is proposed to keep events in memory using the LIFO stack approach. If after a certain time no events is detected, the last event entered is registered and the stack is cleaned.

## Event types captured
Timer, Activity, Process, Task, Variables, Entity

## Event analysis
Events that used in the listener.

| Event | Relevant | Notes |
|---|---|---|
| `ENGINE_CREATED` | NO | Event is captured at the engine level. |
| `ENGINE_CLOSED` | NO | Event is captured at the engine level. |
| `PROCESS_CREATED` | YES |  |
| `PROCESS_STARTED` | YES |  |
| `PROCESS_COMPLETED` | YES |  |
| `PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT` | YES |  |
| `PROCESS_CANCELLED` | YES |  |
| `MEMBERSHIP_CREATED` | NO |  |
| `MEMBERSHIP_DELETED` | NO |  |
| `MEMBERSHIPS_DELETED` | NO |  |
| `ACTIVITY_STARTED` | YES |  |
| `ACTIVITY_COMPLETED` | YES |  |
| `ACTIVITY_CANCELLED` | YES |  |
| `ACTIVITY_SIGNALED` | YES |  |
| `ACTIVITY_MESSAGE_RECEIVED` | YES |  |
| `ACTIVITY_MESSAGE_WAITING` | YES |  |
| `ACTIVITY_MESSAGE_CANCELLED` | YES |  |
| `ACTIVITY_ERROR_RECEIVED` | YES |  |
| `ACTIVITY_COMPENSATE` | YES |  |
| `UNCAUGHT_BPMN_ERROR` | NO |  |
| `MULTI_INSTANCE_ACTIVITY_STARTED` | YES |  |
| `MULTI_INSTANCE_ACTIVITY_COMPLETED` | YES |  |
| `MULTI_INSTANCE_ACTIVITY_CANCELLED` | YES |  |
| `TASK_ASSIGNED` | YES |  |
| `TASK_CREATED` | YES |  |
| `TASK_COMPLETED` | YES |  |
| `TASK_OWNER_CHANGED` | YES |  |
| `TASK_PRIORITY_CHANGED` | YES |  |
| `TASK_DUEDATE_CHANGED` | YES |  |
| `TASK_NAME_CHANGED` | YES |  |
| `VARIABLE_CREATED` | YES |  |
| `VARIABLE_UPDATED` | YES |  |
| `VARIABLE_DELETED` | YES |  |
| `ENTITY_CREATED` | YES |  |
| `ENTITY_INITIALIZED` | YES |  |
| `ENTITY_UPDATED` | YES |  |
| `ENTITY_DELETED` | YES |  |
| `ENTITY_SUSPENDED` | YES |  |
| `ENTITY_ACTIVATED` | YES |  |
| `JOB_EXECUTION_SUCCESS` | NO |  Event is captured at the engine level. It will affect other events. |
| `JOB_EXECUTION_FAILURE` | NO | Event is captured at the engine level. It will affect other events. |
| `JOB_RETRIES_DECREMENTED` | NO | Event is captured at the engine level. It will affect other events. |
| `JOB_CANCELED` | NO | Event is captured at the engine level. It will affect other events. |
| `TIMER_FIRED` | NO |  |
| `TIMER_SCHEDULED` | NO |  |

[More of events not included in the table.](https://flowable.com/open-source/docs/bpmn/ch03-Configuration/#supported-event-types)

## Setting up the needed Infrastructure
Please check the following links on how to setup the infrastructure for Flowable Core manually without
using Docker:

- [Postgres Database](https://flowable.com/open-source/docs/bpmn/ch03-Configuration/#database-configuration)
- [Flowable Applications](https://flowable.com/open-source/docs/bpmn/ch14-Applications/)

Do not forget to set the deployment and cluster address properties of the Flowable Applications to point to
`http://localhost:8090`.  
For an easier setup with help of Docker check chapter [Setting up the needed Infrastructure with Docker](#Setting up the needed Infrastructure with Docker).

## Starting the project
Afterwards you can start the Spring Boot application defined in `com.acme.lastupdatetime.AcmeLastUpdateTimeApplication`. In order to achieve this,
you can build the executable war file with maven and execute `java -jar` pointing to the built war or create a new IDE Run Configuration. 
The Flowable Core REST APIs will then be available at `http://localhost:8090`.

The built war file can also be deployed to a supported servlet container to be started with it.
You would then need to adapt the Flowable Applications properties to fit the chosen web context accordingly.

## Helpful links
After both docker-compose and the Spring Boot application are started, these are the links for the different applications:

- http://localhost:8090 - Flowable Core
- http://localhost:8091 - Flowable Modeler
- http://localhost:8092 - Flowable Admin
- http://localhost:8093 - Flowable IDM
- http://localhost:8094/flowable-task - Flowable Task (https://github.com/flowable/flowable-engine/issues/2026)

## Setting up the needed Infrastructure with Docker
A decent amount of memory is needed for your docker VM. At least 4 GB are suggested.
Navigate to `/docker`, then execute `docker-compose up`. Services such as Flowable Applications and Database will be started.

- Flowable Modeler is already preconfigured to deploy the models into the engine.
- Flowable Admin is already preconfigured to connect to the engine.
- Flowable IDM is already preconfigured to connect to the engine and to serve as authentication provider for the other applications.
- Flowable Task is already preconfigured to connect to the engine.

### Containers
If you need to recreate the containers, perform the following actions:
- Make sure the application and the docker containers are stopped.
- Execute `docker-compose down` inside the `/docker` directory. This will remove the created containers.

### Data
Data created by the database is stored in docker volume `data_db`.
This allows you to purge and recreate the containers without loosing any data.

If you need a clean state for the database just execute `docker-compose down -v`inside the `/docker` directory.
The volumes will be recreated as soon as you restart the containers.  
BE CAREFUL AS WITH THIS YOU WILL LOOSE ALL YOUR DATA STORED IN THE DATABASE!

## Sample users
| User | Login | Password |
| -------------| ------------- | ------------- |
| Test Administrator | admin | test |

## Change Log

### 0.0.1
- Initial commit