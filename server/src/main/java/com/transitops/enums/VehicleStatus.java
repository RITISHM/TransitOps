package com.transitops.enums;

/**
 * Enumeration of possible vehicle statuses in the TransitOps system.
 *
 * <p>Maps directly to the CHECK constraint on the vehicles.status column:
 * ('AVAILABLE', 'ON_TRIP', 'IN_SHOP', 'RETIRED').</p>
 */
public enum VehicleStatus {

    /** Vehicle is idle and eligible for trip assignment */
    AVAILABLE,

    /** Vehicle is currently assigned to an active (dispatched) trip */
    ON_TRIP,

    /** Vehicle is undergoing maintenance and cannot be dispatched */
    IN_SHOP,

    /** Vehicle has been permanently decommissioned from the fleet */
    RETIRED
}
