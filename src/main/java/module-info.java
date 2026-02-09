module ca.qc.bdeb.sim.galak_sim {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.qc.bdeb.sim.galak_sim to javafx.fxml;
    exports ca.qc.bdeb.sim.galak_sim;
    exports ca.qc.bdeb.sim.galak_sim.astres;
    opens ca.qc.bdeb.sim.galak_sim.astres to javafx.fxml;
    exports ca.qc.bdeb.sim.galak_sim.addons;
    opens ca.qc.bdeb.sim.galak_sim.addons to javafx.fxml;
    exports ca.qc.bdeb.sim.galak_sim.graphics;
    opens ca.qc.bdeb.sim.galak_sim.graphics to javafx.fxml;
}