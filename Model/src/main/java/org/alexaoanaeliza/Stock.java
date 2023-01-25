package org.alexaoanaeliza;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Stock extends Entity<Long> {
    private final String name;
    private final String companyName;
    private final Map<LocalDate, Double> prices;

    public Stock(Long id, String name, String companyName, Map<LocalDate, Double> prices) {
        super(id);
        this.name = name;
        this.companyName = companyName;
        this.prices = prices;
    }

    public Stock(String name, String companyName) {
        super(0L);
        this.name = name;
        this.companyName = companyName;
        this.prices = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Map<LocalDate, Double> getPrices() {
        return prices;
    }

    public Double getCurrentPrice() {
        return prices.entrySet().stream().max((o1, o2) -> o2.getKey().compareTo(o1.getKey())).get().getValue();
    }

    public void addPrice(Double price) {
        prices.put(LocalDate.now(), price);
    }
}
