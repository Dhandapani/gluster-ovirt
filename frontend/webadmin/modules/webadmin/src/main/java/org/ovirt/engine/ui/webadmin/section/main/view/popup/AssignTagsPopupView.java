package org.ovirt.engine.ui.webadmin.section.main.view.popup;

import java.util.ArrayList;
import java.util.List;

import org.ovirt.engine.core.compat.Event;
import org.ovirt.engine.core.compat.EventArgs;
import org.ovirt.engine.core.compat.IEventListener;
import org.ovirt.engine.core.compat.PropertyChangedEventArgs;
import org.ovirt.engine.ui.common.idhandler.ElementIdHandler;
import org.ovirt.engine.ui.common.view.popup.AbstractModelBoundPopupView;
import org.ovirt.engine.ui.common.widget.dialog.SimpleDialogPanel;
import org.ovirt.engine.ui.uicommonweb.models.common.SelectionTreeNodeModel;
import org.ovirt.engine.ui.uicommonweb.models.tags.TagListModel;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.ApplicationResources;
import org.ovirt.engine.ui.webadmin.section.main.presenter.popup.AssignTagsPopupPresenterWidget;
import org.ovirt.engine.ui.webadmin.uicommon.model.ModelListTreeViewModel;
import org.ovirt.engine.ui.webadmin.uicommon.model.SimpleSelectionTreeNodeModel;
import org.ovirt.engine.ui.webadmin.widget.editor.EntityModelCellTree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.inject.Inject;

public class AssignTagsPopupView extends AbstractModelBoundPopupView<TagListModel>
        implements AssignTagsPopupPresenterWidget.ViewDef {

    interface Driver extends SimpleBeanEditorDriver<TagListModel, AssignTagsPopupView> {
        Driver driver = GWT.create(Driver.class);
    }

    interface ViewUiBinder extends UiBinder<SimpleDialogPanel, AssignTagsPopupView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    interface ViewIdHandler extends ElementIdHandler<AssignTagsPopupView> {
        ViewIdHandler idHandler = GWT.create(ViewIdHandler.class);
    }

    @UiField(provided = true)
    @Ignore
    EntityModelCellTree<SelectionTreeNodeModel, SimpleSelectionTreeNodeModel> tree;

    @Inject
    public AssignTagsPopupView(EventBus eventBus, ApplicationResources resources, ApplicationConstants constants) {
        super(eventBus, resources);
        initTree();
        initWidget(ViewUiBinder.uiBinder.createAndBindUi(this));
        ViewIdHandler.idHandler.generateAndSetIds(this);
        localize(constants);
        Driver.driver.initialize(this);
    }

    void localize(ApplicationConstants constants) {
    }

    private void initTree() {
        CellTree.Resources res = GWT.create(AssignTagTreeResources.class);
        tree = new EntityModelCellTree<SelectionTreeNodeModel, SimpleSelectionTreeNodeModel>(res);
    }

    @Override
    public void edit(TagListModel object) {
        Driver.driver.edit(object);

        // Listen to Properties
        object.getPropertyChangedEvent().addListener(new IEventListener() {

            @Override
            public void eventRaised(Event ev, Object sender, EventArgs args) {
                TagListModel model = (TagListModel) sender;
                String propertyName = ((PropertyChangedEventArgs) args).PropertyName;
                if ("SelectionNodeList".equals(propertyName)) {
                    updateTree(model);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void updateTree(TagListModel model) {
        // Get tag node list
        ArrayList<SelectionTreeNodeModel> tagTreeNodes = model.getSelectionNodeList();

        // Get tree view model
        ModelListTreeViewModel<SelectionTreeNodeModel, SimpleSelectionTreeNodeModel> modelListTreeViewModel =
                (ModelListTreeViewModel<SelectionTreeNodeModel, SimpleSelectionTreeNodeModel>) tree.getTreeViewModel();

        // Set root nodes
        List<SimpleSelectionTreeNodeModel> rootNodes = SimpleSelectionTreeNodeModel.fromList(tagTreeNodes);
        modelListTreeViewModel.setRoot(rootNodes);

        // Update tree data
        AsyncDataProvider<SimpleSelectionTreeNodeModel> asyncTreeDataProvider =
                modelListTreeViewModel.getAsyncTreeDataProvider();
        asyncTreeDataProvider.updateRowCount(rootNodes.size(), true);
        asyncTreeDataProvider.updateRowData(0, rootNodes);

        // Expand tree nodes
        expandTree();
    }

    private void expandTree() {
        if (tree != null) {
            expandTree(tree.getRootTreeNode());
        }
    }

    private void expandTree(TreeNode node) {
        if (node == null) {
            return;
        }

        if (node.getChildCount() > 0) {
            for (int i = 0; i < node.getChildCount(); i++) {
                expandTree(node.setChildOpen(i, true));
            }
        }
    }

    @Override
    public TagListModel flush() {
        return Driver.driver.flush();
    }

    interface AssignTagTreeResources extends CellTree.Resources {
        interface TableStyle extends CellTree.Style {
        }

        @Override
        @Source({ "org/ovirt/engine/ui/webadmin/css/AssignTagTree.css" })
        TableStyle cellTreeStyle();
    }

}
