package org.ovirt.engine.ui.common.view.popup;

import org.ovirt.engine.ui.common.CommonApplicationResources;
import org.ovirt.engine.ui.common.idhandler.HasElementId;
import org.ovirt.engine.ui.common.presenter.AbstractModelBoundPopupPresenterWidget;
import org.ovirt.engine.ui.common.utils.ElementIdUtils;
import org.ovirt.engine.ui.common.view.AbstractPopupView;
import org.ovirt.engine.ui.common.widget.AbstractUiCommandButton;
import org.ovirt.engine.ui.common.widget.HasUiCommandClickHandlers;
import org.ovirt.engine.ui.common.widget.IsProgressContentWidget;
import org.ovirt.engine.ui.common.widget.UiCommandButton;
import org.ovirt.engine.ui.common.widget.dialog.AbstractDialogPanel;
import org.ovirt.engine.ui.common.widget.dialog.PopupNativeKeyPressHandler;
import org.ovirt.engine.ui.common.widget.dialog.ProgressPopupContent;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.uicommonweb.models.Model;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Base class for popup views bound to a UiCommon Window model.
 *
 * @param <T>
 *            Window model type.
 */
public abstract class AbstractModelBoundPopupView<T extends Model> extends AbstractPopupView<AbstractDialogPanel>
        implements AbstractModelBoundPopupPresenterWidget.ViewDef<T>, HasElementId {

    /**
     * Popup progress indicator widget
     */
    private final IsProgressContentWidget progressContent;

    /**
     * Actual popup content
     */
    private Widget popupContent;

    /**
     * Popup hash-name
     */
    private String hashName;

    private String elementId = DOM.createUniqueId();

    public AbstractModelBoundPopupView(EventBus eventBus, CommonApplicationResources resources) {
        super(eventBus, resources);
        this.progressContent = createProgressContentWidget();
    }

    @Override
    protected void initWidget(AbstractDialogPanel widget) {
        super.initWidget(widget);
        this.popupContent = widget.getContent();
    }

    protected AbstractUiCommandButton createCommandButton(String label) {
        return new UiCommandButton(label);
    }

    protected IsProgressContentWidget createProgressContentWidget() {
        return new ProgressPopupContent();
    }

    @Override
    public void setTitle(String title) {
        asWidget().setHeader(new Label(title));
    }

    @Override
    public void setMessage(String message) {
        // No-op, override as necessary
    }

    @Override
    public void setItems(Iterable<?> items) {
        // No-op, override as necessary
    }

    @Override
    public void setHashName(String name) {
        this.hashName = name;
    }

    @Override
    public void setHelpCommand(UICommand command) {
        asWidget().setHelpCommand(command);
    }

    @Override
    public HasUiCommandClickHandlers addFooterButton(String label, String uniqueId) {
        AbstractUiCommandButton button = createCommandButton(label);
        asWidget().addFooterButton(button);

        // Set button element ID for better accessibility
        button.asWidget().getElement().setId(
                ElementIdUtils.createElementId(elementId, uniqueId));

        return button;
    }

    @Override
    public void removeButtons() {
        asWidget().removeFooterButtons();
    }

    @Override
    public void startProgress(String progressMessage) {
        // Set dialog content to the progress indicator widget
        progressContent.setProgressMessage(progressMessage);
        asWidget().setContent(progressContent.asWidget());

        // Hide dialog buttons when starting progress
        asWidget().setFooterPanelVisible(false);
    }

    @Override
    public void stopProgress() {
        // Set dialog content to the actual popup content widget
        asWidget().setContent(popupContent);

        // Show dialog buttons when stopping progress
        asWidget().setFooterPanelVisible(true);

        // Now that the panel is visible we can try to focus
        focusInput();
    }

    @Override
    public void focusInput() {
        // No-op, override as necessary
    }

    @Override
    public void setPopupKeyPressHandler(PopupNativeKeyPressHandler handler) {
        asWidget().setKeyPressHandler(handler);
    }

    @Override
    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    protected String getHashName() {
        return hashName;
    }

}
