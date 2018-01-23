package com.ssh.sm2_update.bean.qtBean;

import java.io.Serializable;
import java.util.List;

/**
 * Description：
 * Created by fyx on 6/5/2017.
 * Version：
 */
public class QtMediacenterPageDataRadiostations implements Serializable {
    private List<QtMediacenterPageDataRadiostationsMediacentersItem> mediacenters;
    private String name;
    private String partition_by;
    private String preference_change_cost;

    public List<QtMediacenterPageDataRadiostationsMediacentersItem> getMediacenters() {
        return mediacenters;
    }

    public void setMediacenters(List<QtMediacenterPageDataRadiostationsMediacentersItem> mediacenters) {
        this.mediacenters = mediacenters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartition_by() {
        return partition_by;
    }

    public void setPartition_by(String partition_by) {
        this.partition_by = partition_by;
    }

    public String getPreference_change_cost() {
        return preference_change_cost;
    }

    public void setPreference_change_cost(String preference_change_cost) {
        this.preference_change_cost = preference_change_cost;
    }
}
