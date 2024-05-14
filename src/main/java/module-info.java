module pl.edu.pw {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens pl.edu.pw to javafx.fxml;
    exports pl.edu.pw;

    opens pl.edu.pw.gui to javafx.fxml;
    exports pl.edu.pw.gui to javafx.graphics, javafx.fxml;
}