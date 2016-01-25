package com.gk.daas.log;

import com.gk.daas.di.AppComponent;

/**
 * @author Gabor_Keszthelyi
 */
public interface LogFactory {

    Log create(String tag);

    Log create(Class<?> classForTag);

    static Log createLog(Class<?> classForTag) {
        return AppComponent.Holder.getInstance().getLogFactory().create(classForTag);
    }


}
