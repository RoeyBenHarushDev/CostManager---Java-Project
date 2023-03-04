package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Cost;
import model.CostDAO;
import viewmodel.CostManagerViewModel;

public class CostManagerUI {
    private JFrame mainFrame;
    private JComboBox<String> categoryComboBox;
    private JTextField sumTextField;
    private JComboBox<String> currencyComboBox;
    private JTextField descriptionTextField;
    private DefaultTableModel costTableModel;
    private JTable costTable;
    private final CostDAO costDAO;

    public CostManagerUI(CostManagerViewModel viewModel) {
        costDAO = new CostDAO();
        createGUI();
    }

    private void createGUI() {
        mainFrame = new JFrame("Cost Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel categoryLabel = new JLabel("Category:");
        categoryComboBox = new JComboBox<>();
        categoryComboBox.addItem("Food");
        categoryComboBox.addItem("Transportation");
        categoryComboBox.addItem("Entertainment");
        categoryComboBox.addItem("Utilities");
        categoryComboBox.addItem("Rent");
        inputPanel.add(createHorizontalPanel(categoryLabel, categoryComboBox));

        JLabel sumLabel = new JLabel("Sum:");
        sumTextField = new JTextField(10);
        inputPanel.add(createHorizontalPanel(sumLabel, sumTextField));

        JLabel currencyLabel = new JLabel("Currency:");
        currencyComboBox = new JComboBox<>();
        currencyComboBox.addItem("USD");
        currencyComboBox.addItem("EUR");
        currencyComboBox.addItem("JPY");
        inputPanel.add(createHorizontalPanel(currencyLabel, currencyComboBox));

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionTextField = new JTextField(20);
        inputPanel.add(createHorizontalPanel(descriptionLabel, descriptionTextField));

        JButton addButton = new JButton("Add Cost");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = (String) categoryComboBox.getSelectedItem();
                double sum = Double.parseDouble(sumTextField.getText());
                String currency = (String) currencyComboBox.getSelectedItem();
                String description = descriptionTextField.getText();

                String date = "";
                Cost cost = new Cost(category, sum, currency, description, date);
                costDAO.addCost(cost);

                updateCostTable();
            }
        });
        inputPanel.add(addButton);

        mainFrame.add(inputPanel, BorderLayout.NORTH);

        costTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Category", "Sum", "Currency", "Description", "Date"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        costTable = new JTable(costTableModel);
        JScrollPane scrollPane = new JScrollPane(costTable);
        mainFrame.add(scrollPane, BorderLayout.CENTER);

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));

        JButton todayButton = new JButton("Today");
        todayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(new Date());
                List<Cost> costs = null;
                try {
                    costs = costDAO.getCostsByDate(date, true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                updateCostTableModel(costs);
            }
        });
        JButton monthButton = new JButton("This Month");
        monthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                String date = year + "-" + String.format("%02d", month) + "-01";
                List<Cost> costs = null;
                try {
                    costs = costDAO.getCostsByDate(date, true);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                updateCostTableModel(costs);
            }
        });
        JButton showButton = new JButton("Show All");
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Cost> costs = costDAO.getAllCosts();
                updateCostTableModel(costs);
            }
        });
        reportPanel.add(showButton);

        reportPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        reportPanel.add(new JLabel("Report", SwingConstants.CENTER));
        reportPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        reportPanel.add(todayButton);
        reportPanel.add(monthButton);

        mainFrame.add(reportPanel, BorderLayout.SOUTH);

        mainFrame.pack();
        mainFrame.setVisible(true);

        updateCostTable();
    }

    private JPanel createHorizontalPanel(JComponent left, JComponent right) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(left);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(right);
        return panel;
    }

    private void updateCostTable() {
        List<Cost> costs = costDAO.getAllCosts();
        updateCostTableModel(costs);
    }

    private void updateCostTableModel(List<Cost> costs) {
        costTableModel.setRowCount(0);
        for (Cost cost : costs) {
            costTableModel.addRow(new Object[]{cost.getCategory(), cost.getSum(), cost.getCurrency(),
                    cost.getDescription(), cost.getDate()});
        }
    }

    public void show() {
        mainFrame.setVisible(true);
    }
}
