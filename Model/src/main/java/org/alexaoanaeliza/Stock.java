package org.alexaoanaeliza;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    public Stock(Long id, String name, String companyName) {
        super(id);
        this.name = name;
        this.companyName = companyName;
        this.prices = new HashMap<>();
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
        Optional<Map.Entry<LocalDate, Double>> price = prices.entrySet().stream().max(Map.Entry.comparingByKey());
        if (price.isPresent())
            return price.get().getValue();
        return 0D;
    }

    public void addPrice(Double price) {
        prices.put(LocalDate.now(), price);
    }
}
