/*
 * Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 * Copyright (C) 2021  cyoung06
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

// This files edited by luna724.

package luna724.iloveichika.penguin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.IOException;
import java.util.*;

import static luna724.iloveichika.penguin.MainMod.HEADER;
import static luna724.iloveichika.penguin.MainMod.config;


public class Penguin {
    public Penguin() {
        OBJLoader.instance.addDomain("time2penguin");
    }

    /**
     * 特定リソースパスに存在する 3D obj ファイルを読み込む
     */
    private void tryLoading(String modelName, String location, TextureStitchEvent.Pre event) {
        ResourceLocation modelResourceLocation = new ResourceLocation(location);
        try {
            OBJModel objModel = (OBJModel) OBJLoader.instance.loadModel(modelResourceLocation);
            objModel = (OBJModel) objModel.process(new ImmutableMap.Builder<String, String>().put("flip-v", "true").build());
            for (String obj : objModel.getMatLib().getMaterialNames()) {
                ResourceLocation resourceLocation = objModel.getMatLib().getMaterial(obj).getTexture().getTextureLocation();
                event.map.registerSprite(resourceLocation);
            }
            objModels.put(modelName, objModel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException npe) {
            System.out.println("NullPointerException occurred in try to load Penguin texture!");
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(
                    new ChatComponentText(HEADER + "NPE occurred while loading Penguin!")
            );
            npe.printStackTrace();
        }
    }

    /**
     * 知らん なんこれ
     */
    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event != null) {
            objModels.clear();
            tryLoading("crownpenguin", "time2penguin:penguin/crownpenguin.obj", event);
            tryLoading("penguin", "time2penguin:penguin/penguin.obj", event);
        }
    }

    /**
     * 知らん なんこれ
     */
    @SubscribeEvent
    public void onTextureStitchPost(TextureStitchEvent.Post event) {
        for (Map.Entry<String, OBJModel> value : objModels.entrySet()) {
            models.put(value.getKey(), value.getValue().bake(value.getValue().getDefaultState(),  DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
        }
    }


    private Map<String, OBJModel> objModels = new HashMap<>();
    private Map<String, IBakedModel> models = new HashMap<>();

    /**
     * 知らん 多分プレイヤーの描画いじる部分
     */
    @SubscribeEvent
    public void onEntityRenderPre(RenderPlayerEvent.Pre renderPlayerEvent) {
        if (renderPlayerEvent.entityPlayer.isInvisible() && config.getHideInvisiblePlayer()) return;
        if (!config.isEnable()) return;

        String modelName = config.getModelName();
        if (!models.containsKey(modelName)) {
            modelName = "penguin"; // 非対応モデルなら penguin にリセット
        }

        // 特に問題なければ変換イベント開始
        if (!config.getLowLatencyMode()) {
            renderPlayerEvent.setCanceled(true);
        }

        GlStateManager.pushMatrix();
        GlStateManager.color(1,1,1,1);
        GlStateManager.translate(renderPlayerEvent.x, renderPlayerEvent.y, renderPlayerEvent.z);
        if (renderPlayerEvent.entityPlayer.isSneaking())
        {
            GlStateManager.translate(0.0F, -0.203125F, 0.0F);
        }
        float f1 = renderPlayerEvent.entityPlayer.prevRotationYawHead + (renderPlayerEvent.entityPlayer.rotationYawHead - renderPlayerEvent.entityPlayer.prevRotationYawHead) * renderPlayerEvent.partialRenderTick;
        GlStateManager.rotate(f1+180,0,-1,0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        GlStateManager.scale(1.5,1.5,1.5);
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(
                models.get(modelName), 1,1,1,1
        );
        GlStateManager.popMatrix();


        EntityPlayer entityPlayer = renderPlayerEvent.entityPlayer;
        {
            ItemStack itemstack = entityPlayer.getHeldItem();

            if (itemstack != null)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(renderPlayerEvent.x, renderPlayerEvent.y, renderPlayerEvent.z);
                if (renderPlayerEvent.entityPlayer.isSneaking())
                {
                    GlStateManager.translate(0.0F, -0.203125F, 0.0F);
                }
                GlStateManager.rotate(f1+180, 0.0f, -1.0f, 0.0f);
                GlStateManager.translate(0,1.30 ,-0.5);



                if (entityPlayer.fishEntity != null)
                {
                    itemstack = new ItemStack(Items.fishing_rod, 0);
                }

                Item item = itemstack.getItem();
                Minecraft minecraft = Minecraft.getMinecraft();

                GlStateManager.rotate(180, 0.0f, 0.0f, 1.0f);
                if (item.isFull3D()) {
                    GlStateManager.translate(0.05,0,0);
                    GlStateManager.rotate(90, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(-45, 1.0f, 0.0f, 0.0f);
                } else if (item instanceof ItemBow) {
                    GlStateManager.translate(0,0.1, -0);
                    GlStateManager.rotate(90, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-90, 0.0f, 0.0f, 1.0f);
                } else if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                    GlStateManager.translate(0,-0.20,0.1);
                    GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                    GlStateManager.rotate(-25.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                    f1 = 0.375F;
                    GlStateManager.scale(-f1, -f1, f1);
                } else if (item instanceof ItemBlock) {
                    GlStateManager.translate(0.0F, 0.05, 0.1);
                    GlStateManager.rotate(-25.0F, 1.0F, 0.0F, 0.0F);
                } else {
                    GlStateManager.translate(0,-0.1, 0.1);
                }

                GlStateManager.scale(0.8,0.8,0.8);

                minecraft.getItemRenderer().renderItem(entityPlayer, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
                GlStateManager.popMatrix();
            }
        }

        if (config.getLowLatencyMode()) {
            renderPlayerEvent.setCanceled(true);
        }

        renderPlayerEvent.renderer.renderName((AbstractClientPlayer) renderPlayerEvent.entityPlayer, renderPlayerEvent.x, renderPlayerEvent.y, renderPlayerEvent.z);
    }
}