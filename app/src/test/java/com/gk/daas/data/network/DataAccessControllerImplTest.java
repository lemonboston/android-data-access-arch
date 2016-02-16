package com.gk.daas.data.network;

import com.gk.daas.bus.Bus;
import com.gk.daas.data.event.GetTempSuccessEvent;
import com.gk.daas.data.event.RetryEvent;
import com.gk.daas.data.model.converter.ForecastConverter;
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
    private static final String CITY = "Berlin";
    private static final DataAccessError DATA_ACCESS_ERROR = DataAccessError.NO_OFFLINE_DATA;
    private static final Exception SOME_EXCEPTION = new Exception();
    private static final java.lang.Double TEMPERATURE = WEATHER_RESPONSE.main.temp;

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

        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        verify(taskCounter).taskFinished();
    }

    @Test
    public void testGetWeather_ErrorHandling_success() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.just(null));
        when(weatherService.getWeather(CITY, INVALID_API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.ERROR_HANDLING, CITY);

        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        verify(bus).post(event.capture());
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        verify(taskCounter).taskFinished();
    }

    @Test
    public void testGetWeather_ErrorHandling_noInternet() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.error(NO_INTERNET_EXCEPTION));
        when(errorInterpreter.interpret(NO_INTERNET_EXCEPTION)).thenReturn(DATA_ACCESS_ERROR);

        underTest.getWeather(UseCase.ERROR_HANDLING, CITY);

        verify(bus).post(DATA_ACCESS_ERROR);
        verify(taskCounter).taskFinished();
        verifyNoMoreInteractions(bus, taskCounter);
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
        verify(taskCounter).taskFinished();
        verifyNoMoreInteractions(bus, weatherService, taskCounter);
    }

    @Test
    public void testGetWeather_OfflineStore_firstCall() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.just(WEATHER_RESPONSE));

        underTest.getWeather(UseCase.OFFLINE_STORAGE, CITY);

        InOrder o = inOrder(weatherService, dataStore, bus, taskCounter);
        o.verify(weatherService).getWeather(CITY, API_KEY);
        o.verify(dataStore).saveAsync(CITY, WEATHER_RESPONSE.main.temp);
        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        o.verify(bus).post(event.capture());
        o.verify(taskCounter).taskFinished();
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        o.verifyNoMoreInteractions();
    }

    @Test
    public void testGetWeather_OfflineStore_fromDataStore() {
        when(weatherService.getWeather(CITY, API_KEY)).thenReturn(Single.error(SOME_EXCEPTION));
        when(dataStore.getTemperatureAsObservable(CITY)).thenReturn(Observable.just(TEMPERATURE));

        underTest.getWeather(UseCase.OFFLINE_STORAGE, CITY);

        InOrder o = inOrder(weatherService, dataStore, bus, taskCounter);
        o.verify(weatherService).getWeather(CITY, API_KEY);
        o.verify(dataStore).getTemperatureAsObservable(CITY);
        ArgumentCaptor<GetTempSuccessEvent> event = forClass(GetTempSuccessEvent.class);
        o.verify(bus).post(event.capture());
        o.verify(taskCounter).taskFinished();
        assertEquals(event.getValue().temperature.temperatureInKelvin, WEATHER_RESPONSE.main.temp, 0.00001);
        o.verifyNoMoreInteractions();
    }

    @Test
    // TODO Not working because of threading. Should call the uncommented verifications as well.
    public void testGetWeather_Retry() {
        when(connectionChecker.checkNetwork()).thenReturn(Single.just(null), Single.just(null), Single.just(null));
        when(weatherService.getWeather(CITY, INVALID_API_KEY)).thenReturn(Single.error(SOME_EXCEPTION), Single.error(SOME_EXCEPTION), Single.error(SOME_EXCEPTION));
        when(errorInterpreter.interpret(SOME_EXCEPTION)).thenReturn(DATA_ACCESS_ERROR);

        underTest.getWeather(UseCase.RETRY, CITY);

        InOrder o = inOrder(connectionChecker, weatherService, errorInterpreter, bus, taskCounter);

        o.verify(connectionChecker).checkNetwork();
        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        o.verify(bus).post(isA(RetryEvent.class));

        //        o.verify(connectionChecker).checkNetwork();
        //        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        //        o.verify(bus).post(isA(RetryEvent.class));
        //
        //        o.verify(connectionChecker).checkNetwork();
        //        o.verify(weatherService).getWeather(CITY, INVALID_API_KEY);
        //        o.verify(bus).post(isA(RetryEvent.class));

        //        o.verify(errorInterpreter).interpret(SOME_EXCEPTION);
        //        o.verify(bus).post(DATA_ACCESS_ERROR);
        //        o.verify(taskCounter).taskFinished();
        o.verifyNoMoreInteractions();
    }
}
