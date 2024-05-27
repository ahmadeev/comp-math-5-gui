package runnable;

import backend.DataInput;
import backend.Options;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static backend.Methods.getOptionByNumber;
import static backend.math.Utils.showAlert;
import static java.util.Objects.isNull;

public class MainController implements Initializable {
    @FXML
    private TextField textFieldZero;
    @FXML
    private TextField textFieldOne;
    @FXML
    private TextField textFieldTwo;
    @FXML
    private Button submitButton;
    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    protected void handleSubmitEvent(ActionEvent event) {

        plot.getData().clear();

        DataInput dataInput = handleTextInput(event);
        if (!isNull(dataInput)) {
            double[][] result = dataInput.getXY();
            double[] x = result[0];
            double[] y = result[1];
            double point = dataInput.getPoint();

            drawLine(1, result);
            drawLine(2, result);
            drawLine(3, result);
            drawLine(4, result);

            for(int i = 1; i <= 4; i++) {
                System.out.println(getOptionByNumber(i).getPolynomialSum(point, x, y));
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Введены некорректные данные!");
            //textFieldOne.setText("");
            //textFieldTwo.setText("");
        }
    }

    private DataInput handleTextInput(ActionEvent event) {
        if (event.getSource() != submitButton) return null;

        String pointText = textFieldZero.getText().trim();
        String[] xText = textFieldOne.getText().split("\\s+");
        String[] yText = textFieldTwo.getText().split("\\s+");

        if (xText.length != yText.length) return null;

        int size = xText.length;
        pointText = validateNumber(pointText);
        double point;
        if (!isNull(pointText)) point = Double.parseDouble(pointText);
        else return null;
        double[] x = new double[size];
        double[] y = new double[size];;

        for(int i = 0; i < size; i++) {
            xText[i] = validateNumber(xText[i]);
            yText[i] = validateNumber(yText[i]);

            if (!(isNull(xText[i]) || isNull(yText[i]))) {
                x[i] = Double.parseDouble(xText[i]);
                y[i] = Double.parseDouble(yText[i]);
            } else {
                return null;
            }
        }

        return new DataInput(new double[][]{x, y}, point);
    }

    private void drawLine(int number, double[][] xy) {
        double[] x = xy[0];
        double[] y = xy[1];

        int size = x.length;
        double step = 0.01;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for(int i = 0; i < size; i++) {
            series.getData().add(new XYChart.Data<>(x[i], y[i]));
        }

        double a = x[0] - 100 * step;
        double b = x[size - 1] + 100 * step;

        Options option = getOptionByNumber(number);

        while (a <= b) {
            series.getData().add(new XYChart.Data<>(a, option.getPolynomialSum(a, x, y)));
            a += step;
        }

        series.setName(option.getClass().getSimpleName());
        plot.setCreateSymbols(false);
        plot.getData().add(series);
    }

    private String validateNumber(String text) {
        text = text.replace(",", ".");
        if (text.matches("[+-]?([0-9]*[.])?[0-9]+")) {
            return text;
        } else {
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("meow");
    }
}
