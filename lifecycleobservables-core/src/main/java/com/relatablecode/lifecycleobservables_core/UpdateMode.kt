package com.relatablecode.lifecycleobservables_core

/**
 * Enumerates the modes in which the [LifecycleAwareSubject] can be updated.
 *
 * @property SYNC Performs updates synchronously, ensuring atomic operations but might block the main thread.
 * @property ASYNC Performs updates asynchronously, ensuring atomic operations without blocking the main thread.
 * @property NON_LOCKING Performs updates without any locking mechanism, which might lead to data inconsistencies in concurrent environments.
 */
enum class UpdateMode {
    SYNC, ASYNC, NON_LOCKING
}
