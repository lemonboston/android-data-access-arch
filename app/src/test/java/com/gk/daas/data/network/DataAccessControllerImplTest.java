package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.model.converter.ForecastConverter;
import com.gk.daas.data.model.server.WeatherResponse;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import static com.gk.daas.data.network.OpenWeatherService.API_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Gabor_Keszthelyi
 */
@RunWith(MockitoJUnitRunner.class)
public class DataAccessControllerImplTest {

    private static final WeatherResponse WEATHER_RESPONSE = WeatherResponse.createFromTemp(18);
    private static final String CITY = "Berlin";

    @Mock
    OpenWeatherService weatherService;
    @Mock
    LogFactory logFactory;
    @Mock
    Bus bus;
    @Mock
    NetworkConnectionChecker connectionChecker;
    @Mock
    DataStore dataStore;
    @Mock
    TaskCounter taskCounter;
    @Mock
    ErrorInterpreter errorInterpreter;
    @Mock
    ForecastConverter forecastConverter;
    @Mock
    Log log;

    @InjectMocks
    DataAccessControllerImpl underTest;

    private Scheduler scheduler = Schedulers.immediate();

    @Before
    public void setup() {
        when(logFactory.create(DataAccessControllerImpl.class)).thenReturn(log);
        underTest = new DataAccessControllerImpl(weatherService, logFactory, bus, connectionChecker, dataStore, taskCounter, errorInterpreter, forecastConverter, scheduler);
    }

    @Test
    public void testGetWeather_Basic() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.BASIC, CITY);

        ArgumentCaptor<GetTempSuccessEvent> event = ArgumentCaptor.forClass(GetTempSuccessEvent.class);
        verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        verify(taskCounter).taskFinished();
    }
}
