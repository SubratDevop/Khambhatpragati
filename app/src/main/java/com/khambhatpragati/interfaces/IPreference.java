package com.khambhatpragati.interfaces;

/**
 * Created by hp on 23/01/2016.
 */
public interface IPreference {
    Object getPreference();
    void savePreference(Object oject);
    boolean isPreferenceExist();
    void clearPreference();
}
