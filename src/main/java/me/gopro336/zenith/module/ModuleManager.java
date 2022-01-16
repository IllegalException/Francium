package me.gopro336.zenith.module;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.gui.hud.component.Watermark;
import me.gopro336.zenith.module.combat.Criticals;
import me.gopro336.zenith.module.combat.KillAura;
import me.gopro336.zenith.module.combat.PistonCrystal;
import me.gopro336.zenith.module.combat.Velocity;
import me.gopro336.zenith.module.exploit.BoatPlaceBypass;
import me.gopro336.zenith.module.exploit.CoordExploit;
import me.gopro336.zenith.module.exploit.PacketCanceller;
import me.gopro336.zenith.module.misc.FakePlayer;
import me.gopro336.zenith.module.misc.SilentXP;
import me.gopro336.zenith.module.misc.Spammer;
import me.gopro336.zenith.module.movement.*;
import me.gopro336.zenith.module.render.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ModuleManager {
    private final ArrayList<Module> modules = new ArrayList();

    public ModuleManager() {
        this.modules.add(new ClickGUI("ClickGUI", "Toggle modules by clicking on them", Category.THEME));
        this.modules.add(new CustomFont("CustomFont", "Use a custom font render instead of Minecraft's default", Category.RENDER));
        this.modules.add(new NoRender("NoRender","Doesnt render certain things", Category.RENDER));
        this.modules.add(new DeathEffects("DeathEffects", "Shows cool effect when stuff dies", Category.RENDER));
        this.modules.add(new FullBright("FullBright", "Makes things brighter", Category.RENDER));
        this.modules.add(new PenisESP("PenisESP", "Shows your hard penis", Category.RENDER));
        this.modules.add(new ViewModel("ViewModel", "Changes item looks", Category.RENDER));
        this.modules.add(new Strafe("Strafe", "speeeeeeeed", Category.MOVEMENT));
        this.modules.add(new StrafeBypass("StrafeBypass", "Bypass toggle for strafe(uses crystal damage)", Category.MOVEMENT));
        this.modules.add(new ReverseStep("ReverseStep", "Tps down blocks", Category.MOVEMENT));
        this.modules.add(new Speed("Speed", "Makes you faster", Category.MOVEMENT));
        this.modules.add(new Step("Step", "Makes you teleport up blocks", Category.MOVEMENT));
        this.modules.add(new Sprint("Sprint", "makes you auto sprint", Category.MOVEMENT));
        this.modules.add(new BoatPlaceBypass("BoatPlaceBypass", "Allows you to place boats outside of water on 2b (dont leave this on)", Category.EXPLOIT));
        this.modules.add(new PacketCanceller("PacketCanceller", "Cancels Packets", Category.EXPLOIT));
        this.modules.add(new CoordExploit("CoordExploit", ":troll:", Category.EXPLOIT));
        this.modules.add(new PistonCrystal("PistonCrystal", "SIXTIETH's pistonaura", Category.COMBAT));
        this.modules.add(new KillAura("KillAura", "Kills things automatically", Category.COMBAT));
        this.modules.add(new Criticals("Criticals", "Crits even when not jumping", Category.COMBAT));
        this.modules.add(new Velocity("Velocity", "No Knockback ez", Category.COMBAT));
        this.modules.add(new FakePlayer("FakePlayer", "A FakePlayer to test stuff on", Category.MISC));
        this.modules.add(new SilentXP("SilentXP", "Throws XP when ur not holding ez", Category.MISC));
        this.modules.add(new Spammer("Spammer", "Auto spams chat ez", Category.MISC));
        this.modules.add(new Watermark("Watermark", Category.HUD));
        this.modules.add(new HUDEditor("HUDEditor", Category.THEME));
    }

    public static void onUpdate() {
        Client.moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public Module getModule(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getModules(Category category) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.getCategory().equals((Object)category)) continue;
            mods.add(module);
        }
        return mods;
    }

    public ArrayList<Module> getEnabledModules() {
        return this.modules.stream().filter(Module::isEnabled).collect(Collectors.toCollection(ArrayList::new));
    }
}
