package presentation.controllers;

import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import presentation.core.InitializableController;
import presentation.core.UserAwareController;
import presentation.core.View;
import presentation.core.ViewManager;
import service.UserService;

import java.io.IOException;

public class dashboardController implements InitializableController, UserAwareController
{
    @FXML
    private Label welcomeLbl;

    private User loggedInUser;
    private UserService service;

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
}
