package org.ovirt.engine.core.bll.storage;

import java.util.Collections;
import java.util.List;

import org.ovirt.engine.core.bll.MultiLevelAdministrationHandler;
import org.ovirt.engine.core.common.PermissionSubject;
import org.ovirt.engine.core.common.VdcObjectType;
import org.ovirt.engine.core.common.action.StorageServerConnectionParametersBase;
import org.ovirt.engine.core.common.businessentities.storage_server_connections;

public abstract class StorageServerConnectionCommandBase<T extends StorageServerConnectionParametersBase> extends
        StorageHandlingCommandBase<T> {
    public StorageServerConnectionCommandBase(T parameters) {
        super(parameters);
        setVdsId(parameters.getVdsId());
    }

    protected storage_server_connections getConnection() {
        return getParameters().getStorageServerConnection();
    }

    @Override
    public List<PermissionSubject> getPermissionCheckSubjects() {
        return Collections.singletonList(new PermissionSubject(MultiLevelAdministrationHandler.SYSTEM_OBJECT_ID,
                VdcObjectType.System,
                getActionType().getActionGroup()));
    }
}
