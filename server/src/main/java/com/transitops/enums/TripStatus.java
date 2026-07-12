package com.transitops.enums;

/**
 * Enumeration of possible trip statuses in the TransitOps FSM.
 *
 * <p>The Trip state machine supports the following transitions:
 * <pre>
 *   [*] ──→ DRAFT ──→ DISPATCHED ──→ COMPLETED
 *                │           │
 *                │           └──→ CANCELLED
 *                └──→ [*] (abandon/delete)
 * </pre>
 * </p>
 */
public enum TripStatus {

    /** Trip has been created but not yet dispatched — assets are NOT locked */
    DRAFT,

    /** Trip is active — vehicle and driver are locked with ON_TRIP status */
    DISPATCHED,

    /** Trip finished successfully — assets released back to AVAILABLE */
    COMPLETED,

    /** Trip was cancelled after dispatch — assets released back to AVAILABLE */
    CANCELLED
}
