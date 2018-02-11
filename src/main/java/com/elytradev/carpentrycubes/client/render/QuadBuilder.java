package com.elytradev.carpentrycubes.client.render;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import javax.vecmath.Vector4f;
import java.util.Optional;

/**
 * Small utility class for building quads, makes things less painful.
 */
public class QuadBuilder {

    private UnpackedBakedQuad.Builder builder;
    private VertexFormat format;
    private Optional<TRSRTransformation> transform;
    private EnumFacing side;

    public QuadBuilder(VertexFormat format, @Nullable TRSRTransformation transform, TextureAtlasSprite sprite, EnumFacing side, int tintIndex) {
        this.builder = new UnpackedBakedQuad.Builder(format);
        this.format = format;
        this.transform = Optional.of(transform);
        this.side = side;

        this.builder.setTexture(sprite);
        this.builder.setQuadTint(tintIndex);
        this.builder.setQuadOrientation(side);
    }

    public void putVertex(float x, float y, float z, float u, float v) {
        Vector4f vec = new Vector4f();
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    if (transform.isPresent()) {
                        vec.x = x;
                        vec.y = y;
                        vec.z = z;
                        vec.w = 1;
                        transform.get().getMatrix().transform(vec);
                        builder.put(e, vec.x, vec.y, vec.z, vec.w);
                    } else {
                        builder.put(e, x, y, z, 1);
                    }
                    break;
                case COLOR:
                    builder.put(e, 1f, 1f, 1f, 1f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) side.getFrontOffsetX(), (float) side.getFrontOffsetY(), (float) side.getFrontOffsetZ(), 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    public BakedQuad build() {
        return builder.build();
    }

}