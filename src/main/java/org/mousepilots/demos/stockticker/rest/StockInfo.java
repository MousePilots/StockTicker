/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */

package org.mousepilots.demos.stockticker.rest;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains information on a particular {@link StockInfo#stock}
 * @author geenenju
 */
@XmlRootElement
public class StockInfo implements Serializable, Comparable<StockInfo> {

    public static enum Trend {
        DOWN, FLAT, UP
    };

    private String stock;

    private BigDecimal price;

    private Trend trend;

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Trend getTrend() {
        return trend;
    }

    public void setTrend(Trend trend) {
        this.trend = trend;
    }
    
    @Override
    public int compareTo(StockInfo o) {
        return this.stock.compareTo(o.stock);
    }

    @Override
    public String toString() {
        return "StockInfo{" + "stock=" + stock + ", price=" + price + ", trend=" + trend + '}';
    }
    
    

}
