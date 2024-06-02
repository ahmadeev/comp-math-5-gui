package backend;

import javafx.scene.control.Alert;

import java.util.Locale;

import static java.util.Objects.isNull;

public class Utils {
    public static void exit(String msg, int exitCode) {
        if (!(msg.trim().equals(""))) {
            System.out.println(msg);
        }
        System.exit(exitCode);
    }

    public static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static String formatArrayNumbers(double[] array, int decimals) {
        if (isNull(array)) return null;
        StringBuilder result = new StringBuilder();
        for(double i : array) {
            String format = "%." + decimals + "f";
            result.append(String.format(Locale.ROOT, format, i)).append(", ");
        }
        result.deleteCharAt(result.length() - 2);

        return result.toString();
    }
}

