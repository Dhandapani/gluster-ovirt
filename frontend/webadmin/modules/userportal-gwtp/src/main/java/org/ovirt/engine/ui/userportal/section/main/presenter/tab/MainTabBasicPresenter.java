package org.ovirt.engine.ui.userportal.section.main.presenter.tab;

import org.ovirt.engine.ui.common.auth.LoggedInGatekeeper;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalBasicListModel;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalItemModel;
import org.ovirt.engine.ui.userportal.gin.ClientGinjector;
import org.ovirt.engine.ui.userportal.place.ApplicationPlaces;
import org.ovirt.engine.ui.userportal.section.main.presenter.AbstractModelActivationPresenter;
import org.ovirt.engine.ui.userportal.section.main.presenter.MainTabPanelPresenter;
import org.ovirt.engine.ui.userportal.section.main.presenter.tab.basic.MainTabBasicDetailsPresenterWidget;
import org.ovirt.engine.ui.userportal.section.main.presenter.tab.basic.MainTabBasicListPresenterWidget;
import org.ovirt.engine.ui.userportal.uicommon.model.UserPortalModelInitEvent;
import org.ovirt.engine.ui.userportal.uicommon.model.UserPortalModelInitEvent.UserPortalModelInitHandler;
import org.ovirt.engine.ui.userportal.uicommon.model.basic.UserPortalBasicListProvider;
import org.ovirt.engine.ui.userportal.utils.ConnectAutomaticallyManager;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class MainTabBasicPresenter extends AbstractModelActivationPresenter<UserPortalItemModel, UserPortalBasicListModel, MainTabBasicPresenter.ViewDef, MainTabBasicPresenter.ProxyDef> {

    @ProxyCodeSplit
    @NameToken(ApplicationPlaces.basicMainTabPlace)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface ProxyDef extends TabContentProxyPlace<MainTabBasicPresenter> {
    }

    public interface ViewDef extends View {

        HasClickHandlers getRefreshButton();

    }

    @TabInfo(container = MainTabPanelPresenter.class)
    static TabData getTabData(ClientGinjector ginjector) {
        return new TabDataBasic(ginjector.getApplicationConstants().basicMainTabLabel(), 0);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_VmListContent = new Type<RevealContentHandler<?>>();

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_VmDetailsContent = new Type<RevealContentHandler<?>>();

    private final MainTabBasicListPresenterWidget vmList;
    private final MainTabBasicDetailsPresenterWidget vmDetails;

    @Inject
    public MainTabBasicPresenter(EventBus eventBus,
            ViewDef view,
            ProxyDef proxy,
            MainTabBasicListPresenterWidget vmList,
            MainTabBasicDetailsPresenterWidget vmDetails,
            final UserPortalBasicListProvider provider,
            final ConnectAutomaticallyManager connectAutomaticallyManager) {
        super(eventBus, view, proxy, provider);
        this.vmList = vmList;
        this.vmDetails = vmDetails;

        connectAutomaticallyManager.registerModel(provider.getModel());
        getEventBus().addHandler(UserPortalModelInitEvent.getType(), new UserPortalModelInitHandler() {

            @Override
            public void onUserPortalModelInit(UserPortalModelInitEvent event) {
                connectAutomaticallyManager.unregisterModels();
                connectAutomaticallyManager.registerModel(provider.getModel());
            }

        });
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainTabPanelPresenter.TYPE_SetTabContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_VmListContent, vmList);
        setInSlot(TYPE_VmDetailsContent, vmDetails);

    }

}
