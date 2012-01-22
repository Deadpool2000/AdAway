/*
 * Copyright (C) 2011 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * This file is part of AdAway.
 * 
 * AdAway is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AdAway is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AdAway.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.adaway.ui;

import org.adaway.R;
import org.adaway.helper.RevertHelper;
import org.adaway.helper.OpenHostsFileHelper;
import org.adaway.service.ApplyService;
import org.adaway.service.UpdateService;
import org.adaway.util.StatusCodes;

import com.commonsware.cwac.wakeful.WakefulIntentService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseFragment extends Fragment {
    private Activity mActivity;

    private TextView mStatusTitle;
    private TextView mStatusText;
    private ProgressBar mStatusProgress;
    private ImageView mStatusIcon;

    private void setStatusIcon(int iconStatus) {
        switch (iconStatus) {
        case StatusCodes.UPDATE_AVAILABLE:
            mStatusProgress.setVisibility(View.GONE);
            mStatusIcon.setVisibility(View.VISIBLE);
            mStatusIcon.setImageResource(R.drawable.status_update);
            break;
        case StatusCodes.ENABLED:
            mStatusProgress.setVisibility(View.GONE);
            mStatusIcon.setVisibility(View.VISIBLE);
            mStatusIcon.setImageResource(R.drawable.status_enabled);
            break;
        case StatusCodes.DISABLED:
            mStatusProgress.setVisibility(View.GONE);
            mStatusIcon.setVisibility(View.VISIBLE);
            mStatusIcon.setImageResource(R.drawable.status_disabled);
            break;
        case StatusCodes.DOWNLOAD_FAIL:
            mStatusProgress.setVisibility(View.GONE);
            mStatusIcon.setImageResource(R.drawable.status_fail);
            mStatusIcon.setVisibility(View.VISIBLE);
            break;
        case StatusCodes.CHECKING:
            mStatusProgress.setVisibility(View.VISIBLE);
            mStatusIcon.setVisibility(View.GONE);
            break;

        default:
            break;
        }
    }

    public void setStatus(String title, String text, int iconStatus) {
        mStatusTitle.setText(title);
        mStatusText.setText(text);
        setStatusIcon(iconStatus);
    }

    /**
     * Inflate Menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.base, menu);
    }

    /**
     * Menu Options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_hosts_sources:
            startActivity(new Intent(mActivity, HostsSourcesActivity.class));
            return true;

        case R.id.menu_lists:
            startActivity(new Intent(mActivity, ListsActivity.class));
            return true;

        case R.id.menu_show_hosts_file:
            OpenHostsFileHelper.openHostsFile(mActivity);
            return true;

        case R.id.menu_preferences:
            startActivity(new Intent(mActivity, PrefsActivity.class));
            return true;

        case R.id.menu_donations:
            startActivity(new Intent(mActivity, DonationsActivity.class));
            return true;

        case R.id.menu_about:
            startActivity(new Intent(mActivity, AboutActivity.class));
            return true;

        case R.id.menu_refresh:
            Intent updateIntent = new Intent(mActivity, UpdateService.class);
            updateIntent.putExtra(UpdateService.EXTRA_APPLY_AFTER_CHECK, false);
            WakefulIntentService.sendWakefulWork(mActivity, updateIntent);
            return true;

        case R.id.menu_help:
            startActivity(new Intent(mActivity, HelpActivity.class));
            return true;

        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Inflate the layout for this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_fragment, container, false);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        mStatusTitle = (TextView) mActivity.findViewById(R.id.status_title);
        mStatusText = (TextView) mActivity.findViewById(R.id.status_text);
        mStatusProgress = (ProgressBar) mActivity.findViewById(R.id.status_progress);
        mStatusIcon = (ImageView) mActivity.findViewById(R.id.status_icon);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // enable options menu for this fragment
    }

    /**
     * Button Action to download and apply hosts files
     * 
     * @param view
     */
    public void applyOnClick(View view) {
        WakefulIntentService.sendWakefulWork(mActivity, ApplyService.class);
    }

    /**
     * Button Action to Revert to default hosts file
     * 
     * @param view
     */
    public void revertOnClick(View view) {
        RevertHelper.revert(mActivity);
    }

}