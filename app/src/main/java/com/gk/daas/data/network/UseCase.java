package com.gk.daas.data.network;

import android.content.Context;
import android.support.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import static com.gk.daas.R.string.GetForecast_Button;
import static com.gk.daas.R.string.GetTemp_Button;
import static com.gk.daas.R.string.StartSync_Button;
import static com.gk.daas.R.string.UseCase_Basic_Description;
import static com.gk.daas.R.string.UseCase_Basic_Title;
import static com.gk.daas.R.string.UseCase_Cancellable_HowToTest;
import static com.gk.daas.R.string.UseCase_Cancellable_Implementation;
import static com.gk.daas.R.string.UseCase_Cancellable_Requirement;
import static com.gk.daas.R.string.UseCase_Cancellable_Title;
import static com.gk.daas.R.string.UseCase_Choose;
import static com.gk.daas.R.string.UseCase_Combined;
import static com.gk.daas.R.string.UseCase_Combined_Requirement;
import static com.gk.daas.R.string.UseCase_DoubleLoad_HowToTest;
import static com.gk.daas.R.string.UseCase_DoubleLoad_Implementation;
import static com.gk.daas.R.string.UseCase_DoubleLoad_Requirement;
import static com.gk.daas.R.string.UseCase_DoubleLoad_Title;
import static com.gk.daas.R.string.UseCase_ErrorHandling_Desc;
import static com.gk.daas.R.string.UseCase_ErrorHandling_HowToTest;
import static com.gk.daas.R.string.UseCase_ErrorHandling_Implementation;
import static com.gk.daas.R.string.UseCase_ErrorHandling_Title;
import static com.gk.daas.R.string.UseCase_OfflineStorage_HowToTest;
import static com.gk.daas.R.string.UseCase_OfflineStorage_Implementation;
import static com.gk.daas.R.string.UseCase_OfflineStorage_Requirement;
import static com.gk.daas.R.string.UseCase_OfflineStorage_Title;
import static com.gk.daas.R.string.UseCase_OnGoingCall_Title;
import static com.gk.daas.R.string.UseCase_OngoingCall_Desc;
import static com.gk.daas.R.string.UseCase_OngoingCall_HowToTest;
import static com.gk.daas.R.string.UseCase_OngoingCall_Implementation;
import static com.gk.daas.R.string.UseCase_ParallelAndChained_Implementation;
import static com.gk.daas.R.string.UseCase_ParallelAndChained_Requirement;
import static com.gk.daas.R.string.UseCase_ParallelChained_Title;
import static com.gk.daas.R.string.UseCase_Retry_HowToTest;
import static com.gk.daas.R.string.UseCase_Retry_Implementation;
import static com.gk.daas.R.string.UseCase_Retry_Requirement;
import static com.gk.daas.R.string.UseCase_Retry_Title;
import static com.gk.daas.R.string.UseCase_Sync_HowToTest;
import static com.gk.daas.R.string.UseCase_Sync_Implementation;
import static com.gk.daas.R.string.UseCase_Sync_Requirement;
import static com.gk.daas.R.string.UseCase_Sync_Title;
import static com.gk.daas.R.string.WeatherUseCase_GetForecast;
import static com.gk.daas.R.string.WeatherUseCase_GetTemp;
import static com.gk.daas.R.string.WeatherUseCase_Sync;

/**
 * @author Gabor_Keszthelyi
 */
public enum UseCase {

    EMPTY_PLACEHOLDER(
            UseCase_Choose,
            0,
            0,
            0,
            0,
            0),
    BASIC(
            UseCase_Basic_Title,
            UseCase_Basic_Description,
            0,
            0,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    ERROR_HANDLING(
            UseCase_ErrorHandling_Title,
            UseCase_ErrorHandling_Desc,
            UseCase_ErrorHandling_Implementation,
            UseCase_ErrorHandling_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    ONGOING_CALL_HANDLING(
            UseCase_OnGoingCall_Title,
            UseCase_OngoingCall_Desc,
            UseCase_OngoingCall_Implementation,
            UseCase_OngoingCall_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    OFFLINE_STORAGE(
            UseCase_OfflineStorage_Title,
            UseCase_OfflineStorage_Requirement,
            UseCase_OfflineStorage_Implementation,
            UseCase_OfflineStorage_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    RETRY(
            UseCase_Retry_Title,
            UseCase_Retry_Requirement,
            UseCase_Retry_Implementation,
            UseCase_Retry_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    CANCELLABLE(
            UseCase_Cancellable_Title,
            UseCase_Cancellable_Requirement,
            UseCase_Cancellable_Implementation,
            UseCase_Cancellable_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    COMBINED(
            UseCase_Combined,
            UseCase_Combined_Requirement,
            0,
            0,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    DOUBLE_LOAD(
            UseCase_DoubleLoad_Title,
            UseCase_DoubleLoad_Requirement,
            UseCase_DoubleLoad_Implementation,
            UseCase_DoubleLoad_HowToTest,
            WeatherUseCase_GetTemp,
            GetTemp_Button),
    PARALLEL_AND_CHAINED(
            UseCase_ParallelChained_Title,
            UseCase_ParallelAndChained_Requirement,
            UseCase_ParallelAndChained_Implementation,
            0,
            WeatherUseCase_GetForecast,
            GetForecast_Button),
    SYNC(
            UseCase_Sync_Title,
            UseCase_Sync_Requirement,
            UseCase_Sync_Implementation,
            UseCase_Sync_HowToTest,
            WeatherUseCase_Sync,
            StartSync_Button
    );

    private final int titleResId;

    public final int technicalDesc;
    public final int implementationDesc;
    public final int testDesc;
    public final int weatherUseCase;
    public final int button;

    UseCase(@StringRes int title,
            @StringRes int technicalDesc,
            @StringRes int implementationDesc,
            @StringRes int testDesc,
            @StringRes int weatherUseCase,
            @StringRes int button) {
        this.titleResId = title;
        this.technicalDesc = technicalDesc;
        this.implementationDesc = implementationDesc;
        this.testDesc = testDesc;
        this.weatherUseCase = weatherUseCase;
        this.button = button;
    }

    public static List<String> getTitles(Context context) {
        List<String> titles = new ArrayList<>();
        for (UseCase useCase : values()) {
            titles.add(context.getString(useCase.titleResId));
        }
        return titles;
    }

    public static UseCase get(int position) {
        return UseCase.values()[position];
    }
}
