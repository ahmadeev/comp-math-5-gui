module com.example.compmathlab5gui {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens runnable to javafx.fxml;
    exports runnable;
}