package runnable;

import backend.CalculatedData;
import backend.DataInput;
import backend.math.Options;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static backend.Utils.*;
import static backend.math.Methods.*;
import static java.util.Objects.isNull;
import static runnable.Main.*;

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
    private Button deltaFButton;
    @FXML
    private Button approximationButton;
    @FXML
    private LineChart<Number, Number> plot;

    @FXML
    protected void handleSubmitEvent(ActionEvent event) {
        lagrangeData = new CalculatedData.LagrangeData();
        newtonData = new CalculatedData.NewtonData();
        newtonForwardsData = new CalculatedData.NewtonForwardsData();
        newtonBackwardsData = new CalculatedData.NewtonBackwardsData();

        plot.getData().clear();

        DataInput dataInput = handleTextInput(event);
        if (!isNull(dataInput)) {
            double[][] result = dataInput.getXY();
            double[] x = result[0];
            double[] y = result[1];
            double point = dataInput.getPoint();

            int size = x.length;

            boolean flag = true;
            double previousStep = x[1] - x[0];
            double step;
            for(int i = 2; i < size; i++) {
                step = x[i] - x[i-1];
                if (Math.abs(step - previousStep) > 1e-8) {
                    flag = false;
                    break;
                }
                previousStep = step;
            }
            //drawDots(result);
            drawLine(1, result, point);
            getDataByNumber(1).setXy(new double[]{point, getOptionByNumber(1).getPolynomialSum(point, x, y)});
            if (flag) {
                //  равноотстоящие
                getTable(size, y);
                drawLine(3, result, point);
                getDataByNumber(3).setXy(new double[]{point, getOptionByNumber(3).getPolynomialSum(point, x, y)});
                drawLine(4, result, point);
                getDataByNumber(4).setXy(new double[]{point, getOptionByNumber(4).getPolynomialSum(point, x, y)});

            } else {
                //  неравноотстоящие
                getTable(size, x, y);
                drawLine(2, result, point);
                getDataByNumber(2).setXy(new double[]{point, getOptionByNumber(2).getPolynomialSum(point, x, y)});
            }

            //System.out.println();

/*            System.out.println("Гаусс 1: " + gaussGreaterThanA.getPolynomialSum(0.32, x, y));
            System.out.println("Гаусс 2: " + gaussLesserThanA.getPolynomialSum(0.28, x, y));*/
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

    @FXML
    protected void handleGetDeltaFButton(ActionEvent event) {
        if (event.getSource() != deltaFButton) exit("че-то с кнопкой для разностей", 1);

        if (isNull(getDataByNumber(2).getArray()) && isNull(getDataByNumber(3).getArray()) && isNull(getDataByNumber(4).getArray())) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Сначала введите точки x и y!");
        } else {
            StringBuilder content = new StringBuilder();
            if (!isNull(newtonData.getArray())) {
                content.append(getNameByNumber(2)).append(":\n\n");
                for(double[] i : newtonData.getArray()) {
                    content.append(Arrays.toString(i)).append("\n");
                }
                content.append("\n");
            }

            if (!isNull(newtonForwardsData.getArray())) {
                content.append(getNameByNumber(3)).append(" и ").append(getNameByNumber(4)).append(":\n\n");
                for(double[] i : newtonForwardsData.getArray()) {
                    content.append(Arrays.toString(i)).append("\n");
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Разности", content.toString());
        }
    }

    @FXML
    protected void handleGetApproximationButton(ActionEvent event) {
        if (event.getSource() != approximationButton) exit("че-то с кнопкой для разностей", 1);

/*        for(int i = 1; i <= 4; i++) {
            System.out.println(Arrays.toString(getDataByNumber(i).getXy()));
        }*/

        if (isNull(getDataByNumber(1).getXy())) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Сначала введите точки x и y!");
        } else {
            StringBuilder content = new StringBuilder();
            content.append("Вычисленные приближения:").append("\n\n");
            for(int i = 1; i <= 4; i++) {
                if (!isNull(getDataByNumber(i).getXy())) {
                    content.append(getNameByNumber(i)).append(": ");
                    content.append(formatArrayNumbers((getDataByNumber(i).getXy()), 6)).append("\n");
                }
            }
            showAlert(Alert.AlertType.INFORMATION, "Приближения", content.toString());
        }
    }

/*    private void drawDots(double[][] xy) {
        double[] x = xy[0];
        double[] y = xy[1];

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for(int i = 0; i < x.length; i++) {
            series.getData().add(new XYChart.Data<>(x[i], y[i]));
        }
        Platform.runLater(() -> {
            series.getNode().setVisible(false);
        });

        plot.setCreateSymbols(true);
        //plot.setOpacity(0.5);
        plot.getData().add(series);
    }*/

    private void drawLine(int number, double[][] xy, double point) {
        double[] x = xy[0];
        double[] y = xy[1];

        int size = x.length;
        double step = 0.01;
        double offset = (x[size - 1] - x[0]) * 0.1;

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

/*        for(int i = 0; i < size; i++) {
            series.getData().add(new XYChart.Data<>(x[i], y[i]));
        }*/

        /*double a = x[0] - offset;
        double b = x[size - 1] + offset;*/

        double a = x[0] < point ? x[0] - offset : point - offset;
        double b = x[size - 1] > point ? x[size - 1] + offset : point + offset;

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
        textFieldZero.setText("0.47");
        textFieldOne.setText("0.1 0.2 0.3 0.4 0.5");
        textFieldTwo.setText("1.25 2.38 3.79 5.44 7.14");
    }
}
