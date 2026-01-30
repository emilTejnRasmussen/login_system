package presentation.controllers;

import domain.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import presentation.core.InitializableController;
import presentation.core.View;
import presentation.core.ViewManager;
import service.UserService;
import java.io.IOException;
import java.util.Optional;

public class LoginController implements InitializableController
{
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private TextField passwordTxtField;
    @FXML
    private Label commonErrorLbl;

    private UserService service;

    @Override
    public void init(Object service)
    {
        this.service = (UserService) service;
        passwordTxtField.textProperty()
                .bindBidirectional(passwordField.textProperty());
    }

    public void onLoginPressed() throws IOException
    {
        String username = usernameTxtField.getText().trim();
        String password = passwordTxtField.getText();

        Optional<User> userOpt = service.login(username, password);

        if (userOpt.isEmpty()) {
            commonErrorLbl.setText("Invalid username or password");
            return;
        }

        User user = userOpt.get();
        ViewManager.showView(View.DASHBOARD, user);

    }

    public void handleCreateAccount() throws IOException
    {
        ViewManager.showView(View.CREATE_ACCOUNT);
    }

    public void handleForgotPassword()
    {
    }

    public void togglePasswordVisibility()
    {
        boolean show = showPasswordCheckBox.isSelected();

        passwordTxtField.setVisible(show);
        passwordTxtField.setManaged(show);

        passwordField.setVisible(!show);
        passwordField.setManaged(!show);
    }

    public void handleCloseApp()
    {
        Platform.exit();
    }
}
