package com.transitops.enums;

/**
 * Enumeration of possible driver statuses in the TransitOps system.
 *
 * <p>Maps directly to the CHECK constraint on the drivers.status column:
 * ('AVAILABLE', 'ON_TRIP', 'OFF_DUTY', 'SUSPENDED').</p>
 */
public enum DriverStatus {

    /** Driver is available and eligible for trip assignment */
    AVAILABLE,

    /** Driver is currently assigned to an active (dispatched) trip */
    ON_TRIP,

    /** Driver is off duty and cannot be assigned to trips */
    OFF_DUTY,

    /** Driver has been suspended due to compliance or safety violations */
    SUSPENDED
}
