/*
 * Copyright 2014 Jurjen van Geenen. All rights reserved.
 */

package org.mousepilots.demos.stockticker.rest;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

/**
 *
 * @author geenenju
 */
@Path("/aex")
public class AexResource{

    @Inject
    private Aex aex;
    
    /**
     * @return all current stock info from `the' {@link Aex}
     */
    @GET
    @Path("/stockInfos")
    public List<StockInfo> getStockInfos(){
        return aex.getStockInfos();
    }
    /**
     * listen to `the' {@link Aex} for updates. Asynchronously writes a 
     * a {@link StockInfo} in the negotiated format (JSON/XML) to {@code asyncResponse}.
     * @param asyncResponse the suspended respons (injected by JAX-RS)
     */
    @GET
    @Path("/listen")
    public void listen(final @Suspended AsyncResponse asyncResponse){
        new AexResourceListener(aex,asyncResponse).register();
    }
}
