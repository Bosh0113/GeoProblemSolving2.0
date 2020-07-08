package cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums;

import lombok.Getter;

@Getter

/**
 * Public: free to join and quit
 * Discoverable: apply to join and quit
 * Private: only for Project Level
 */
public enum ActivityPrivacy {
    Public,
    Discoverable,
    Private
}
