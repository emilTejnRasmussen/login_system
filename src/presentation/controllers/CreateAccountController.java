package presentation.controllers;
import domain.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import presentation.core.InitializableController;
import presentation.core.View;
import presentation.core.ViewManager;
import service.UserService;

import java.io.IOException;

public class CreateAccountController implements InitializableController
{
    @FXML
    private Label commonErrorLbl;
    @FXML
    private Label usernameErrorLbl;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private Label passwordErrorLbl;
    @FXML
    private TextField passwordTxtField;
    @FXML
    private Label emailErrorLbl;
    @FXML
    private TextField emailTxtField;

    private UserService service;

    @Override
    public void init(Object service)
    {
        this.service = (UserService) service;
    }

    public void handleCreateAccount() throws IOException
    {
        String username = usernameTxtField.getText().trim();
        String password = passwordTxtField.getText().trim();
        String email = emailTxtField.getText().trim();

        if (!validateInput(username, password, email)) return;


        try
        {
            User newUser = service.register(username,email, password);
            ViewManager.showView(View.DASHBOARD, newUser);
        } catch (IllegalArgumentException e) {
            commonErrorLbl.setText(e.getMessage());
        } catch (RuntimeException e) {
            commonErrorLbl.setText("Something went wrong - try again.");
        }

    }

    private boolean validateInput(String username, String password, String email) {
        usernameErrorLbl.setText("");
        passwordErrorLbl.setText("");
        emailErrorLbl.setText("");
        commonErrorLbl.setText("");

        boolean ok = true;

        // Username
        if (username == null || username.isBlank()) {
            usernameErrorLbl.setText("Username is required");
            ok = false;
        } else {
            if (username.length() < 6) {
                usernameErrorLbl.setText("Username must be at least 6 characters");
                ok = false;
            } else if (username.contains(" ")) {
                usernameErrorLbl.setText("Username cannot contain spaces");
                ok = false;
            }
        }

        // Email
        if (email == null || email.isBlank()) {
            emailErrorLbl.setText("Email is required");
            ok = false;
        } else if (!isValidEmail(email)) {
            emailErrorLbl.setText("Please enter a valid email");
            ok = false;
        }

        // Password
        if (password == null || password.isBlank()) {
            passwordErrorLbl.setText("Password is required");
            ok = false;
        } else if (password.length() < 6) {
            passwordErrorLbl.setText("Password must be at least 6 characters");
            ok = false;
        }

        return ok;
    }

    private boolean isValidEmail(String email) {
        // simple, practical email check (not perfect, but good for apps)
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }


    public void handleGotAccount() throws IOException
    {
        ViewManager.showView(View.LOGIN);
    }

    public void handleCloseApp()
    {
        Platform.exit();
    }
}
