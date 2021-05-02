package org.slf4j.helpers;

/**
 * @author itning
 * @since 2021/5/2 19:11
 */
@SuppressWarnings("all")
public class Util {
    private Util() {
    }

    public static String safeGetSystemProperty(String key) {
        if (key == null)
            throw new IllegalArgumentException("null input");

        String result = null;
        try {
            result = System.getProperty(key);
        } catch (java.lang.SecurityException sm) {
            ; // ignore
        }
        return result;
    }

    public static boolean safeGetBooleanSystemProperty(String key) {
        String value = safeGetSystemProperty(key);
        if (value == null)
            return false;
        else
            return value.equalsIgnoreCase("true");
    }

    /**
     * In order to call {@link SecurityManager#getClassContext()}, which is a
     * protected method, we add this wrapper which allows the method to be visible
     * inside this package.
     */
    private static final class ClassContextSecurityManager extends SecurityManager {
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }

    private static ClassContextSecurityManager SECURITY_MANAGER;
    private static boolean SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = false;

    private static ClassContextSecurityManager getSecurityManager() {
        if (SECURITY_MANAGER != null)
            return SECURITY_MANAGER;
        else if (SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED)
            return null;
        else {
            SECURITY_MANAGER = safeCreateSecurityManager();
            SECURITY_MANAGER_CREATION_ALREADY_ATTEMPTED = true;
            return SECURITY_MANAGER;
        }
    }

    private static ClassContextSecurityManager safeCreateSecurityManager() {
        try {
            return new ClassContextSecurityManager();
        } catch (java.lang.SecurityException sm) {
            return null;
        }
    }

    /**
     * Returns the name of the class which called the invoking method.
     *
     * @return the name of the class which called the invoking method.
     */
    public static Class<?> getCallingClass() {
        ClassContextSecurityManager securityManager = getSecurityManager();
        if (securityManager == null)
            return null;
        Class<?>[] trace = securityManager.getClassContext();
        String thisClassName = Util.class.getName();

        // Advance until Util is found
        int i;
        for (i = 0; i < trace.length; i++) {
            if (thisClassName.equals(trace[i].getName()))
                break;
        }

        // trace[i] = Util; trace[i+1] = caller; trace[i+2] = caller's caller
        if (i >= trace.length || i + 2 >= trace.length) {
            throw new IllegalStateException("Failed to find org.slf4j.helpers.Util or its caller in the stack; " + "this should not happen");
        }

        return trace[i + 2];
    }

    static final public void report(String msg, Throwable t) {
        System.err.println(msg);
        System.err.println("Reported exception:");
        t.printStackTrace();
    }

    static final public void report(String msg) {
        //  System.err.println("SLF4J: " + msg);
    }
}
