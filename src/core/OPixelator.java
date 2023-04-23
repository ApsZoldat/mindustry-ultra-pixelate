package core;

import arc.Core;
import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.Texture.TextureFilter;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.FrameBuffer;
import arc.math.Mathf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pixelator;
import mindustry.graphics.Shaders;

import static arc.Core.*;
import static mindustry.Vars.*;

// AND AGAIN ME DOING THIS CLASS REPLACING SHIT :>
public class OPixelator extends Pixelator{
    private FrameBuffer buffer = new FrameBuffer();
    private float px, py, pre;

    {
        buffer.getTexture().setFilter(TextureFilter.nearest, TextureFilter.nearest);
    }

    public void drawPixelate(){
        pre = renderer.getScale();
        float scale = renderer.getScale();
        scale = (int)scale;
        renderer.setScale(scale);
        camera.width = (int)camera.width;
        camera.height = (int)camera.height;

        px = Core.camera.position.x;
        py = Core.camera.position.y;
        Core.camera.position.set((int)px + ((int)(camera.width) % 2 == 0 ? 0 : 0.5f), (int)py + ((int)(camera.height) % 2 == 0 ? 0 : 0.5f));

        int w = (int)Core.camera.width, h = (int)Core.camera.height;
        if(renderer.isCutscene()){
            w = (int)(Core.camera.width * renderer.landScale() / renderer.getScale());
            h = (int)(Core.camera.height * renderer.landScale() / renderer.getScale());
        }
        w = Mathf.clamp(w, 2, graphics.getWidth());
        h = Mathf.clamp(h, 2, graphics.getHeight());

        int size = Core.settings.getInt("pixelatemultiper");

        w = (int)(w / (size / 50f));
        h = (int)(h / (size / 50f));

        buffer.resize(w, h);

        buffer.begin(Color.clear);
        renderer.draw();
    }

    public void register(){
        Draw.draw(Layer.end, () -> {
            buffer.end();

            Blending.disabled.apply();
            buffer.blit(Shaders.screenspace);

            Core.camera.position.set(px, py);
            renderer.setScale(pre);
        });
    }

    public boolean enabled(){
        return Core.settings.getBool("pixelate");
    }

    @Override
    public void dispose(){
        buffer.dispose();
    }
}
