package viewmodel;

import model.Cost;
import model.CostDAO;

import java.sql.SQLException;
import java.util.List;

public class CostManagerViewModel {
    private final CostDAO costDAO;

    public CostManagerViewModel(CostDAO costDAO) {
        this.costDAO = costDAO;
    }

    public void addCost(String category, double sum, String currency, String description, String date) {
        Cost cost = new Cost(category, sum, currency, description, date);
        costDAO.addCost(cost);
    }

    public List<String> getCategories() {
        return costDAO.getCategories();
    }

    public List<Cost> getCostsByDate(String date, boolean exactMatch) throws SQLException {
        return costDAO.getCostsByDate(date, exactMatch);
    }
}
