package core;

import mindustry.mod.Mod;
import arc.Events;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.core.Renderer;
import mindustry.game.EventType.ClientLoadEvent;

import static mindustry.Vars.*;

public class ModMain extends Mod {
    public ModMain() {
        Events.on(ClientLoadEvent.class, e -> {
            try {
                ui.settings.graphics.sliderPref("pixelatemultiper", 50, 1, 200, 1,
                i -> Float.toString(Math.round((i / 50f) * 100f) / 100f) + "x");

                Reflect.set(Renderer.class, renderer, "pixelator", new OPixelator());
            } catch (Exception ex) {
                Log.err(ex.toString());
                ui.showException("ApsZoldat is dumb and the UltraPixelate mod doesn't even work", ex);
            }
        });
    }
}
