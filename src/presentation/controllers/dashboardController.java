package presentation.controllers;

import domain.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import presentation.core.InitializableController;
import presentation.core.UserAwareController;
import presentation.core.View;
import presentation.core.ViewManager;
import service.UserService;

import java.io.IOException;
import java.util.Objects;

public class dashboardController implements InitializableController, UserAwareController
{
    public Button dashboardBtn;
    public Button settingsBtn;
    @FXML
    private Label welcomeLbl;

    private User loggedInUser;
    private UserService service;
    private Button currentActive;

    public void handleLogout() throws IOException
    {
        ViewManager.showView(View.LOGIN);
    }

    @Override
    public void init(Object service)
    {
        this.service = (UserService) service;

    }

    @Override
    public void setUser(User user)
    {
        this.loggedInUser = user;
        welcomeLbl.setText("Hello, " + loggedInUser.username() + "!");
    }

    private void setActive(Button btn) {

        if (currentActive != null) {
            currentActive.getStyleClass().remove("active");
        }

        btn.getStyleClass().add("active");
        currentActive = btn;
    }

    public void handleSettingsPressed()
    {
        setActive(settingsBtn);
    }

    public void handleDashboardPressed()
    {
        setActive(dashboardBtn);
    }
}
