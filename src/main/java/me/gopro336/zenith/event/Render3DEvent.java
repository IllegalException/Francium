package me.gopro336.zenith.event;

public class Render3DEvent extends EventStage
{
    private final float partialTicks;

    public Render3DEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }
}

