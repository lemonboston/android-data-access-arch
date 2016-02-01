package com.gk.daas.data.model.converter;

import com.gk.daas.data.model.client.Forecast;
import com.gk.daas.data.model.client.Temperature;
import com.gk.daas.data.model.server.ForecastResponse;

/**
 * @author Gabor_Keszthelyi
 */
public class ForecastConverter {

    public Forecast convert(ForecastResponse response) {
        // Note: no validation in this PoC
        String city = response.city.name;
        double lastTemp = response.list.get(response.list.size() - 1).main.temp;
        return new Forecast(city, new Temperature(lastTemp));
    }
}
