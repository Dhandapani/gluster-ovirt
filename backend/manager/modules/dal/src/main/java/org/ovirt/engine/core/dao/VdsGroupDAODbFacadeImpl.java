package org.ovirt.engine.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.ovirt.engine.core.common.businessentities.ActionGroup;
import org.ovirt.engine.core.common.businessentities.HypervisorType;
import org.ovirt.engine.core.common.businessentities.MigrateOnErrorOptions;
import org.ovirt.engine.core.common.businessentities.VDSGroup;
import org.ovirt.engine.core.common.businessentities.VdsSelectionAlgorithm;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.compat.NGuid;
import org.ovirt.engine.core.compat.Version;
import org.ovirt.engine.core.dal.dbbroker.DbFacadeUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * <code>VdsGroupDAODbFacadeImpl</code> provides an implementation of {@link VdsGroupDAO} that uses code previously
 * found in {@link org.ovirt.engine.core.dal.dbbroker.DbFacade}.
 *
 */
@SuppressWarnings("synthetic-access")
public class VdsGroupDAODbFacadeImpl extends BaseDAODbFacade implements VdsGroupDAO {
    private static final ParameterizedRowMapper<VDSGroup> vdsGroupRowMapper = new VdsGroupRowMapper();

    @Override
    public VDSGroup get(Guid id) {
        return get(id, null, false);
    }

    @Override
    public VDSGroup get(Guid id, Guid userID, boolean isFiltered) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vds_group_id", id).addValue("user_id", userID).addValue("is_filtered", isFiltered);

        return getCallsHandler().executeRead("GetVdsGroupByVdsGroupId", vdsGroupRowMapper, parameterSource);
    }

    @Override
    public VDSGroup getWithRunningVms(Guid id) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vds_group_id", id);
        return getCallsHandler().executeRead("GetVdsGroupWithRunningVms", vdsGroupRowMapper, parameterSource);
    }

    @Override
    public VDSGroup getByName(String name) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vds_group_name", name);

        return (VDSGroup) DbFacadeUtils.asSingleResult(
                getCallsHandler().executeReadList("GetVdsGroupByVdsGroupName", vdsGroupRowMapper, parameterSource));
    }

    @Override
    public List<VDSGroup> getAllForStoragePool(Guid id) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("storage_pool_id", id);

        return getCallsHandler().executeReadList("GetVdsGroupsByStoragePoolId", vdsGroupRowMapper, parameterSource);
    }

    @Override
    public List<VDSGroup> getAllWithQuery(String query) {
        return new SimpleJdbcTemplate(jdbcTemplate).query(query, vdsGroupRowMapper);
    }

    @Override
    public List<VDSGroup> getAll() {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource();
        return getCallsHandler().executeReadList("GetAllFromVdsGroups", vdsGroupRowMapper, parameterSource);
    }

    @Override
    public void save(VDSGroup group) {
        Map<String, Object> dbResults = getCallsHandler().executeModification("InsertVdsGroups", getVdsGroupParamSource(group));

        group.setvds_group_id(new Guid(dbResults.get("vds_group_id").toString()));
    }

    @Override
    public void update(VDSGroup group) {
        getCallsHandler().executeModification("UpdateVdsGroup", getVdsGroupParamSource(group));
    }

    @Override
    public void remove(Guid id) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("vds_group_id", id);

        getCallsHandler().executeModification("DeleteVdsGroup", parameterSource);
    }

    @Override
    public List<VDSGroup> getClustersWithPermittedAction(Guid userId, ActionGroup actionGroup) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
                .addValue("user_id", userId).addValue("action_group_id", actionGroup.getId());

        return getCallsHandler().executeReadList("fn_perms_get_vds_groups_with_permitted_action",
                vdsGroupRowMapper,
                parameterSource);
    }

    private MapSqlParameterSource getVdsGroupParamSource(VDSGroup group) {
        MapSqlParameterSource parameterSource = getCustomMapSqlParameterSource()
        .addValue("description", group.getdescription())
        .addValue("name", group.getname())
        .addValue("vds_group_id", group.getId())
        .addValue("cpu_name", group.getcpu_name())
        .addValue("selection_algorithm", group.getselection_algorithm())
        .addValue("high_utilization", group.gethigh_utilization())
        .addValue("low_utilization", group.getlow_utilization())
        .addValue("cpu_over_commit_duration_minutes",
                group.getcpu_over_commit_duration_minutes())
        .addValue("hypervisor_type", group.gethypervisor_type())
        .addValue("storage_pool_id", group.getstorage_pool_id())
        .addValue("max_vds_memory_over_commit",
                group.getmax_vds_memory_over_commit())
        .addValue("transparent_hugepages",
                group.getTransparentHugepages())
        .addValue("compatibility_version",
                group.getcompatibility_version())
        .addValue("migrate_on_error", group.getMigrateOnError())
        .addValue("virt_service", group.supportsVirtService())
        .addValue("gluster_service", group.supportsGlusterService());
        return parameterSource;
    }

    private final static class VdsGroupRowMapper implements ParameterizedRowMapper<VDSGroup> {
        @Override
        public VDSGroup mapRow(ResultSet rs, int rowNum)
                throws SQLException {
            VDSGroup entity = new VDSGroup();
            entity.setdescription(rs.getString("description"));
            entity.setname(rs.getString("name"));
            entity.setId(Guid.createGuidFromString(rs
                    .getString("vds_group_id")));
            entity.setcpu_name(rs.getString("cpu_name"));
            entity.setselection_algorithm(VdsSelectionAlgorithm.forValue(rs
                    .getInt("selection_algorithm")));
            entity.sethigh_utilization(rs.getInt("high_utilization"));
            entity.setlow_utilization(rs.getInt("low_utilization"));
            entity.setcpu_over_commit_duration_minutes(rs
                    .getInt("cpu_over_commit_duration_minutes"));
            entity.sethypervisor_type(HypervisorType.forValue(rs
                    .getInt("hypervisor_type")));
            entity.setstorage_pool_id(NGuid.createGuidFromString(rs
                    .getString("storage_pool_id")));
            entity.setmax_vds_memory_over_commit(rs
                    .getInt("max_vds_memory_over_commit"));
            entity.setTransparentHugepages(rs
                    .getBoolean("transparent_hugepages"));
            entity.setcompatibility_version(new Version(rs
                    .getString("compatibility_version")));
            entity.setMigrateOnError(MigrateOnErrorOptions.forValue(rs.getInt("migrate_on_error")));
            entity.setVirtService(rs.getBoolean("virt_service"));
            entity.setGlusterService(rs.getBoolean("gluster_service"));
            return entity;
        }
    };

}
