package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.core.Config;
import com.gk.daas.data.event.GetForecastProgressEvent;
import com.gk.daas.data.event.GetForecastSuccessEvent;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.event.RetryEvent;
import com.gk.daas.data.model.client.Forecast;
import com.gk.daas.data.model.client.Temperature;
import com.gk.daas.data.model.converter.ForecastConverter;
import com.gk.daas.data.model.server.ForecastResponse;
import com.gk.daas.data.model.server.MockData;
import com.gk.daas.data.model.server.WeatherResponse;
import com.gk.daas.data.network.connection.NetworkConnectionChecker;
import com.gk.daas.data.network.connection.NoInternetException;
import com.gk.daas.data.store.DataStore;
import com.gk.daas.log.Log;
import com.gk.daas.log.LogFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import static com.gk.daas.data.model.server.MockData.INVALID_API_KEY;
import static com.gk.daas.data.network.OpenWeatherService.API_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Note: EasyMock might be better suited than Mockito for this much of mock verification.
 *
 * @author Gabor_Keszthelyi
 */
@RunWith(MockitoJUnitRunner.class)
public class DataAccessControllerImplTest {

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    private static final NoInternetException NO_INTERNET_EXCEPTION = new NoInternetException();

    private static final WeatherResponse WEATHER_RESPONSE = WeatherResponse.createFromTemp(18);
    private static final WeatherResponse WEATHER_RESPONSE_2 = WeatherResponse.createFromTemp(44);
    private static final String CITY = "Berlin";
    private static final DataAccessError DATA_ACCESS_ERROR = DataAccessError.NO_OFFLINE_DATA;
    private static final Exception SOME_EXCEPTION = new Exception();
    private static final java.lang.Double TEMPERATURE = WEATHER_RESPONSE.main.temp;
    private static final String CITY2 = "New York";

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
        underTest = new DataAccessControllerImpl(weatherService, logFactory, bus, connectionChecker, dataStore, errorInterpreter, forecastConverter, scheduler);
    }

    @Test
    public void testGetWeather_Basic() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.BASIC, CITY);

        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
    }

    @Test
    public void testGetWeather_ErrorHandling_success() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.just(null));
        when(weatherService.getWeather(CITY, INVALID_API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.ERROR_HANDLING, CITY);

        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
    }

    @Test
    public void testGetWeather_ErrorHandling_noInternet() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.error(NO_INTERNET_EXCEPTION));
        when(errorInterpreter.interpret(NO_INTERNET_EXCEPTION)).thenReturn(DATA_ACCESS_ERROR);

        underTest.getWeather(UseCase.ERROR_HANDLING, CITY);

        verify(bus).post(DATA_ACCESS_ERROR);
        verifyNoMoreInteractions(bus);
        verifyZeroInteractions(weatherService);
    }

    @Test
    public void testGetWeather_ErrorHandling_apiError() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.just(null));
        when(weatherService.getWeather(CITY, INVALID_API_KEY)).thenReturn(Single.error(SOME_EXCEPTION));
        when(errorInterpreter.interpret(SOME_EXCEPTION)).thenReturn(DATA_ACCESS_ERROR);

        underTest.getWeather(UseCase.ERROR_HANDLING, CITY);

        verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        verify(bus).post(DATA_ACCESS_ERROR);
        verifyNoMoreInteractions(bus, weatherService);
    }

    @Test
    public void testGetWeather_OfflineStore_firstCall() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.OFFLINE_STORAGE, CITY);

        InOrder o = inOrder(weatherService, dataStore, bus);
        o.verify(weatherService).getWeather(CITY, API_KEY);
        o.verify(dataStore).saveAsync(CITY, WEATHER_RESPONSE.main.temp);
        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        o.verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        o.verifyNoMoreInteractions();
    }

    @Test
    public void testGetWeather_OfflineStore_fromDataStore() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.error(SOME_EXCEPTION));
        when(dataStore.getTemperatureAsObservable(CITY)).thenReturn(Observable.just(TEMPERATURE));

        underTest.getWeather(UseCase.OFFLINE_STORAGE, CITY);

        InOrder o = inOrder(weatherService, dataStore, bus);
        o.verify(weatherService).getWeather(CITY, API_KEY);
        o.verify(dataStore).getTemperatureAsObservable(CITY);
        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        o.verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        o.verifyNoMoreInteractions();
    }

    @Test
    public void testGetWeather_Retry() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.just(null));
        when(weatherService.getWeather(CITY, INVALID_API_KEY)).thenReturn(Single.error(SOME_EXCEPTION));
        when(errorInterpreter.interpret(SOME_EXCEPTION)).thenReturn(DATA_ACCESS_ERROR);

        underTest.getWeather(UseCase.RETRY, CITY);

        // Wait until retries complete.
        sleep((Config.RETRY_COUNT + 1) * Config.RETRY_DELAY_SECONDS * 1000);

        InOrder o = inOrder(connectionChecker, weatherService, errorInterpreter, bus);
        o.verify(connectionChecker).checkNetwork();
        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        o.verify(bus).post(isA(RetryEvent.class));

        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        o.verify(bus).post(isA(RetryEvent.class));

        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        o.verify(bus).post(isA(RetryEvent.class));

        o.verify(errorInterpreter).interpret(SOME_EXCEPTION);
        o.verify(bus).post(DATA_ACCESS_ERROR);
        o.verifyNoMoreInteractions();
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetForecast_happyPath() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));
        when(weatherService.getWeather(CITY2, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE_2));
        ForecastResponse forecastResponse = MockData.forecastResponse(CITY2, WEATHER_RESPONSE_2.main.temp);
        when(weatherService.getForecast(CITY2, API_KEY)).thenReturn(Single.just(forecastResponse));
        Forecast forecast = new Forecast(CITY2, new Temperature(WEATHER_RESPONSE_2.main.temp));
        when(forecastConverter.convert(forecastResponse)).thenReturn(forecast);

        underTest.getForecastForWarmerCity(CITY, CITY2);

        InOrder o = inOrder(weatherService, bus, forecastConverter);
        o.verify(weatherService).getWeather(CITY, API_KEY);
        o.verify(weatherService).getWeather(CITY2, API_KEY);
        o.verify(bus).postSticky(isA(GetForecastProgressEvent.class));
        o.verify(weatherService).getForecast(CITY2, API_KEY);
        o.verify(forecastConverter).convert(forecastResponse);
        o.verify(bus).post(isA(GetForecastSuccessEvent.class));
        o.verify(bus).postSticky(isA(GetForecastProgressEvent.class));
        o.verifyNoMoreInteractions();
    }
}
