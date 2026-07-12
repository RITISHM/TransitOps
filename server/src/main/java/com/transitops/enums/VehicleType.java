package com.transitops.enums;

/**
 * Enumeration of allowed vehicle types in the TransitOps system.
 *
 * <p>Fixes gap #16 from the gap analysis — vehicle_type was previously unconstrained
 * free text, which would fragment dashboard filters and utilization-by-type reports
 * due to inconsistent casing/spelling. This enum enforces a closed set of values.</p>
 */
public enum VehicleType {

    /** Heavy-duty freight truck */
    TRUCK,

    /** Cargo or passenger van */
    VAN,

    /** Passenger bus */
    BUS,

    /** Light-duty pickup vehicle */
    PICKUP,

    /** Towed trailer unit */
    TRAILER,

    /** Catch-all for vehicle types not covered above */
    OTHER
}
