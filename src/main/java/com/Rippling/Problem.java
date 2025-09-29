package com.Rippling;

import java.util.*;

interface Rule {
    RuleViolation evaluate(Map<String, String> expense);
    String getRuleId();
    String getRuleDescription();
}

class RuleViolation {
    private final String ruleId;
    private final String ruleDescription;
    private final String reason;
    private final boolean isViolated;

    public RuleViolation(String ruleId, String ruleDescription, String reason, boolean isViolated) {
        this.ruleId = ruleId;
        this.ruleDescription = ruleDescription;
        this.reason = reason;
        this.isViolated = isViolated;
    }

    public String getRuleId() {
        return ruleId;
    }
    public String getRuleDescription() {
        return ruleDescription;
    }

    public String getReason() {
        return reason;
    }

    public boolean isViolated() {
        return isViolated;
    }
}

class ExpenseEvaluationResult {
    private final String expenseId;
    private final List<RuleViolation> violations;
    private final boolean hasViolations;

    public ExpenseEvaluationResult(String expenseId, List<RuleViolation> violations) {
        this.expenseId = expenseId;
        this.violations = violations;
        this.hasViolations = violations.stream().anyMatch(RuleViolation::isViolated);
    }

    public String getExpenseId() {
        return expenseId;
    }
    public List<RuleViolation> getViolations() {
        return violations;
    }
    public boolean hasViolations() {
        return hasViolations;
    }
}

class RestaurantAmountLimitRule implements Rule {
    private final double maxAmount;

    public RestaurantAmountLimitRule(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public RuleViolation evaluate(Map<String, String> expense) {
        String vendorId = expense.get("vendor_type");
        String amountStr =  expense.get("amount_usd");

        if(!"restaurant".equalsIgnoreCase(vendorId)){
            return new RuleViolation(getRuleId(), getRuleDescription(),
                    "Not a restaurant expense", false);
        }

        try {
            Double amount = Double.valueOf(amountStr);
            boolean isViolated = amount > maxAmount;
            String reason = isViolated ? String.format("Restaurant expense %.2f exceeds the limit %.2f", amount, maxAmount) :
                    String.format("Restaurant expense %.2f is within the limit %.2f", amount, maxAmount);

            return new RuleViolation(getRuleId(), getRuleDescription(), reason, isViolated);
        } catch (NumberFormatException e) {
            return new RuleViolation(getRuleId(), getRuleDescription(), "Invalid amount format", false);
        }
    }

    @Override
    public String getRuleId() {
        return "RESTAURANT_MAX_AMOUNT_LIMIT";
    }

    @Override
    public String getRuleDescription() {
        return String.format("No Restaurant expenses over %.2f", maxAmount);
    }
}

class NoAirFareRule implements Rule {
    @Override
    public RuleViolation evaluate(Map<String, String> expense) {
        String expenseType = expense.get("expense_type");
        boolean isViolated = "airfare".equals(expenseType);
        String reason = isViolated ? "AirFare expense are not allowed" : "Not an airfare expense";

        return new RuleViolation(getRuleId(), getRuleDescription(), reason, isViolated);
    }

    @Override
    public String getRuleId() {
        return "NO_AIRFARE_EXPENSE";
    }

    @Override
    public String getRuleDescription() {
        return "AirFare expenses are not allowed";
    }
}

class NoEntertainmentRule implements Rule {
    @Override
    public RuleViolation evaluate(Map<String, String> expense) {
        String expenseType = expense.get("expense_type");
        boolean isViolated = "entertainment".equals(expenseType);
        String reason = isViolated ? "Entertainment expense are not allowed" : "Not an Entertainment expense";

        return new RuleViolation(getRuleId(), getRuleDescription(), reason, isViolated);
    }

    @Override
    public String getRuleId() {
        return "NO_ENTERTAINMENT_EXPENSE";
    }

    @Override
    public String getRuleDescription() {
        return "Entertainment expenses are not allowed";
    }
}

class MaxAmountRule  implements Rule {
    private final double maxAmount;
    public MaxAmountRule(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public RuleViolation evaluate(Map<String, String> expense) {
        String amountStr =  expense.get("amount_usd");

        try {
            Double amount = Double.valueOf(amountStr);
            boolean isViolated = amount > maxAmount;
            String reason = isViolated ? String.format("Expense %.2f exceeds the limit %.2f", amount, maxAmount) :
                    String.format("Expense %.2f is within the limit %.2f", amount, maxAmount);

            return new RuleViolation(getRuleId(), getRuleDescription(), reason, isViolated);
        } catch (NumberFormatException e) {
            return new RuleViolation(getRuleId(), getRuleDescription(), "Invalid amount format", false);
        }
    }

    @Override
    public String getRuleId() {
        return "MAX_AMOUNT_EXPENSE";
    }

    @Override
    public String getRuleDescription() {
        return String.format("No expenses over %.2f", maxAmount);
    }
}

class ExpenseRuleEngine{
    public Map<String, ExpenseEvaluationResult> evaluateRules(List<Rule> rules,
                                                              List<Map<String, String>> expenses) {

        Map<String, ExpenseEvaluationResult> results = new LinkedHashMap<>();

        for (Map<String, String> expense : expenses) {
            String expenseId = expense.get("expense_id");
            List<RuleViolation> violations = new ArrayList<>();
            for (Rule rule : rules) {
                RuleViolation ruleViolation = rule.evaluate(expense);
                if (ruleViolation.isViolated()) {
                    violations.add(ruleViolation);
                }
            }

            if (!violations.isEmpty()) {
                results.put(expenseId, new ExpenseEvaluationResult(expenseId, violations));
            }
        }

        return results;
    }
}


//==============================================================================================//

interface TripLevelRule {
    TripRuleViolation evaluate(String tripId, List<Map<String, String>> tripExpenses);
    String getRuleId();
    String getRuleDescription();
}

class TripRuleViolation {
    private final String ruleId;
    private final String ruleDescription;
    private final String reason;
    private final boolean isViolated;
    private final double calculatedAmount;
    private final double limitAmount;
    public TripRuleViolation(String ruleId, String ruleDescription, String reason, boolean isViolated,
                             double calculatedAmount, double limitAmount) {
        this.ruleId = ruleId;
        this.ruleDescription = ruleDescription;
        this.reason = reason;
        this.isViolated = isViolated;
        this.calculatedAmount = calculatedAmount;
        this.limitAmount = limitAmount;
    }

    public String getRuleId() {
        return ruleId;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public String getReason() {
        return reason;
    }

    public boolean isViolated() {
        return isViolated;
    }

    public double getCalculatedAmount() {
        return calculatedAmount;
    }

    public double getLimitAmount() {
        return limitAmount;
    }

}

class TripEvaluationResult {
    private final String tripId;
    private final List<TripRuleViolation> violations;
    private final boolean hasViolation;
    private final double totalAmount;
    private final int expenseCount;

    public TripEvaluationResult(String tripId, List<TripRuleViolation> violations, double totalAmount, int expenseCount) {
        this.tripId = tripId;
        this.violations = violations;
        this.hasViolation = violations.stream().anyMatch(TripRuleViolation::isViolated);
        this.totalAmount = totalAmount;
        this.expenseCount = expenseCount;
    }

    public String getTripId() {
        return tripId;
    }

    public List<TripRuleViolation> getViolations() {
        return violations;
    }

    public boolean hasViolation() {
        return hasViolation;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getExpenseCount() {
        return expenseCount;
    }
}

class TripTotalAmountLimitRule implements TripLevelRule {
    private final double maxAmount;
    public TripTotalAmountLimitRule(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public TripRuleViolation evaluate(String tripId, List<Map<String, String>> tripExpenses) {
        double totalAmount = 0;
        for (Map<String, String> expense : tripExpenses) {
            String amount = expense.get("amount_usd");
            totalAmount += Double.parseDouble(amount);
        }

        boolean isViolated = totalAmount > maxAmount;
        String reason = isViolated ? String.format("Trip total $%.2f exceeds limit of $%.2f",  totalAmount, maxAmount) :
                String.format("Trip total $%.2f is within limit",  totalAmount);

        return new TripRuleViolation(getRuleId(), getRuleDescription(), reason, isViolated, totalAmount, maxAmount);
    }

    @Override
    public String getRuleId() {
        return "TRIP_TOTAL_AMOUNT_LIMIT";
    }

    @Override
    public String getRuleDescription() {
        return String.format("Trip total cannot exceed the limit of $%.2f", maxAmount);
    }
}

class TripMealAmountLimitRule implements TripLevelRule {
    private final double maxAmount;
    public TripMealAmountLimitRule(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public TripRuleViolation evaluate(String tripId, List<Map<String, String>> tripExpenses) {
        double totalAmount = 0;
        for (Map<String, String> expense : tripExpenses) {
            String expenseType = expense.get("expense_type");
            if("meal".equals(expenseType)) {
                String amount = expense.get("amount_usd");
                totalAmount += Double.parseDouble(amount);
            }
        }

        boolean isViolated = totalAmount > maxAmount;
        String reason = isViolated ? String.format("Trip meal total $%.2f exceeds limit of $%.2f",  totalAmount, maxAmount) :
                String.format("Trip meal total $%.2f is within limit",  totalAmount);

        return new TripRuleViolation(getRuleId(), getRuleDescription(), reason, isViolated, totalAmount, maxAmount);
    }

    @Override
    public String getRuleId() {
        return "TRIP_MEAL_TOTAL_AMOUNT_LIMIT";
    }

    @Override
    public String getRuleDescription() {
        return String.format("Trip meal total cannot exceed the limit of $%.2f", maxAmount);
    }
}





public class Problem {
    public static void main(String[] args) {
        // Initialize rules
        List<Rule> rules = Arrays.asList(
                new RestaurantAmountLimitRule(75.0),
                new NoAirFareRule(),
                new NoEntertainmentRule(),
                new MaxAmountRule(250.0)
        );

        // Your expense data
        List<Map<String, String>> expenses = createSampleExpenses();

        // Evaluate rules
        ExpenseRuleEngine engine = new ExpenseRuleEngine();
        Map<String, ExpenseEvaluationResult> results = engine.evaluateRules(rules, expenses);

        // Display results
        System.out.println("=== EXPENSE EVALUATION RESULTS ===");
        for (ExpenseEvaluationResult result : results.values()) {
            System.out.println("\nExpense ID: " + result.getExpenseId());
            System.out.println("Has Violations: " + result.hasViolations());

            for (RuleViolation violation : result.getViolations()) {
                if (violation.isViolated()) {
                    System.out.println("  ❌ VIOLATED: " + violation.getRuleDescription());
                    System.out.println("     Reason: " + violation.getReason());
                } else {
                    System.out.println("  ✅ PASSED: " + violation.getRuleDescription());
                }
            }
        }

        // Show only violating expenses
//        System.out.println("\n=== EXPENSES REQUIRING REVIEW ===");
//        Map<String, ExpenseEvaluationResult> violations = engine.getViolatingExpenses(rules, expenses);
//        for (ExpenseEvaluationResult result : violations.values()) {
//            System.out.println("Expense " + result.getExpenseId() + " requires review");
//        }
    }

    private static List<Map<String, String>> createSampleExpenses() {
        List<Map<String, String>> expenses = new ArrayList<>();
        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "001");
            put("trip_id", "001");
            put("amount_usd", "49.99");
            put("expense_type", "supplies");
            put("vendor_type", "restaurant");
            put("vendor_name", "Outback Roadhouse");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "002");
            put("trip_id", "001");
            put("amount_usd", "125.00");
            put("expense_type", "supplies");
            put("vendor_type", "retailer");
            put("vendor_name", "Staples");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "003");
            put("trip_id", "002");
            put("amount_usd", "153.00");
            put("expense_type", "meals");
            put("vendor_type", "restaurant");
            put("vendor_name", "Olive Yurt");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "004");
            put("trip_id", "002");
            put("amount_usd", "1996.00");
            put("expense_type", "airfare");
            put("vendor_type", "transportation");
            put("vendor_name", "Southeast Airlines");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "005");
            put("trip_id", "002");
            put("amount_usd", "34.68");
            put("expense_type", "meals");
            put("vendor_type", "restaurant");
            put("vendor_name", "The Great Grill");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "006");
            put("trip_id", "002");
            put("amount_usd", "22.40");
            put("expense_type", "meals");
            put("vendor_type", "restaurant");
            put("vendor_name", "The Great Grill");
        }});

        expenses.add(new HashMap<String, String>() {{
            put("expense_id", "007");
            put("trip_id", "003");
            put("amount_usd", "59.50");
            put("expense_type", "entertainment");
            put("vendor_type", "theater");
            put("vendor_name", "Silver Screen");
        }});

        return expenses;
    }
}
