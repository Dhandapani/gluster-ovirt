package org.ovirt.engine.core.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public final class VdcEventNotificationUtils {
    private static final Map<EventNotificationEntity, HashSet<AuditLogType>> _eventNotificationTypeMap =
            new HashMap<EventNotificationEntity, HashSet<AuditLogType>>();

    /**
     * Initializes the <see cref="VdcEventNotificationUtils"/> class.
     */
    static {
        // VDC
        AddEventNotificationEntry(EventNotificationEntity.Engine, AuditLogType.VDC_STOP);
        // VDS
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.VDS_FAILURE);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.USER_VDS_MAINTENANCE);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.USER_VDS_MAINTENANCE_MIGRATION_FAILED);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.VDS_ACTIVATE_FAILED);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.VDS_RECOVER_FAILED);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.VDS_APPROVE_FAILED);
        AddEventNotificationEntry(EventNotificationEntity.Host, AuditLogType.VDS_INSTALL_FAILED);
        // VM
        AddEventNotificationEntry(EventNotificationEntity.Vm, AuditLogType.VM_FAILURE);
        AddEventNotificationEntry(EventNotificationEntity.Vm, AuditLogType.VM_MIGRATION_START);
        AddEventNotificationEntry(EventNotificationEntity.Vm, AuditLogType.VM_MIGRATION_FAILED);
        AddEventNotificationEntry(EventNotificationEntity.Vm, AuditLogType.VM_NOT_RESPONDING);
        // IRS
        AddEventNotificationEntry(EventNotificationEntity.Storage, AuditLogType.VDS_SLOW_STORAGE_RESPONSE_TIME);
        AddEventNotificationEntry(EventNotificationEntity.Storage, AuditLogType.IRS_FAILURE);
        AddEventNotificationEntry(EventNotificationEntity.Storage, AuditLogType.IRS_DISK_SPACE_LOW);
        AddEventNotificationEntry(EventNotificationEntity.Storage, AuditLogType.IRS_DISK_SPACE_LOW_ERROR);
    }

    /**
     * Gets all notification events.
     *
     * @return
     */
    public static Map<EventNotificationEntity, HashSet<AuditLogType>> GetNotificationEvents() {
        return _eventNotificationTypeMap;
    }

    /**
     * Gets notification events by type.
     *
     * @param type
     *            The type.
     * @return
     */
    public static Map<EventNotificationEntity, HashSet<AuditLogType>> GetNotificationEventsByType(
            EventNotificationEntity type) {
        Map<EventNotificationEntity, HashSet<AuditLogType>> map =
                new HashMap<EventNotificationEntity, HashSet<AuditLogType>>();
        if (_eventNotificationTypeMap.containsKey(type)) {
            map.put(type, _eventNotificationTypeMap.get(type));
        }
        return map;
    }

    /**
     * Adds an event notification entry.
     *
     * @param entity
     *            The entity.
     * @param auditLogType
     *            Type of the audit log.
     */
    private static void AddEventNotificationEntry(EventNotificationEntity entity, AuditLogType auditLogType) {
        HashSet<AuditLogType> entry;
        if (!_eventNotificationTypeMap.containsKey(entity)) {
            _eventNotificationTypeMap.put(entity, new HashSet<AuditLogType>());
            entry = _eventNotificationTypeMap.get(entity);
        } else {
            entry = _eventNotificationTypeMap.get(entity);
        }
        if (!entry.contains(auditLogType)) {
            entry.add(auditLogType);
        }
        _eventNotificationTypeMap.put(entity, entry);
    }

}
