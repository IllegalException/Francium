package me.primooctopus33.octohack.util;

public class Timer {
    private long time = -1L;
    private long current;

    public boolean passedS(double s) {
        return this.passedMs((long)s * 1000L);
    }

    public boolean passedDms(double dms) {
        return this.passedMs((long)dms * 10L);
    }

    public boolean passedDs(double ds) {
        return this.passedMs((long)ds * 100L);
    }

    public boolean passedMs(long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public void setMs(long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }

    public boolean passedNS(long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public Timer reset() {
        this.time = System.nanoTime();
        return this;
    }

    public long getTimePassed() {
        return System.currentTimeMillis() - this.current;
    }

    public boolean hasReached(long l) {
        long var1 = l;
        return System.currentTimeMillis() - this.current >= var1;
    }

    public boolean passed(double ms) {
        return (double)(System.currentTimeMillis() - this.current) >= ms;
    }

    public long getMs(long time) {
        return time / 1000000L;
    }

    public long convertToNS(long time) {
        return time * 1000000L;
    }

    public void reset2() {
        this.current = System.currentTimeMillis();
    }

    public boolean passed(long delay) {
        return System.currentTimeMillis() - this.current >= delay;
    }

    public boolean sleep(long time) {
        if (System.nanoTime() / 1000000L - time >= time) {
            this.reset();
            return true;
        }
        return false;
    }
}
