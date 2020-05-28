package com.acme.lastupdatetime.listeners;

import org.flowable.common.engine.api.delegate.event.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

/**
 * @author Ievgenii Bespal
 */
public class LastUpdateTimeEventListener implements FlowableEventListener {
    private final static Logger log = LoggerFactory.getLogger(LastUpdateTimeEventListener.class);
    protected final DataSource dataSource;

    protected final String SQL_INSERT_DATA = "" +
            "INSERT INTO FLW_HI_LAST_UPDATE_TIME (PROC_ID_, LAST_UPDATE_TIME_) " +
            "VALUES (?,?);" +
            "";

    public LastUpdateTimeEventListener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onEvent(FlowableEvent event) {
        log.info("Event type: {} received.",event.getType().name());

        String processInstanceId = null;
        FlowableEngineEvent flowableEngineEvent = (FlowableEngineEvent) event;
        FlowableEngineEventType eventType = (FlowableEngineEventType) event.getType();

        switch (eventType) {
            // ACTIVITY
            case ACTIVITY_STARTED:
            case ACTIVITY_COMPLETED:
            case ACTIVITY_CANCELLED:
            case ACTIVITY_SIGNALED:
            case ACTIVITY_MESSAGE_RECEIVED:
            case ACTIVITY_MESSAGE_WAITING:
            case ACTIVITY_MESSAGE_CANCELLED:
            case ACTIVITY_ERROR_RECEIVED:
            case ACTIVITY_COMPENSATE:

            // MULTI INSTANCE ACTIVITY
            case MULTI_INSTANCE_ACTIVITY_STARTED:
            case MULTI_INSTANCE_ACTIVITY_COMPLETED:
            case MULTI_INSTANCE_ACTIVITY_CANCELLED:

            // PROCESS
            case PROCESS_CREATED:
            case PROCESS_CANCELLED:
            case PROCESS_STARTED:
            case PROCESS_COMPLETED:
            case PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT:

            // TASK
            case TASK_ASSIGNED:
            case TASK_CREATED:
            case TASK_COMPLETED:
            case TASK_OWNER_CHANGED:
            case TASK_DUEDATE_CHANGED:
            case TASK_PRIORITY_CHANGED:
            case TASK_NAME_CHANGED:

            // VARIABLES
            case VARIABLE_CREATED:
            case VARIABLE_UPDATED:
            case VARIABLE_DELETED:

            // ENTITY
            case ENTITY_CREATED:
            case ENTITY_INITIALIZED:
            case ENTITY_UPDATED:
            case ENTITY_DELETED:
            case ENTITY_ACTIVATED:
            case ENTITY_SUSPENDED:
                processInstanceId = flowableEngineEvent.getProcessInstanceId();
                break;
        }

        // Persist data into database
        if (processInstanceId != null) {
            persistDataIntoDatabase(
                    processInstanceId,
                    new Timestamp(DateTime.now().getMillis())
            );
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }

    private void persistDataIntoDatabase(String processInstanceId, Timestamp timestamp) {
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement(SQL_INSERT_DATA, Statement.RETURN_GENERATED_KEYS);
        ) {
            preparedStatement.setString(1, processInstanceId);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
