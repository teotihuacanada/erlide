package org.ttb.integration.utils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;
import org.ttb.integration.Activator;
import org.ttb.integration.TraceBackend;
import org.ttb.integration.TracingStatus;

/**
 * Class that contains helper methods for handling tracing status
 * 
 * @author Piotr Dorobisz
 * @see TracingStatus
 * 
 */
public final class TracingStatusHandler {

    private TracingStatusHandler() {
    }

    /**
     * Displays dialog with further details about status (e.g. reason of error).
     * 
     * @param status
     *            status
     */
    public static void handleStatus(TracingStatus status) {
        Status executionStatus = null;
        switch (status) {
        case ERROR:
            Object errorObject = TraceBackend.getInstance().getErrorObject();
            executionStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error: " + errorObject, null);
            break;
        case EXCEPTION_THROWN:
            Exception e = (Exception) TraceBackend.getInstance().getErrorObject();
            executionStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error", e);
            break;
        case NO_ACTIVATED_NODES:
            executionStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "No nodes were activated for tracing", null);
            break;
        case NOT_ALL_NODES_ACTIVATED:
            StringBuilder builder = new StringBuilder("Following nodes were not activated for tracing:\n");
            for (String node : TraceBackend.getInstance().getNotActivatedNodes()) {
                builder.append(node).append("\n");
            }
            executionStatus = new Status(IStatus.WARNING, Activator.PLUGIN_ID, builder.toString(), null);
            break;
        case EMPTY:
            executionStatus = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "No data to display", null);
            break;
        case OK:
            break;
        }
        if (executionStatus != null)
            StatusManager.getManager().handle(executionStatus, StatusManager.SHOW);
    }
}