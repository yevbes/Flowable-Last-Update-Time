# Acme Last update time Flowable Core
## Abstract


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

## Event types captured
Timer, Activity, Process, Task, Variables, Entity

## Event analysis
Events that used in the listener.

| Event | Relevant | Notes |
|---|---|---|
| `PROCESS_CANCELLED` | YES | Process event - A process has been cancelled. |
| `PROCESS_COMPLETED` | YES | Process event - A process has been completed. |
| `ACTIVITY_STARTED` | YES | Activity event - An activity is starting to execute. |
| `ACTIVITY_COMPLETED` | YES | Activity event - An activity has been completed successfully. |
| `ACTIVITY_CANCELLED` | YES | Activity event - An activity has been cancelled because of boundary event. |
| `TASK_ASSIGNED` | YES | Task event - A task has been assigned. |
| `TASK_CREATED` | YES | Task event - A task has been created. | 
| `TASK_COMPLETED` | YES | Task event - A task has been completed. |
| `TASK_OWNER_CHANGED` | YES | Task event - A task owner has been changed. |
| `TASK_DUEDATE_CHANGED` | YES | Task event - A task dueDate has been changed. |
| `TASK_PRIORITY_CHANGED` | YES | Task event - A task priority has been changed. |
| `TASK_NAME_CHANGED` | YES | Task event - A task name has been changed. |
| `VARIABLE_CREATED` | YES | Variable event - A new variable has been created. |
| `VARIABLE_UPDATED` | YES | Variable event - An existing variable has been updated. |
| `VARIABLE_DELETED` | YES | Variable event - An existing variable has been deleted. |
| `ENTITY_CREATED` | YES | Entity event - New entity is created. |
| `ENTITY_INITIALIZED` | YES | Entity event - New entity has been created and all child-entities that are created as a result of the creation of this particular entity are also created and initialized. |
| `ENTITY_UPDATED` | YES | Entity event - Existing entity is updated. |
| `ENTITY_DELETED` | YES | Entity event - Existing entity is deleted. |
| `ENTITY_ACTIVATED` | YES | Entity event - Existing entity has been activated. |
| `ENTITY_SUSPENDED` | YES | Entity event - Existing entity has been suspended. |
| `TIMER_FIRED` | YES | Timer event - Timer has been fired successfully. |
| `TIMER_SCHEDULED` | YES | Timer event - A Timer has been scheduled. |

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