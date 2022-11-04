package org.alexaoanaeliza;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;

public class Stock extends Entity<Long> {
    private final String name;
    private final String companyName;
    private Map<LocalDateTime, Double> prices;

    public Stock(Long id, String name, String companyName) {
        super(id);
        this.name = name;
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Map<LocalDateTime, Double> getPrices() {
        return prices;
    }

    public Double getCurrentPrice() {
        return prices.entrySet().stream().max((o1, o2) -> o2.getKey().compareTo(o1.getKey())).get().getValue();
    }

    public void addPrice(Double price) {
        prices.put(LocalDateTime.now(), price);
    }
}
