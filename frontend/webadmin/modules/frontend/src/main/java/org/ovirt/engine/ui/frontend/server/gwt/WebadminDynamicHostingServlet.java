package org.ovirt.engine.ui.frontend.server.gwt;

/**
 * A landing servlet for WebAdmin project.
 *
 * @author Asaf Shakarchi
 */
public class WebadminDynamicHostingServlet extends GwtDynamicHostPageServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected String getPageTitle() {
        return "oVirt Enterprise Virtualization Engine Web Administration";
    }

    @Override
    protected String getSelectorScriptName() {
        return "webadmin.nocache.js";
    }

}
