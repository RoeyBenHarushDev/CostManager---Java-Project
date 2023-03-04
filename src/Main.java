import model.CostDAO;
import view.CostManagerUI;
import viewmodel.CostManagerViewModel;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Create a new instance of the CostDAO class
        CostDAO costDAO = new CostDAO();

        // Create a new instance of the CostManagerViewModel class
        CostManagerViewModel viewModel = new CostManagerViewModel(costDAO);

        // Use SwingUtilities to run the GUI on the event dispatch thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create a new instance of the CostManagerUI class
                CostManagerUI ui = new CostManagerUI(viewModel);

                // Show the UI
                ui.show();
            }
        });
    }
}
