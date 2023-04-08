package org.alexaoanaeliza;

import java.time.LocalDate;
import java.util.*;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Stock stock = (Stock) o;
        return Objects.equals(name, stock.name) && Objects.equals(companyName, stock.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, companyName);
    }
}
