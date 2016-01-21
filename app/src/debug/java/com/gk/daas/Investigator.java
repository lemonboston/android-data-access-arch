package com.gk.daas;

import android.util.Log;

/**
 * Simplifies adding ad hoc, tracking logs to code during investigation, bug fixing.<br/>
 * <br/>
 * For tracking down asynchronous events, lifecycles simply adding <code>Investigator.log(this)</code> to every checkpoint will usually do.
 * (Set {@link Investigator.Settings#NO_OF_EXTRA_METHOD_DEPTH_LOGGED} to 1 to see who is calling the watched method.)<br/>
 * <br/>
 * The varargs param can be used to print variable values.<br/>
 * <br/>
 * Printing elapsed time is also possible, see more at the methods.<br/>
 *
 * @author Gabor_Keszthelyi
 */
public class Investigator {

    /**
     * Configuration settings for the Investigator. See the javadocs of the constants for more.
     */
    public static class Settings {

        /**
         * Number of the extra elements (class + method name) logged from the stacktrace created at the log() call. 0 means no extra method is logged, only the watched one.<br/>
         * (It is exposed so it can be changed at individual checkpoints if needed.)
         */
        public static int NO_OF_EXTRA_METHOD_DEPTH_LOGGED = 0;

        /**
         * If true, the name of the thread is printed at the beginning of the log message.
         */
        static final boolean LOG_THREAD_NAME = true;

        static final String TAG = "Investigator";

        static final int LOG_LEVEL = Log.DEBUG;

        /**
         * If true, the package name from the instance's toString value is removed for easier readability of the logs.
         */
        static final boolean REMOVE_PACKAGE_NAME = true;

        /**
         * When enabled, an extra word ({@link Settings#INNER_CLASS_HIGHLIGHTING_WORD}) is inserted into anonymous and nested inner class toString values to help notice them more easily.
         * <br/>e.g.: <code>FirstFragment$1@1bf1abe3.onClick()</code> --> <code>FirstFragment_INNNER_1@1bf1abe3.onClick()</code>
         */
        static final boolean HIGHLIGHT_INNER_CLASSES = true;
        static final String INNER_CLASS_HIGHLIGHTING_WORD = "_INNER_";

    }

    /**
     * Logs the calling instance and method name, plus the variable names and values if provided.
     * <br/><br/>Examples:
     * <br/><code>D/investigation﹕ HomeActivity@788dc5c.onCreate()</code>
     * <br/><code>D/investigation﹕ HomeActivity@788dc5c.onCreate() | someVariable = someVariableValue</code>
     *
     * @param instance               the calling object instance (use as <code>Investigator.log(this)</code>)
     * @param variableNamesAndValues variable name and value pairs. eg: <code>Investigator.log(this, "firstVariableName", firstVariable, "secondVariableName", secondVariable);</code>
     */
    public static void log(Object instance, Object... variableNamesAndValues) {
        InvestigatorImpl.log(instance, variableNamesAndValues);
    }

    /**
     * Starts an internal stopwatch, so consequent log calls will print the time elapsed since calling this method. Calling it multiple times will just restart the stopwatch.
     * * <br/><br/>Example:<br/><code>D/investigation﹕ HomeActivity@788dc5c.onResume() | 36 ms</code>
     *
     * @param instance the calling object instance (use as <code>Investigator.startStopWatch(this)</code>)
     */
    public static void startStopWatch(Object instance) {
        InvestigatorImpl.startStopWatch(instance);
    }

    /**
     * Tells Investigator to stop logging times (started by {@link Investigator#startStopWatch(Object)} from this point on.
     */
    public static void stopLoggingTimes() {
        InvestigatorImpl.stopLoggingTimes();
    }
}

/**
 * The implementation of {@link Investigator} is moved here, so {@link Investigator} can be kept clean similarly to an interface while keeping the methods static.
 */
@SuppressWarnings("PointlessBooleanExpression")
class InvestigatorImpl {

    private static final int STACKTRACE_INDEX_OF_CALLING_METHOD = 3; // fixed value, need to update only if the 'location' of the stack trace getting code changes
    private static final String INNER_CLASS_TOSTRING_SYMBOL = "$";

    private static boolean hasStopWatchBeenStarted;

    // "Implements" Investigator.log()
    static void log(Object instance, Object... variableNamesAndValues) {
        StackTraceElement[] stackTrace = getStackTrace();

        StringBuilder message = new StringBuilder();

        if (Investigator.Settings.LOG_THREAD_NAME) {
            message.append(threadName());
        }

        message.append(instanceAndMethodName(instance, stackTrace));

        if (variableNamesAndValues != null) {
            message.append(variablesMessage(variableNamesAndValues));
        }

        if (hasStopWatchBeenStarted) {
            message.append(timeElapsedMessage());
        }

        if (Investigator.Settings.NO_OF_EXTRA_METHOD_DEPTH_LOGGED > 0) {
            message.append(extraMethodLines(stackTrace));
        }

        log(message);
    }

    // "Implements" Investigator.startStopWatch()
    static void startStopWatch(Object instance) {
        StackTraceElement[] stackTrace = getStackTrace();

        StringBuilder message = new StringBuilder();

        if (Investigator.Settings.LOG_THREAD_NAME) {
            message.append(threadName());
        }

        message.append(instanceAndMethodName(instance, stackTrace));

        message.append(" | 0 ms (STOPWATCH STARTED)");

        if (Investigator.Settings.NO_OF_EXTRA_METHOD_DEPTH_LOGGED > 0) {
            message.append(extraMethodLines(stackTrace));
        }

        log(message);

        StopWatch.startStopWatch();
        hasStopWatchBeenStarted = true;
    }

    // "Implements" Investigator.stopLoggingTimes()
    static void stopLoggingTimes() {
        hasStopWatchBeenStarted = false;
    }

    private static void log(StringBuilder message) {
        Log.println(Investigator.Settings.LOG_LEVEL, Investigator.Settings.TAG, message.toString());
    }

    private static StackTraceElement[] getStackTrace() {
        // How to get the stacktrace and why this code is better than Thread.currentThread().getStackTrace():
        // http://stackoverflow.com/q/421280/4247460
        // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=6375302
        return new Exception().getStackTrace();
    }

    private static String threadName() {
        return String.format("[%s] ", Thread.currentThread().getName());
    }

    private static String instanceAndMethodName(Object instance, StackTraceElement[] stackTrace) {
        String methodName = stackTrace[STACKTRACE_INDEX_OF_CALLING_METHOD].getMethodName();
        String instanceName = instance.toString();
        if (Investigator.Settings.REMOVE_PACKAGE_NAME) {
            instanceName = removePackageName(instanceName);
        }
        if (Investigator.Settings.HIGHLIGHT_INNER_CLASSES && isInnerClass(instanceName)) {
            instanceName = insertInnerClassHighlight(instanceName);
        }
        return String.format("%s.%s()", instanceName, methodName);
    }

    private static String removePackageName(String instanceName) {
        return instanceName.substring(instanceName.lastIndexOf(".") + 1);
    }

    private static boolean isInnerClass(String instanceName) {
        return instanceName.contains(INNER_CLASS_TOSTRING_SYMBOL);
    }

    private static String insertInnerClassHighlight(String instanceName) {
        int insertionLocation = instanceName.indexOf(INNER_CLASS_TOSTRING_SYMBOL);
        return instanceName.substring(0, insertionLocation) + Investigator.Settings.INNER_CLASS_HIGHLIGHTING_WORD + instanceName.substring(insertionLocation + 1);
    }

    private static StringBuilder variablesMessage(Object... variableNamesAndValues) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < variableNamesAndValues.length; i++) {
            Object variableName = variableNamesAndValues[i];
            Object variableValue = variableNamesAndValues[++i]; // will fail on odd number of params deliberately
            String variableMessage = String.format(" | %s = %s", variableName, variableValue);
            result.append(variableMessage);
        }
        return result;
    }

    private static StringBuilder extraMethodLines(StackTraceElement[] stackTrace) {
        StringBuilder extraLines = new StringBuilder();
        for (int i = STACKTRACE_INDEX_OF_CALLING_METHOD + 1;
             i <= STACKTRACE_INDEX_OF_CALLING_METHOD + Investigator.Settings.NO_OF_EXTRA_METHOD_DEPTH_LOGGED && i < stackTrace.length;
             i++) {
            extraLines.append(createExtraLine(stackTrace[i]));
        }
        return extraLines;
    }

    private static String createExtraLine(StackTraceElement stackTraceElement) {
        String className = stackTraceElement.getClassName();
        if (Investigator.Settings.HIGHLIGHT_INNER_CLASSES && isInnerClass(className)) {
            className = insertInnerClassHighlight(className);
        }
        return String.format("\n...%s.%s()", className, stackTraceElement.getMethodName());
    }

    private static String timeElapsedMessage() {
        return String.format(" | %d ms", StopWatch.getElapsedTimeInMillis());
    }
}

class StopWatch {

    private static long startTimeInMillis;

    static void startStopWatch() {
        startTimeInMillis = System.currentTimeMillis();
    }

    static long getElapsedTimeInMillis() {
        return System.currentTimeMillis() - startTimeInMillis;
    }

}