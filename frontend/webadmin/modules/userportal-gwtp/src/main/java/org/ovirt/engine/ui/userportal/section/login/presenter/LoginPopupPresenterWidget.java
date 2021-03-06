package org.ovirt.engine.ui.userportal.section.login.presenter;

import java.util.logging.Logger;

import org.ovirt.engine.core.compat.Event;
import org.ovirt.engine.core.compat.EventArgs;
import org.ovirt.engine.core.compat.IEventListener;
import org.ovirt.engine.ui.common.widget.HasEditorDriver;
import org.ovirt.engine.ui.uicommonweb.models.userportal.UserPortalLoginModel;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class LoginPopupPresenterWidget extends PresenterWidget<LoginPopupPresenterWidget.ViewDef> {

    public interface ViewDef extends PopupView, HasEditorDriver<UserPortalLoginModel> {

        void resetAndFocus();

        void setErrorMessage(String text);

        void clearErrorMessage();

    }

    private static final Logger logger = Logger.getLogger(LoginPopupPresenterWidget.class.getName());

    private final ConnectAutomaticallyProvider connectAutomaticallyProvider;

    @Inject
    public LoginPopupPresenterWidget(EventBus eventBus,
            ViewDef view,
            UserPortalLoginModel loginModel,
            ConnectAutomaticallyProvider connectAutomaticallyProvider) {
        super(eventBus, view);

        this.connectAutomaticallyProvider = connectAutomaticallyProvider;
        getView().edit(loginModel);
    }

    @Override
    protected void onBind() {
        super.onBind();

        final UserPortalLoginModel loginModel = getView().flush();
        loginModel.getLoggedInEvent().addListener(new IEventListener() {
            @Override
            public void eventRaised(Event ev, Object sender, EventArgs args) {
                getView().clearErrorMessage();
                saveLoginAutomatically(loginModel);
            }

        });

        loginModel.getLoginFailedEvent().addListener(new IEventListener() {
            @Override
            public void eventRaised(Event ev, Object sender, EventArgs args) {
                logger.warning("Login failed for user [" + loginModel.getUserName().getEntity() + "]");
                getView().setErrorMessage(loginModel.getMessage());

                // FIXME: Re-enable login properties (can't this be handled by the model itself when auth fails?)
                loginModel.getUserName().setIsChangable(true);
                loginModel.getPassword().setIsChangable(true);
                loginModel.getDomain().setIsChangable(true);
                loginModel.getLoginCommand().setIsExecutionAllowed(true);
            }
        });

        initLoginAutomatically(loginModel);
    }

    private void saveLoginAutomatically(UserPortalLoginModel loginModel) {
        Boolean isAutoconnect = (Boolean) loginModel.getIsAutoConnect().getEntity();
        connectAutomaticallyProvider.storeConnectAutomatically(isAutoconnect);
    }

    private void initLoginAutomatically(UserPortalLoginModel loginModel) {
        boolean isAutoconnect = connectAutomaticallyProvider.readConnectAutomatically();
        loginModel.getIsAutoConnect().setEntity(isAutoconnect);
    }

    @Override
    protected void onReset() {
        super.onReset();

        getView().resetAndFocus();
    }

}
