package me.gopro336.zenith.module.combat;

import me.gopro336.zenith.module.Category;
import me.gopro336.zenith.module.Module;
import me.gopro336.zenith.setting.Setting;

public class KillAura
extends Module {
    private final Setting range = new Setting("Range", this, 5, 0, 6);
    private final Setting silent = new Setting("SilentMode", this, true);

    public KillAura(String name, String description, Category category) {
        super(name, description, category);
        this.addSetting(this.range);
        this.addSetting(this.silent);
    }
}

