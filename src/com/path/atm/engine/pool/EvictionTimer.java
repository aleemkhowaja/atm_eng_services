package com.path.atm.engine.pool;

import java.util.concurrent.ScheduledThreadPoolExecutor;

class EvictionTimer {

//    /** Executor instance */
//    private static ScheduledThreadPoolExecutor executor; //@GuardedBy("EvictionTimer.class")
//
//    /** Prevents instantiation */
//    private EvictionTimer() {
//        // Hide the default constructor
//    }
//
//
//    /**
//     * @since 2.4.3
//     */
//    @Override
//    public String toString() {
//        final StringBuilder builder = new StringBuilder();
//        builder.append("EvictionTimer []");
//        return builder.toString();
//    }
//
//
//    /**
//     * Adds the specified eviction task to the timer. Tasks that are added with a
//     * call to this method *must* call {@link #cancel()} to cancel the
//     * task to prevent memory and/or thread leaks in application server
//     * environments.
//     *
//     * @param task      Task to be scheduled.
//     * @param delay     Delay in milliseconds before task is executed.
//     * @param period    Time in milliseconds between executions.
//     */
//    static synchronized void schedule(
//            final BaseGenericObjectPool<?>.Evictor task, final long delay, final long period) {
//        if (null == executor) {
//            executor = new ScheduledThreadPoolExecutor(1, new EvictorThreadFactory());
//            executor.setRemoveOnCancelPolicy(true);
//        }
//        final ScheduledFuture<?> scheduledFuture =
//                executor.scheduleWithFixedDelay(task, delay, period, TimeUnit.MILLISECONDS);
//        task.setScheduledFuture(scheduledFuture);
//    }
//
//    /**
//     * Removes the specified eviction task from the timer.
//     *
//     * @param evictor   Task to be cancelled.
//     * @param timeout   If the associated executor is no longer required, how
//     *                  long should this thread wait for the executor to
//     *                  terminate?
//     * @param unit      The units for the specified timeout.
//     */
//    static synchronized void cancel(
//            final BaseGenericObjectPool<?>.Evictor evictor, final long timeout, final TimeUnit unit) {
//        if (evictor != null) {
//            evictor.cancel();
//        }
//        if (executor != null && executor.getQueue().isEmpty()) {
//            executor.shutdown();
//            try {
//                executor.awaitTermination(timeout, unit);
//            } catch (final InterruptedException e) {
//                // Swallow
//                // Significant API changes would be required to propagate this
//            }
//            executor.setCorePoolSize(0);
//            executor = null;
//        }
//    }
//
//    /**
//     * Thread factory that creates a daemon thread, with the context class loader from this class.
//     */
//    private static class EvictorThreadFactory implements ThreadFactory {
//
//        @Override
//        public Thread newThread(final Runnable runnable) {
//            final Thread thread = new Thread(null, runnable, "commons-pool-evictor-thread");
//            thread.setDaemon(true); // POOL-363 - Required for applications using Runtime.addShutdownHook().
//            AccessController.doPrivileged(new PrivilegedAction<Void>() {
//                @Override
//                public Void run() {
//                    thread.setContextClassLoader(EvictorThreadFactory.class.getClassLoader());
//                    return null;
//                }
//            });
//
//            return thread;
//        }
//    }
}