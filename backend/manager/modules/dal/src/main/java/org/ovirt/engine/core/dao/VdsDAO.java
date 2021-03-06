package org.ovirt.engine.core.dao;

import java.util.List;

import org.ovirt.engine.core.common.businessentities.VDS;
import org.ovirt.engine.core.common.businessentities.VDSType;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.compat.NGuid;

/**
 * <code>VdsDAO</code> defines a type that performs CRUD operations on instances of {@link VDS}.
 *
 *
 */
public interface VdsDAO extends DAO, SearchDAO<VDS>, AutoRecoverDAO<VDS> {
    /**
     * Retrieves the instance with the given id.
     *
     * @param id
     *            the id
     * @return the VDS instance
     */
    VDS get(NGuid id);

    /**
     * Retrieves the instance with the given id, with optional permission filtering.
     *
     * @param id
     *            the id
     * @param userID
     *            the ID of the user requesting the information
     * @param isFiltered
     *            Whether the results should be filtered according to the user's permissions
     * @return the VDS instance
     */
    VDS get(NGuid id, Guid userID, boolean isFiltered);

    /**
     * Finds all instances with the given name.
     *
     * @param name
     *            the name
     * @return the list of instances
     */
    List<VDS> getAllWithName(String name);

    /**
     * Finds all instances for the given host.
     *
     * @param hostname
     *            the hostname
     * @return the list of instances
     */
    List<VDS> getAllForHostname(String hostname);

    /**
     * Retrieves all instances with the given address.
     *
     * @param address
     *            the address
     * @return the list of instances
     */
    List<VDS> getAllWithIpAddress(String address);

    /**
     * Retrieves all instances with the given unique id.
     *
     * @param id
     *            the unique id
     * @return the list of instances
     */
    List<VDS> getAllWithUniqueId(String id);

    /**
     * Retrieves all instances for the specified type.
     *
     * @param vds
     *            the type
     * @return the list of instances
     */
    List<VDS> getAllOfType(VDSType vds);

    /**
     * Retrieves all instances for the given list of types.
     *
     * @param types
     *            the type filter
     * @return the list of instances
     */
    List<VDS> getAllOfTypes(VDSType[] types);

    /**
     * Retrieves all instances by group id.
     *
     * @param vdsGroup
     *            the group id
     * @return the list of instances
     */
    List<VDS> getAllForVdsGroupWithoutMigrating(Guid vdsGroup);

    /**
     * Retrieves all VDS instances.
     *
     * @return the list of VDS instances
     */
    List<VDS> getAll();

    /**
     * Retrieves all VDS instances by vds group id (cluster ID)
     *
     * @param vdsGroup
     * @return the list of VDS instances
     */
    List<VDS> getAllForVdsGroup(Guid vdsGroup);

    /**
     * Retrieves all VDS instances in the given Storage Pool, that are in status "UP"
     * ordered by their vds_spm_priority, not including -1
     * @return the list of VDS instances
     */
    List<VDS> getListForSpmSelection(Guid storagePoolId);

}
