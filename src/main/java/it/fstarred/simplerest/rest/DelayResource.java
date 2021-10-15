package it.fstarred.simplerest.rest;

import it.fstarred.simplerest.service.MyService;

import javax.inject.Inject;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.text.MessageFormat;

@Path("/delay")
public class DelayResource {

    @Inject
    MyService service;

    @GET
    public String delay(@QueryParam("time") @Min(1) @Max(5) @DefaultValue("3") int time) {
        service.sleep(time);
        return MessageFormat.format("waited for {0} seconds", time);
    }

}
