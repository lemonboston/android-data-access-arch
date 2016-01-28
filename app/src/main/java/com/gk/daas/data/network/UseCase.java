package com.gk.daas.data.network;

import android.content.Context;
import android.support.annotation.StringRes;

import com.gk.daas.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabor_Keszthelyi
 */
public enum UseCase {

    EMPTY_PLACEHOLDER(R.string.UseCase_Choose),
    BASIC(R.string.UseCaseTitle_Basic),
    ERROR_HANDLING(R.string.UseCaseTitle_ErrorHandling),
    ONGOING_CALL_HANDLING(R.string.UseCaseTitle_OnGoingCall),
    OFFLINE_STORAGE(R.string.UseCaseTitle_OfflineStorage),
    COMBINED(R.string.UseCase_Combined),
    PARALLEL_AND_CHAINED(R.string.UseCaseTitle_ParallelChained);

    @StringRes
    private final int titleResId;

    UseCase(@StringRes int titleResId) {
        this.titleResId = titleResId;
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
