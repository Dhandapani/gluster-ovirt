package org.ovirt.engine.ui.userportal.section.main.view.tab;

import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.view.AbstractView;
import org.ovirt.engine.ui.common.widget.refresh.RefreshPanel;
import org.ovirt.engine.ui.common.widget.refresh.SimpleRefreshManager;
import org.ovirt.engine.ui.userportal.section.main.presenter.tab.MainTabBasicPresenter;
import org.ovirt.engine.ui.userportal.uicommon.model.basic.UserPortalBasicListProvider;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class MainTabBasicView extends AbstractView implements MainTabBasicPresenter.ViewDef {

    interface ViewUiBinder extends UiBinder<Widget, MainTabBasicView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    @UiField
    Panel vmListPanel;

    @UiField
    Panel vmDetailPanel;

    @UiField(provided = true)
    RefreshPanel refreshPanel;

    private SimpleRefreshManager refreshManager;

    @Inject
    public MainTabBasicView(UserPortalBasicListProvider modelProvider, EventBus eventBus, ClientStorage clientStorage) {
        this.refreshManager = new SimpleRefreshManager(modelProvider, eventBus, clientStorage);
        this.refreshPanel = refreshManager.getRefreshPanel();

        initWidget(ViewUiBinder.uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == MainTabBasicPresenter.TYPE_VmListContent) {
            setPanelContent(vmListPanel, content);
        } else if (slot == MainTabBasicPresenter.TYPE_VmDetailsContent) {
            setPanelContent(vmDetailPanel, content);
        } else {
            super.setInSlot(slot, content);
        }
    }

    @Override
    public HasClickHandlers getRefreshButton() {
        return refreshPanel;
    }

}
