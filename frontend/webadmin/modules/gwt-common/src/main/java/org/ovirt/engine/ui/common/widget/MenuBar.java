package org.ovirt.engine.ui.common.widget;

import org.ovirt.engine.ui.common.utils.PopupUtils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 *
 * MenuBar that adjusts sub menu popup location to fit screen
 *
 * It is workaround to the problem described below-
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=407
 * http://code.google.com/p/google-web-toolkit/issues/detail?id=3924
 *
 */
public class MenuBar extends com.google.gwt.user.client.ui.MenuBar {
    public MenuBar() {
        super();
        setAutoOpen(true);
    }

    public MenuBar(boolean vertical) {
        super(vertical);
        setAutoOpen(true);
    }

    private native PopupPanel getSubMenuPopup() /*-{
        return this.@com.google.gwt.user.client.ui.MenuBar::getPopup()();
    }-*/;

    private native MenuItem findItem(Element hItem) /*-{
        return this.@com.google.gwt.user.client.ui.MenuBar::findItem(Lcom/google/gwt/user/client/Element;)(hItem);
    }-*/;

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEOVER:
        case Event.ONCLICK: {
            MenuItem item = findItem(DOM.eventGetTarget(event));

            if (item !=null){
                PopupPanel subMenuPopup = getSubMenuPopup();
                if (subMenuPopup != null) {
                    PopupUtils.adjustPopupLocationToFitScreenAndShow(subMenuPopup,
                            subMenuPopup.getAbsoluteLeft(),
                            subMenuPopup.getAbsoluteTop(), this);
                }
            }
            break;
        }
        default: {
            break;
        }
        }
    }

}
