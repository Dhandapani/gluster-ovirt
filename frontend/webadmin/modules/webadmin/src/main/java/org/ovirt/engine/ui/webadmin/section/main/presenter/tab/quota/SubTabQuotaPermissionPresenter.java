package org.ovirt.engine.ui.webadmin.section.main.presenter.tab.quota;

import org.ovirt.engine.core.common.businessentities.Quota;
import org.ovirt.engine.core.common.businessentities.permissions;
import org.ovirt.engine.ui.common.presenter.AbstractSubTabPresenter;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.common.widget.tab.ModelBoundTabData;
import org.ovirt.engine.ui.uicommonweb.models.qouta.QuotaListModel;
import org.ovirt.engine.ui.uicommonweb.models.qouta.QuotaPermissionListModel;
import org.ovirt.engine.ui.webadmin.gin.ClientGinjector;
import org.ovirt.engine.ui.webadmin.place.ApplicationPlaces;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.QuotaSelectionChangeEvent;

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

public class SubTabQuotaPermissionPresenter extends AbstractSubTabPresenter<Quota, QuotaListModel, QuotaPermissionListModel, SubTabQuotaPermissionPresenter.ViewDef, SubTabQuotaPermissionPresenter.ProxyDef> {

    @ProxyCodeSplit
    @NameToken(ApplicationPlaces.quotaPermissionSubTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<SubTabQuotaPermissionPresenter> {
    }

    public interface ViewDef extends AbstractSubTabPresenter.ViewDef<Quota> {
    }

    @TabInfo(container = QuotaSubTabPanelPresenter.class)
    static TabData getTabData(ClientGinjector ginjector) {
        return new ModelBoundTabData(ginjector.getApplicationConstants().quotaPermissionSubTabLabel(), 4,
                ginjector.getSubTabQuotaPermissionModelProvider());
    }

    @Inject
    public SubTabQuotaPermissionPresenter(EventBus eventBus, ViewDef view, ProxyDef proxy,
            PlaceManager placeManager,
            SearchableDetailModelProvider<permissions, QuotaListModel, QuotaPermissionListModel> modelProvider) {
        super(eventBus, view, proxy, placeManager, modelProvider);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, QuotaSubTabPanelPresenter.TYPE_SetTabContent, this);
    }

    @Override
    protected PlaceRequest getMainTabRequest() {
        return new PlaceRequest(ApplicationPlaces.quotaMainTabPlace);
    }

    @ProxyEvent
    public void onQuotaSelectionChange(QuotaSelectionChangeEvent event) {
        updateMainTabSelection(event.getSelectedItems());
    }

}
