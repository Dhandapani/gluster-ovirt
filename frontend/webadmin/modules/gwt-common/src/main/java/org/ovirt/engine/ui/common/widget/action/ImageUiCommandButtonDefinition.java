package org.ovirt.engine.ui.common.widget.action;

import org.ovirt.engine.ui.common.CommonApplicationTemplates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * UiCommon {@linkplain org.ovirt.engine.ui.uicommonweb.UICommand command} button definition that has an image
 * associated with it.
 *
 * @param <T>
 *            Action panel item type.
 */
public abstract class ImageUiCommandButtonDefinition<T> extends UiCommandButtonDefinition<T> {

    private static final CommonApplicationTemplates TEMPLATES = GWT.create(CommonApplicationTemplates.class);

    private final SafeHtml enabledImage;
    private final SafeHtml disabledImage;
    private boolean showTitle;
    private boolean imageAfterTitle;

    public ImageUiCommandButtonDefinition(EventBus eventBus,
            String title,
            ImageResource enabledImage,
            ImageResource disabledImage,
            boolean showTitle,
            boolean imageAfterTitle, CommandLocation commandLocation) {
        super(eventBus, title, commandLocation);
        this.enabledImage = enabledImage != null
                ? SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(enabledImage).getHTML()) : null;
        this.disabledImage = disabledImage != null
                ? SafeHtmlUtils.fromTrustedString(AbstractImagePrototype.create(disabledImage).getHTML()) : null;
        this.showTitle = showTitle;
        this.imageAfterTitle = imageAfterTitle;
    }

    /**
     * Creates a new button with the given title and images.
     *
     * @param title
     *            The Command Text title
     * @param enabledImage
     *            The Image to display when the command is Enabled
     * @param disabledImage
     *            The Image to display when the command is Disabled
     */
    public ImageUiCommandButtonDefinition(EventBus eventBus,
            String title,
            ImageResource enabledImage,
            ImageResource disabledImage,
            boolean showTitle,
            boolean imageAfterTitle) {
        this(eventBus,
                title,
                enabledImage,
                disabledImage,
                showTitle,
                imageAfterTitle,
                CommandLocation.ContextAndToolBar);
    }

    public ImageUiCommandButtonDefinition(EventBus eventBus,
            String title,
            ImageResource enabledImage,
            ImageResource disabledImage) {
        this(eventBus, title, enabledImage, disabledImage, false, false);
    }

    @Override
    public SafeHtml getEnabledHtml() {
        return !showTitle ? enabledImage
                : (!imageAfterTitle ? TEMPLATES.imageTextButton(enabledImage, getTitle())
                        : TEMPLATES.textImageButton(getTitle(), enabledImage));
    }

    @Override
    public SafeHtml getDisabledHtml() {
        return !showTitle ? disabledImage
                : (!imageAfterTitle ? TEMPLATES.imageTextButton(disabledImage, getTitle())
                        : TEMPLATES.textImageButton(getTitle(), disabledImage));
    }

}
