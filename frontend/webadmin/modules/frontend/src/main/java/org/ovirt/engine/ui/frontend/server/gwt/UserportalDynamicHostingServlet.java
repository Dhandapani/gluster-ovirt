package org.ovirt.engine.ui.frontend.server.gwt;

/**
 * A landing servlet for UserPortal project.
 *
 * @author Asaf Shakarchi
 */
public class UserportalDynamicHostingServlet extends GwtDynamicHostPageServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected String getPageTitle() {
        return "oVirt Enterprise Virtualization Engine User Portal";
    }

    @Override
    protected String getSelectorScriptName() {
        return "userportal.nocache.js";
    }

}
