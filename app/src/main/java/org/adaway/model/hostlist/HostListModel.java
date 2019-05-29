package org.adaway.model.hostlist;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.adaway.model.error.HostErrorException;

public abstract class HostListModel {
    /**
     * The application context.
     */
    protected final Context context;
    /**
     * The model state.
     */
    protected MutableLiveData<String> state;
    /**
     * The hosts installation status:
     * <ul>
     *     <li>{@code null} if not defined,</li>
     *     <li>{@code true} if hosts list is installed,</li>
     *     <li>{@code false} if default hosts file.</li>
     * </ul>
     */
    protected final MutableLiveData<Boolean> applied;

//    /**
//     * The model detailed state.
//     */
//    protected String detailedState;

    /**
     * Constructor.
     *
     * @param context The application context.
     */
    public HostListModel(Context context) {
        this.context = context;
        this.state = new MutableLiveData<>();
        this.applied = new MutableLiveData<>();
//        this.detailedState = "";
    }

    /**
     * Checks if hosts list is applied.
     *
     * @return {@code true} if applied, {@code false} if default.
     */
    public @NonNull LiveData<Boolean> isApplied() {
        return this.applied;
    }

    /**
     * Apply hosts list.
     *
     * @throws HostErrorException If the hosts file could not be applied. // TODO
     */
    public abstract void apply() throws HostErrorException;

    /**
     * Revert the hosts list to the default one.
     *
     * @throws HostErrorException If the hosts file could not be applied. // TODO
     */
    public abstract void revert() throws HostErrorException;

    /**
     * Get the model state.
     *
     * @return The model state.
     */
    public LiveData<String> getState() {
        return this.state;
    }
//
//    /**
//     * Get the model detailed state.
//     *
//     * @return The model detailed state.
//     */
//    public String getDetailedState() {
//        return this.detailedState;
//    }

    protected void setStateAndDetails(@StringRes int stateResId, @StringRes int detailsResId) {
        this.setStateAndDetails(stateResId, this.context.getString(detailsResId));
    }

    protected void setStateAndDetails(@StringRes int stateResId, String details) {
        this.state.postValue(this.context.getString(stateResId));
//        this.detailedState = details;
//        this.setChanged();
//        this.notifyObservers();
    }

}
