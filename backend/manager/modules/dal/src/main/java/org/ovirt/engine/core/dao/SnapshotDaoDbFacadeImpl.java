package org.ovirt.engine.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.ovirt.engine.core.common.businessentities.Snapshot;
import org.ovirt.engine.core.common.businessentities.Snapshot.SnapshotStatus;
import org.ovirt.engine.core.common.businessentities.Snapshot.SnapshotType;
import org.ovirt.engine.core.common.utils.EnumUtils;
import org.ovirt.engine.core.compat.Guid;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class SnapshotDaoDbFacadeImpl extends DefaultGenericDaoDbFacade<Snapshot, Guid> implements SnapshotDao {

    private static final ParameterizedRowMapper<Snapshot> ROW_MAPPER = new SnapshotRowMapper();

    private static final ParameterizedRowMapper<Snapshot> NO_CONFIG_ROW_MAPPER =
            new SnapshotRowMapperWithoutConfiguration();

    @Override
    protected String getProcedureNameForUpdate() {
        return "UpdateSnapshot";
    }

    @Override
    protected String getProcedureNameForGet() {
        return "GetSnapshotBySnapshotId";
    }

    @Override
    protected String getProcedureNameForGetAll() {
        return "GetAllFromSnapshots";
    }

    @Override
    protected String getProcedureNameForSave() {
        return "InsertSnapshot";
    }

    @Override
    protected String getProcedureNameForRemove() {
        return "DeleteSnapshot";
    }

    @Override
    protected MapSqlParameterSource createIdParameterMapper(Guid id) {
        return getCustomMapSqlParameterSource().addValue("snapshot_id", id);
    }

    @Override
    protected MapSqlParameterSource createFullParametersMapper(Snapshot entity) {
        return createIdParameterMapper(entity.getId())
                .addValue("vm_id", entity.getVmId())
                .addValue("snapshot_type", EnumUtils.nameOrNull(entity.getType()))
                .addValue("status", EnumUtils.nameOrNull(entity.getStatus()))
                .addValue("description", entity.getDescription())
                .addValue("creation_date", entity.getCreationDate())
                .addValue("app_list", entity.getAppList())
                .addValue("vm_configuration", entity.getVmConfiguration());
    }

    @Override
    protected ParameterizedRowMapper<Snapshot> createEntityRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    public void updateStatus(Guid id, SnapshotStatus status) {
        MapSqlParameterSource parameterSource = createIdParameterMapper(id)
                .addValue("status", EnumUtils.nameOrNull(status));
        getCallsHandler().executeModification("UpdateSnapshotStatus", parameterSource);
    }

    @Override
    public void updateId(Guid snapshotId, Guid newSnapshotId) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("snapshot_id", snapshotId)
                .addValue("new_snapshot_id", newSnapshotId);
        getCallsHandler().executeModification("UpdateSnapshotId", parameterSource);
    }

    @Override
    public Guid getId(Guid vmId, SnapshotType type) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vm_id", vmId)
                .addValue("snapshot_type", EnumUtils.nameOrNull(type));

        return getCallsHandler().executeRead("GetSnapshotIdsByVmIdAndType",
                createGuidMapper(),
                parameterSource);
    }

    @Override
    public Guid getId(Guid vmId, SnapshotType type, SnapshotStatus status) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vm_id", vmId)
                .addValue("snapshot_type", EnumUtils.nameOrNull(type))
                .addValue("status", EnumUtils.nameOrNull(status));

        return getCallsHandler().executeRead("GetSnapshotIdsByVmIdAndTypeAndStatus",
                createGuidMapper(),
                parameterSource);
    }

    @Override
    public List<Snapshot> getAll(Guid vmId) {
        return getAll(vmId, null, false);
    }

    @Override
    public List<Snapshot> getAll(Guid vmId, Guid userId, boolean isFiltered) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource().addValue("vm_id", vmId)
                .addValue("user_id", userId)
                .addValue("is_filtered", isFiltered);

        return getCallsHandler().executeReadList("GetAllFromSnapshotsByVmId", NO_CONFIG_ROW_MAPPER, parameterSource);
    }

    @Override
    public boolean exists(Guid vmId, SnapshotType type) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vm_id", vmId)
                .addValue("snapshot_type", EnumUtils.nameOrNull(type));

        return getCallsHandler().executeRead("CheckIfSnapshotExistsByVmIdAndType",
                createBooleanMapper(),
                parameterSource);
    }

    @Override
    public boolean exists(Guid vmId, SnapshotStatus status) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vm_id", vmId)
                .addValue("status", EnumUtils.nameOrNull(status));

        return getCallsHandler().executeRead("CheckIfSnapshotExistsByVmIdAndStatus",
                createBooleanMapper(),
                parameterSource);
    }

    @Override
    public boolean exists(Guid vmId, Guid snapshotId) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vm_id", vmId)
                .addValue("snapshot_id", snapshotId);

        return getCallsHandler().executeRead("CheckIfSnapshotExistsByVmIdAndSnapshotId",
                createBooleanMapper(),
                parameterSource);
    }

    private static class SnapshotRowMapper implements ParameterizedRowMapper<Snapshot> {

        @Override
        public Snapshot mapRow(ResultSet rs, int rowNum) throws SQLException {
            Snapshot snapshot = createInitialSnapshotEntity(rs);

            mapPropertiesWithoutConfiguration(rs, snapshot);
            mapConfiguration(rs, snapshot);

            return snapshot;
        }

        protected Snapshot createInitialSnapshotEntity(ResultSet rs) throws SQLException {
            return new Snapshot();
        }

        private void mapPropertiesWithoutConfiguration(ResultSet rs, Snapshot snapshot) throws SQLException {
            snapshot.setId(Guid.createGuidFromString(rs.getString("snapshot_id")));
            snapshot.setVmId(new Guid(rs.getString("vm_id")));
            snapshot.setType(SnapshotType.valueOf(rs.getString("snapshot_type")));
            snapshot.setStatus(SnapshotStatus.valueOf(rs.getString("status")));
            snapshot.setDescription(rs.getString("description"));
            snapshot.setCreationDate(new Date(rs.getTimestamp("creation_date").getTime()));
            snapshot.setAppList(rs.getString("app_list"));
        }

        protected void mapConfiguration(ResultSet rs, Snapshot snapshot) throws SQLException {
            snapshot.setVmConfiguration(rs.getString("vm_configuration"));
        }
    }

    /**
     * Mapper that will not map the {@link Snapshot#getVmConfiguration()} field, but instead will map the
     * {@link Snapshot#isVmConfigurationAvailable()} field.
     */
    private static class SnapshotRowMapperWithoutConfiguration extends SnapshotRowMapper {

        @Override
        protected Snapshot createInitialSnapshotEntity(ResultSet rs) throws SQLException {
            return new Snapshot(rs.getBoolean("vm_configuration_available"));
        }

        /**
         * Nothing to map, since the read only field was mapped when entity was created.
         */
        @Override
        protected void mapConfiguration(ResultSet rs, Snapshot snapshot) throws SQLException {
        }
    }

    @Override
    public Snapshot get(Guid id) {
        return get(id, null, false);
    }

    @Override
    public Snapshot get(Guid id, Guid userId, boolean isFiltered) {
        MapSqlParameterSource parameterSource = createIdParameterMapper(id)
                .addValue("user_id", userId)
                .addValue("is_filtered", isFiltered);
        return getCallsHandler().executeRead(getProcedureNameForGet(), createEntityRowMapper(), parameterSource);
    }
}
