package it.fstarred.simplerest.provider;

import javax.annotation.Priority;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.config.PropertyOrderStrategy;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.Locale;

@Provider
@Priority(Priorities.ENTITY_CODER)
public class JSONBConfiguration implements ContextResolver<Jsonb> {

    private final Jsonb jsonb;

    public JSONBConfiguration() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true)
                .withNullValues(true)
                .withPropertyNamingStrategy(PropertyNamingStrategy.IDENTITY)
                .withPropertyOrderStrategy(PropertyOrderStrategy.ANY)
                .withDateFormat("yyyy-MM-dd", Locale.ITALY);

        jsonb = JsonbBuilder.create(config);
    }

    @Override
    public Jsonb getContext(Class<?> type) {
        return jsonb;
    }

}
