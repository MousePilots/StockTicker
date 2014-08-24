/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */


package org.mousepilots.demos.stockticker.rest;

import java.util.logging.Logger;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.CompletionCallback;

/**
 *
 * @author geenenju
 */
class AexResourceListener extends AexListener implements CompletionCallback {
    
    private static final Logger LOG = Logger.getLogger(AexResourceListener.class.getName());
    
    private final Aex aex;
    
    private final AsyncResponse asyncResponse;

    public AexResourceListener(Aex aex, AsyncResponse asyncResponse) {
        this.aex = aex;
        this.asyncResponse = asyncResponse;
    }

    public void register() {
        asyncResponse.register(this);
        aex.addListener(this);
    }
    
    @Override
    public void onUpdate(StockInfo item) {
        asyncResponse.resume(item);
        //LOG.log(Level.INFO, "pushed {0} to {1}", new Object[]{item, asyncResponse});
    }

    @Override
    public void onComplete(Throwable throwable) {
        aex.removeListener(this);
    }
}
