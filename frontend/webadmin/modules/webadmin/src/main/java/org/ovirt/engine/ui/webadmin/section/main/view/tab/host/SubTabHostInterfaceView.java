package org.ovirt.engine.ui.webadmin.section.main.view.tab.host;

import javax.inject.Inject;

import org.ovirt.engine.core.common.businessentities.VDS;
import org.ovirt.engine.ui.common.SubTableResources;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.idhandler.WithElementId;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.common.view.AbstractSubTabFormView;
import org.ovirt.engine.ui.common.widget.table.SimpleActionTable;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.uicommonweb.models.hosts.HostInterfaceLineModel;
import org.ovirt.engine.ui.uicommonweb.models.hosts.HostInterfaceListModel;
import org.ovirt.engine.ui.uicommonweb.models.hosts.HostListModel;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.host.SubTabHostInterfacePresenter;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminButtonDefinition;
import org.ovirt.engine.ui.webadmin.widget.host.HostInterfaceForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SubTabHostInterfaceView extends AbstractSubTabFormView<VDS, HostListModel, HostInterfaceListModel>
        implements SubTabHostInterfacePresenter.ViewDef {

    interface ViewIdHandler extends ElementIdHandler<SubTabHostInterfaceView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    /**
     * An empty column, used to render Host NIC table header.
     */
    private class EmptyColumn extends TextColumn<HostInterfaceLineModel> {
        @Override
        public String getValue(HostInterfaceLineModel object) {
            return null;
        }
    }

    @WithElementId
    final SimpleActionTable<HostInterfaceLineModel> table;
    private final VerticalPanel contentPanel;

    @Inject
    public SubTabHostInterfaceView(SearchableDetailModelProvider<HostInterfaceLineModel, HostListModel, HostInterfaceListModel> modelProvider,
            EventBus eventBus, ClientStorage clientStorage) {
        super(modelProvider);
        this.table = new SimpleActionTable<HostInterfaceLineModel>(modelProvider, getTableResources(), eventBus, clientStorage);
        ViewIdHandler.idHandler.generateAndSetIds(this);
        initTable();

        contentPanel = new VerticalPanel();
        contentPanel.setWidth("100%");
        contentPanel.add(table);
        contentPanel.add(new Label("Empty"));
        initWidget(contentPanel);
    }

    Resources getTableResources() {
        return GWT.<Resources> create(SubTableResources.class);
    }

    void initTable() {
        table.addColumn(new EmptyColumn(), "", "10%");
        table.addColumn(new EmptyColumn(), "Name", "20%");
        table.addColumn(new EmptyColumn(), "Address", "20%");
        table.addColumn(new EmptyColumn(), "MAC", "20%");
        table.addColumnWithHtmlHeader(new EmptyColumn(), "Speed <sub>(Mbps)</sub>", "10%");
        table.addColumnWithHtmlHeader(new EmptyColumn(), "Rx <sub>(Mbps)</sub>", "10%");
        table.addColumnWithHtmlHeader(new EmptyColumn(), "Tx <sub>(Mbps)</sub>", "10%");
        table.addColumnWithHtmlHeader(new EmptyColumn(), "Drops <sub>(Pkts)</sub>", "10%");
        table.addColumn(new EmptyColumn(), "Bond", "20%");
        table.addColumn(new EmptyColumn(), "Vlan", "20%");
        table.addColumn(new EmptyColumn(), "Network Name", "20%");

        table.addActionButton(new WebAdminButtonDefinition<HostInterfaceLineModel>("Add / Edit") {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getEditCommand();
            }
        });
        table.addActionButton(new WebAdminButtonDefinition<HostInterfaceLineModel>("Edit Management Network") {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getEditManagementNetworkCommand();
            }
        });
        // TODO: separator
        table.addActionButton(new WebAdminButtonDefinition<HostInterfaceLineModel>("Bond") {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getBondCommand();
            }
        });
        table.addActionButton(new WebAdminButtonDefinition<HostInterfaceLineModel>("Detach") {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getDetachCommand();
            }
        });
        // TODO: separator
        table.addActionButton(new WebAdminButtonDefinition<HostInterfaceLineModel>("Save Network Configuration") {
            @Override
            protected UICommand resolveCommand() {
                return getDetailModel().getSaveNetworkConfigCommand();
            }
        });

        table.showRefreshButton();
    }

    @Override
    public void setMainTabSelectedItem(VDS selectedItem) {
        // TODO(vszocs) possible performance optimization: don't create HostInterfaceForm upon each selection
        HostInterfaceForm hostInterfaceForm = new HostInterfaceForm(getDetailModel());
        contentPanel.remove(contentPanel.getWidgetCount() - 1);
        contentPanel.add(hostInterfaceForm);
    }

}
