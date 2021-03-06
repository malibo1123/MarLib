package com.mar.lib.log.slf4j;

import com.mar.lib.log.slf4j.helpers.BasicMarkerFactory;
import com.mar.lib.log.slf4j.helpers.Util;
import com.mar.lib.log.slf4j.impl.StaticMarkerBinder;

/**
 * MarkerFactory is a utility class producing {@link Marker} instances as
 * appropriate for the logging system currently in use.
 * 
 * <p>
 * This class is essentially implemented as a wrapper around an
 * {@link IMarkerFactory} instance bound at compile time.
 * 
 * <p>
 * Please note that all methods in this class are static.
 * 
 * @author Ceki G&uuml;lc&uuml;
 */
public class MarkerFactory {
    static IMarkerFactory MARKER_FACTORY;

    private MarkerFactory() {
    }

    /**
     * As of SLF4J version 1.7.14, StaticMarkerBinder classes shipping in various bindings
     * come with a getSingleton() method. Previously only a public field called SINGLETON 
     * was available.
     * 
     * @return IMarkerFactory
     * @throws NoClassDefFoundError in case no binding is available
     * @since 1.7.14
     */
    private static IMarkerFactory bwCompatibleGetMarkerFactoryFromBinder() throws NoClassDefFoundError {
        try {
            return StaticMarkerBinder.getSingleton().getMarkerFactory();
        } catch (NoSuchMethodError nsme) {
            // binding is probably a version of SLF4J older than 1.7.14
            return StaticMarkerBinder.SINGLETON.getMarkerFactory();
        }
    }

    // this is where the binding happens
    static {
        try {
            MARKER_FACTORY = bwCompatibleGetMarkerFactoryFromBinder();
        } catch (NoClassDefFoundError e) {
            MARKER_FACTORY = new BasicMarkerFactory();
        } catch (Exception e) {
            // we should never get here
            Util.report("Unexpected failure while binding MarkerFactory", e);
        }
    }

    /**
     * Return a Marker instance as specified by the name parameter using the
     * previously bound {@link IMarkerFactory}instance.
     * 
     * @param name
     *          The name of the {@link Marker} object to return.
     * @return marker
     */
    public static Marker getMarker(String name) {
        return MARKER_FACTORY.getMarker(name);
    }

    /**
     * Create a marker which is detached (even at birth) from the MarkerFactory.
     *
     * @param name the name of the marker
     * @return a dangling marker
     * @since 1.5.1
     */
    public static Marker getDetachedMarker(String name) {
        return MARKER_FACTORY.getDetachedMarker(name);
    }

    /**
     * Return the {@link IMarkerFactory}instance in use.
     * 
     * <p>The IMarkerFactory instance is usually bound with this class at 
     * compile time.
     * 
     * @return the IMarkerFactory instance in use
     */
    public static IMarkerFactory getIMarkerFactory() {
        return MARKER_FACTORY;
    }
}