package org.ovirt.engine.ui.webadmin.section.main.presenter.tab.gluster;

import org.ovirt.engine.core.common.businessentities.gluster.GlusterVolumeEntity;
import org.ovirt.engine.ui.common.presenter.AbstractSubTabPresenter;
import org.ovirt.engine.ui.common.uicommon.model.DetailModelProvider;
import org.ovirt.engine.ui.common.widget.tab.ModelBoundTabData;
import org.ovirt.engine.ui.uicommonweb.models.gluster.VolumeGeneralModel;
import org.ovirt.engine.ui.uicommonweb.models.volumes.VolumeListModel;
import org.ovirt.engine.ui.webadmin.gin.ClientGinjector;
import org.ovirt.engine.ui.webadmin.place.ApplicationPlaces;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.VolumeSelectionChangeEvent;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SubTabVolumeGeneralPresenter extends AbstractSubTabPresenter<GlusterVolumeEntity, VolumeListModel, VolumeGeneralModel, SubTabVolumeGeneralPresenter.ViewDef, SubTabVolumeGeneralPresenter.ProxyDef> {

    @TabInfo(container = VolumeSubTabPanelPresenter.class)
    static TabData getTabData(ClientGinjector ginjector) {
        return new ModelBoundTabData(ginjector.getApplicationConstants().volumeGeneralSubTabLabel(), 0,
                ginjector.getSubTabVolumeGeneralModelProvider());
    }

    @Inject
    public SubTabVolumeGeneralPresenter(
            EventBus eventBus,
            ViewDef view,
            ProxyDef proxy,
            PlaceManager placeManager,
            DetailModelProvider<VolumeListModel, VolumeGeneralModel> modelProvider) {
        super(eventBus, view, proxy, placeManager, modelProvider);
    }

    @ProxyCodeSplit
    @NameToken(ApplicationPlaces.volumeGeneralSubTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<SubTabVolumeGeneralPresenter> {
    }

    public interface ViewDef extends AbstractSubTabPresenter.ViewDef<GlusterVolumeEntity> {
    }

    @Override
    protected PlaceRequest getMainTabRequest() {
        return new PlaceRequest(ApplicationPlaces.volumeMainTabPlace);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, VolumeSubTabPanelPresenter.TYPE_SetTabContent, this);
    }

    @ProxyEvent
    public void onVolumeSelectionChange(VolumeSelectionChangeEvent event) {
        updateMainTabSelection(event.getSelectedItems());
    }
}
