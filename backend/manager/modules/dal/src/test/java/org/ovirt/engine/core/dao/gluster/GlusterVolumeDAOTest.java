package org.ovirt.engine.core.dao.gluster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.ovirt.engine.core.common.businessentities.VdsStatic;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterBrickEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterBrickStatus;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeEntity;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeStatus;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeType;
import org.ovirt.engine.core.common.businessentities.gluster.TransportType;
import org.ovirt.engine.core.compat.Guid;
import org.ovirt.engine.core.dao.BaseDAOTestCase;

public class GlusterVolumeDAOTest extends BaseDAOTestCase {
    private GlusterVolumeDAO dao;
    private static final Guid HOST_ID = new Guid("afce7a39-8e8c-4819-ba9c-796d316592e6");
    private static final Guid CLUSTER_ID = new Guid("0e57070e-2469-4b38-84a2-f111aaabd49d");
    private VdsStatic host;
    private GlusterVolumeEntity volume;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dao = dbFacade.getGlusterVolumeDAO();
        host = dbFacade.getVdsStaticDAO().get(HOST_ID);
        volume = insertTestVolume();
    }

    public GlusterVolumeDAOTest() throws Exception {
        setUp();
    }

    @Test
    public void testGlusterVolumeInsert() throws Exception {
        GlusterVolumeEntity volumeEntity = dao.getById(volume.getId());
        assertNotNull(volumeEntity);
        assertEquals(volumeEntity, volume);
    }

    @Test
    public void testGlusterVolumeStatusChange() throws Exception {
        volume.setStatus(GlusterVolumeStatus.OFFLINE);
        dao.updateVolumeStatus(volume.getId(), GlusterVolumeStatus.OFFLINE);
        GlusterVolumeEntity volumeEntity = dao.getById(volume.getId());
        assertNotNull(volumeEntity);
        assertEquals(volumeEntity, volume);
    }

    private GlusterVolumeEntity insertTestVolume() {
        Guid volumeId = Guid.NewGuid();

        GlusterVolumeEntity volume = new GlusterVolumeEntity();
        volume.setName("testVol1");
        volume.setClusterId(CLUSTER_ID);
        volume.setId(volumeId);
        volume.setVolumeType(GlusterVolumeType.DISTRIBUTE);
        volume.setTransportType(TransportType.ETHERNET);
        volume.setReplicaCount(0);
        volume.setStripeCount(0);
        volume.setStatus(GlusterVolumeStatus.ONLINE);
        volume.setOption("auth.allow", "*");
        volume.setAccessProtocols("GLUSTER,NFS");

        GlusterBrickEntity brick =
                new GlusterBrickEntity(volumeId, host, "/export/testVol1", GlusterBrickStatus.ONLINE);
        volume.addBrick(brick);

        dao.save(volume);
        return volume;
    }
}
