package com.relatablecode.lifecycleobservables

/**
 * Enumerates the conditions under which [LifecycleAwareSubject] should be updated.
 *
 * @property UNIQUE Only updates when the new value is different from the current value.
 * @property FIRST_ONLY Only allows the first update to take effect and ignores subsequent updates.
 * @property FIRST_ONLY_AND_NOT_NULL Only allows the first non-null update to take effect and ignores subsequent updates.
 * @property NONE Always updates regardless of the new value's relation to the current value.
 */
enum class UpdateCondition {
    UNIQUE, FIRST_ONLY, FIRST_ONLY_AND_NOT_NULL, NONE
}
