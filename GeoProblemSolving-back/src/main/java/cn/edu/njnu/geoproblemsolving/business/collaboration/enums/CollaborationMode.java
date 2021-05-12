package cn.edu.njnu.geoproblemsolving.business.collaboration.enums;

import lombok.Getter;

/**
 * The different style of activity implementation
 * Activity_Unit: single activity
 * Activity_Group: contains several child activities
 */
@Getter
public enum CollaborationMode {
    Free,
    SemiFree_Occupy,
    SemiFree_Apply
}
