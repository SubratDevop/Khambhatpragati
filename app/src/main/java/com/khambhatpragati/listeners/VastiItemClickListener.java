package com.khambhatpragati.listeners;

import android.view.View;

import com.khambhatpragati.model.VastiModel;
import com.khambhatpragati.model.response.GroupsBean;

/**
 * Created by bhagvatee1.gupta on 09-03-2017.
 */

public interface VastiItemClickListener {
    void onClick(View view, int position, VastiModel groupsBean);
}
