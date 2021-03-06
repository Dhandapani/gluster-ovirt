package org.ovirt.engine.core.utils.serialization.json;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeInfo.As;
import org.codehaus.jackson.annotate.JsonTypeInfo.Id;
import org.ovirt.engine.core.common.action.AddVmFromTemplateParameters;
import org.ovirt.engine.core.common.businessentities.DiskImageBase;
import org.ovirt.engine.core.common.queries.ValueObjectMap;

@SuppressWarnings("serial")
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY)
public abstract class JsonAddVmFromTemplateParametersMixIn extends AddVmFromTemplateParameters {

    @JsonIgnore
    @Override
    public abstract ValueObjectMap getSerializedDiskInfoList();

    @JsonIgnore
    @Override
    public abstract Map<String, DiskImageBase> getDiskInfoList();
}
