/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */

package org.mousepilots.demos.stockticker.rest;

import java.util.Objects;


/**
 * Abstract base for listening to {@link Aex} updates.
 * 
 * @author geenenju
 */
public abstract class AexListener implements Comparable<AexListener>{
    
    private static long ID_GENERATOR=0;
    
    private final Long id = ID_GENERATOR++;
    
    /**
     * Invoked when a new {@link StockInfo} is available
     * @param stockInfo the {@link Aex} update
     */
    public abstract void onUpdate(StockInfo stockInfo);
    
    @Override
    public final int compareTo(AexListener o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AexListener other = (AexListener) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
}
