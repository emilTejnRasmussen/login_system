package presentation.core;

public enum View
{
    LOGIN("login"),
    CREATE_ACCOUNT("createAccount"),
    DASHBOARD("dashboard");

    private final String view;
    View(String view){
        this.view = view;
    }

    public String getView() {
        return "/fxml/" + this.view + ".fxml";
    }
}
