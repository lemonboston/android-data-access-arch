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

    COMBINED(R.string.UseCase_Combined),
    OFFLINE_STORAGE(R.string.UseCaseTitle_OfflineStorage),
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
