package cn.edu.njnu.geoproblemsolving.business.activity.enums;

import lombok.Getter;

/**
 * The different style of activity implementation
 * Activity_Unit: single activity
 * Activity_Group: contains several child activities
 */
@Getter
public enum ActivityType {

    Activity_Default,
    Activity_Unit,
    Activity_Group,
}
