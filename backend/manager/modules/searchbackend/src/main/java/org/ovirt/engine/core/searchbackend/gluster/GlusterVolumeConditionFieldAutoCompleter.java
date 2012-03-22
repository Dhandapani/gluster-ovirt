package org.ovirt.engine.core.searchbackend.gluster;

import java.util.ArrayList;
import java.util.List;

import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeStatus;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeType;
import org.ovirt.engine.core.common.businessentities.gluster.TransportType;
import org.ovirt.engine.core.searchbackend.BaseConditionFieldAutoCompleter;
import org.ovirt.engine.core.searchbackend.EnumValueAutoCompleter;
import org.ovirt.engine.core.searchbackend.IAutoCompleter;
import org.ovirt.engine.core.searchbackend.IConditionValueAutoCompleter;
import org.ovirt.engine.core.searchbackend.NumericConditionRelationAutoCompleter;
import org.ovirt.engine.core.searchbackend.StringConditionRelationAutoCompleter;

/**
 * Auto completer for conditions on Gluster Volumes. Volumes can be filtered on following fields:<br>
 * [field] - [type] - [column name in {@code gluster_volumes} table]<br>
 * <li>name - {@link String} - vol_type<br>
 * <li>type - {@link GlusterVolumeType} - vol_type<br>
 * <li>transport_type - {@link TransportType} - transport_type<br>
 * <li>replica_count - {@link Integer} - replica_count<br>
 * <li>stripe_count - {@link Integer} - stripe_count<br>
 * <li>status - {@link GlusterVolumeStatus} - status<br>
 */
public class GlusterVolumeConditionFieldAutoCompleter extends BaseConditionFieldAutoCompleter {
    public final static GlusterVolumeConditionFieldAutoCompleter INSTANCE = new GlusterVolumeConditionFieldAutoCompleter();

    private enum FIELDS {
        NAME,
        TYPE,
        TRANSPORT_TYPE,
        REPLICA_COUNT,
        STRIPE_COUNT,
        STATUS
    };

    private static final List<AutoCompletionField> fields = createAutoCompletionFields();

    private static List<AutoCompletionField> createAutoCompletionFields() {
        List<AutoCompletionField> fields = new ArrayList<AutoCompletionField>();

        addField(FIELDS.NAME.toString(), String.class, "vol_name");
        addField(FIELDS.TYPE.toString(), GlusterVolumeType.class, "vol_type");
        addField(FIELDS.TRANSPORT_TYPE.toString(), TransportType.class, "transport_type");
        addField(FIELDS.REPLICA_COUNT.toString(), Integer.class, "replica_count");
        addField(FIELDS.STRIPE_COUNT.toString(), Integer.class, "stripe_count");
        addField(FIELDS.STATUS.toString(), GlusterVolumeStatus.class, "status");

        return fields;
    }

    private GlusterVolumeConditionFieldAutoCompleter() {
        super();
        buildDictionaries();
    }

    @Override
    public IAutoCompleter getFieldRelationshipAutoCompleter(String fieldName) {
        try {
            switch (FIELDS.valueOf(fieldName)) {
            case NAME:
            case TYPE:
            case TRANSPORT_TYPE:
            case STATUS:
                return StringConditionRelationAutoCompleter.INSTANCE;
            case REPLICA_COUNT:
            case STRIPE_COUNT:
                return NumericConditionRelationAutoCompleter.INSTANCE;
            default:
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IConditionValueAutoCompleter getFieldValueAutoCompleter(String fieldName) {
        try {
            switch (FIELDS.valueOf(fieldName)) {
            case TYPE:
                return new EnumValueAutoCompleter(GlusterVolumeType.class);
            case TRANSPORT_TYPE:
                return new EnumValueAutoCompleter(TransportType.class);
            case STATUS:
                return new EnumValueAutoCompleter(GlusterVolumeStatus.class);
            default:
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static void addField(String fieldName, Class<?> fieldType, String columnName) {
        fields.add(new AutoCompletionField(fieldName, fieldName, fieldType, columnName));
    }

    private void buildDictionaries() {
        // Build the field name auto completion dictionary
        for (AutoCompletionField field : fields) {
            mVerbs.put(field.fieldName, field.displayName);
        }
        buildCompletions();

        // Build the field type dictionary
        for (AutoCompletionField field : fields) {
            getTypeDictionary().put(field.fieldName, field.fieldType);
        }

        // Build the column name dictionary
        for (AutoCompletionField field : fields) {
            mColumnNameDict.put(field.fieldName, field.columnName);
        }

        // Build the validation dictionary
        buildBasicValidationTable();
    }

    private static final class AutoCompletionField {
        protected final String fieldName;
        protected final String displayName;
        protected final Class<?> fieldType;
        protected final String columnName;

        public AutoCompletionField(String fieldName, String displayName, Class<?> fieldType, String columnName) {
            this.fieldName = fieldName;
            this.displayName = displayName;
            this.fieldType = fieldType;
            this.columnName = columnName;
        }
    }
}
