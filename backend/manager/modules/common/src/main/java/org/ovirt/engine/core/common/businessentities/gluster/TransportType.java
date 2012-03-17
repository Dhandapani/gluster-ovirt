package org.ovirt.engine.core.common.businessentities.gluster;

/**
 * Enum of transport types supported by Gluster Volumes
 * @see GlusterVolumeEntity
 */
public enum TransportType {
    ETHERNET,
    INFINIBAND;

    public int getValue() {
        return ordinal();
    }

    public static TransportType forValue(int ordinal) {
        return values()[ordinal];
    }
}
