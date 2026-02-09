module ca.qc.bdeb.sim.galak_sim {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.qc.bdeb.sim.galak_sim to javafx.fxml;
    exports ca.qc.bdeb.sim.galak_sim;
}