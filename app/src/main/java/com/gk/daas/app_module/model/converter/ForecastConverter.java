package com.gk.daas.app_module.model.converter;

import com.gk.daas.app_module.model.Forecast;
import com.gk.daas.app_module.model.Temperature;
import com.gk.daas.network_module.data.ForecastResponse;

/**
 * @author Gabor_Keszthelyi
 */
public class ForecastConverter {

    public Forecast convert(ForecastResponse response) {
        // Note: no validation, unit testing, etc. in this PoC
        String city = response.city.name;
        double lastTemp = response.list.get(response.list.size() - 1).main.temp;
        return new Forecast(city, new Temperature(lastTemp));
    }
}
