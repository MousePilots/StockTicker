/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */


package org.mousepilots.demos.stockticker.rest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */
@Singleton
@LocalBean
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class Aex {
    
    private static final Object[][] RAW_STOCK_INFOS = {
        {"Aegon", 6.270, StockInfo.Trend.FLAT},
        {"Ahold", 14.105, StockInfo.Trend.FLAT},
        {"Akzo Nobel", 54.460, StockInfo.Trend.FLAT},
        {"Arcelor Mittal", 11.535, StockInfo.Trend.FLAT},
        {"ASML", 59.430, StockInfo.Trend.FLAT},
        {"Boskalis", 39.465, StockInfo.Trend.FLAT},
        {"Corio", 35.875, StockInfo.Trend.FLAT},
        {"Delta Lloyd Groep", 18.335, StockInfo.Trend.FLAT},
        {"DSM", 52.460, StockInfo.Trend.FLAT},
        {"Fugro", 41.445, StockInfo.Trend.FLAT},
        {"Gemalto", 79.300, StockInfo.Trend.FLAT},
        {"Heineken", 51.770, StockInfo.Trend.FLAT},
        {"ING", 9.460, StockInfo.Trend.FLAT},
        {"KPN", 2.680, StockInfo.Trend.FLAT},
        {"OCI", 28.770, StockInfo.Trend.FLAT},
        {"Philips", 23.100, StockInfo.Trend.FLAT},
        {"Randstad", 40.180, StockInfo.Trend.FLAT},
        {"Reed Elsevier", 15.590, StockInfo.Trend.FLAT},
        {"SBM", 12.435, StockInfo.Trend.FLAT},
        {"Shell", 28.705, StockInfo.Trend.FLAT},
        {"TNT Express", 6.420, StockInfo.Trend.FLAT},
        {"Unibail-Rodamco", 197.950, StockInfo.Trend.FLAT},
        {"Unilever", 31.890, StockInfo.Trend.FLAT},
        {"Wolters Kluwer", 20.850, StockInfo.Trend.FLAT},
        {"ZIGGO", 33.520, StockInfo.Trend.FLAT}};
    
    private final Set<AexListener> listeners = new ConcurrentSkipListSet<>();
    private final List<StockInfo> stockInfos;
    
    public Aex(){
        List<StockInfo> infos = new ArrayList<>();
        this.stockInfos = Collections.unmodifiableList(infos);
        for(Object[] rawStockInfo : RAW_STOCK_INFOS){
            final StockInfo stockInfo = new StockInfo();
            stockInfo.setStock((String) rawStockInfo[0]);
            stockInfo.setPrice(BigDecimal.valueOf((double)rawStockInfo[1]));
            stockInfo.setTrend((StockInfo.Trend) rawStockInfo[2]);
            infos.add(stockInfo);
        }
    }

    @Schedule(persistent = false, hour = "*",minute = "*",second = "*/3")
    protected void randomUpdate(){
        //choose a random stock to update
        final int index = (int) Math.round((RAW_STOCK_INFOS.length-1) * Math.random());
        final StockInfo stockInfo = this.stockInfos.get(index);
        
        //perform an update in the range [-5%,5%]
        final BigDecimal percentage = BigDecimal.valueOf( Math.random()* .05);
        if(percentage.equals(BigDecimal.ZERO)){
            //no change in stock price
            stockInfo.setTrend(StockInfo.Trend.FLAT);
        } else {
            
            final BigDecimal summand = stockInfo.getPrice().multiply(percentage);
            
            //randomly let the stock go up or down
            if(Math.random()<0.5){
                stockInfo.setTrend(StockInfo.Trend.DOWN);
                stockInfo.setPrice(
                    stockInfo.getPrice()
                    .subtract(summand)
                    .setScale(3,RoundingMode.UP)
                );
            } else {
                stockInfo.setTrend(StockInfo.Trend.UP);
                stockInfo.setPrice(
                    stockInfo.getPrice()
                    .add(summand)
                    .setScale(3,RoundingMode.UP)
                );
            }
        }
        
        //notify listeners
        for(AexListener l : listeners){
            l.onUpdate(stockInfo);
        }
    }

    
    /**
     * @return the unmodifiable AEX's list of stocks
     */
    public List<StockInfo> getStockInfos() {
        return stockInfos;
    }
    
    public boolean removeListener(AexListener l){
        return this.listeners.remove(l);
    }
    
    public void addListener(AexListener l){
        this.listeners.add(l);
    }
    
}
