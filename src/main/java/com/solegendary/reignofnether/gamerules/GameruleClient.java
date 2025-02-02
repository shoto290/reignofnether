package com.solegendary.reignofnether.gamerules;

import com.mojang.blaze3d.vertex.PoseStack;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

import static com.solegendary.reignofnether.util.MiscUtil.fcs;

public class GameruleClient {

    private static final Minecraft MC = Minecraft.getInstance();

    public static boolean doLogFalling = true; // only for GUI
    public static boolean neutralAggro = false;
    public static int maxPopulation = ResourceCosts.DEFAULT_MAX_POPULATION;
    public static boolean doUnitGriefing = false; // only for GUI
    public static boolean doPlayerGriefing = true; // only for GUI
    public static boolean improvedPathfinding = true; // only for GUI
    public static double groundYLevel = 0;
    public static double flyingMaxYLevel = 320;
    public static boolean allowBeacons = true;
    public static boolean pvpModesOnly = false;

    public static boolean gamerulesMenuOpen = false;

    public static Button getGamerulesButton() {
        return new Button(
                "Game Rules Menu",
                14,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/repeating_command_block_back.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                () -> gamerulesMenuOpen = !gamerulesMenuOpen,
                null,
                List.of(
                        fcs(I18n.get("Game Rules Menu"))
                )
        );
    }

    private static class GameruleBooleanButton extends Button {
        private final String label;
        public GameruleBooleanButton(String label, boolean enabled, Runnable onLeftClick, String tooltip) {
            super(
                    "Boolean Game Rule",
                    10,
                    enabled ? new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/tick.png") :
                            new ResourceLocation(ReignOfNether.MOD_ID, "textures/hud/cross.png"),
                    (Keybinding) null,
                    () -> false,
                    () -> false,
                    () -> true,
                    onLeftClick,
                    null,
                    List.of(fcs(tooltip))
            );
            this.label = label;
            this.frameResource = null;
        }
        @Override
        public void render(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
            super.render(poseStack, x, y, mouseX, mouseY);
            GuiComponent.drawString(poseStack, MC.font, label,x + 23, y + 7, 0xFFFFFF);
        }
    }

    private static class GameruleIntegerButton extends Button {
        private final String label;
        public GameruleIntegerButton(String label, Runnable onLeftClick, Runnable onRightClick, List<FormattedCharSequence> tooltipLines) {
            super(
                "Integer Game Rule",
                10,
                new ResourceLocation(ReignOfNether.MOD_ID, "textures/icons/blocks/command_block_back.png"),
                (Keybinding) null,
                () -> false,
                () -> false,
                () -> true,
                onLeftClick,
                onRightClick,
                tooltipLines
            );
            this.label = label;
        }
        @Override
        public void render(PoseStack poseStack, int x, int y, int mouseX, int mouseY) {
            super.render(poseStack, x, y, mouseX, mouseY);
            GuiComponent.drawString(poseStack, MC.font, label,
                    x + 23, y + 7, 0xFFFFFF);
        }
    }

    // returns list of rendered buttons
    public static List<Button> renderGamerulesGUI(PoseStack poseStack, int xTR, int yTR, int mouseX, int mouseY) {
        ArrayList<Button> buttons = new ArrayList<>();
        int width = 140;
        int x = xTR - width - 10;
        int y = yTR;

        buttons.add(new GameruleBooleanButton("doLogFalling", doLogFalling,
            () -> {
                doLogFalling = !doLogFalling;
                GameruleServerboundPacket.setLogFalling(doLogFalling);
            },
            I18n.get("commands.reignofnether.gamerule.do_log_falling")
        ));
        buttons.add(new GameruleBooleanButton("neutralAggro", neutralAggro,
            () -> {
                neutralAggro = !neutralAggro;
                GameruleServerboundPacket.setNeutralAggro(neutralAggro);
            },
            I18n.get("commands.reignofnether.gamerule.neutral_aggro")
        ));

        buttons.add(new GameruleBooleanButton("doUnitGriefing", doUnitGriefing,
            () -> {
                doUnitGriefing = !doUnitGriefing;
                GameruleServerboundPacket.setUnitGriefing(doUnitGriefing);
            },
            I18n.get("commands.reignofnether.gamerule.unit_griefing")
        ));
        buttons.add(new GameruleBooleanButton("doPlayerGriefing", doPlayerGriefing,
            () -> {
                doPlayerGriefing = !doPlayerGriefing;
                GameruleServerboundPacket.setPlayerGriefing(doPlayerGriefing);
            },
            I18n.get("commands.reignofnether.gamerule.player_griefing")
        ));
        buttons.add(new GameruleBooleanButton("improvedPathfinding", improvedPathfinding,
                () -> {
                    improvedPathfinding = !improvedPathfinding;
                    GameruleServerboundPacket.setImprovedPathfinding(improvedPathfinding);
                },
                I18n.get("commands.reignofnether.gamerule.improved_pathfinding")
        ));
        buttons.add(new GameruleBooleanButton("allowBeacons", allowBeacons,
            () -> {
                allowBeacons = !allowBeacons;
                GameruleServerboundPacket.setAllowBeacons(allowBeacons);
            },
            I18n.get("commands.reignofnether.gamerule.allow_beacons")
        ));
        buttons.add(new GameruleBooleanButton("pvpModesOnly", pvpModesOnly,
            () -> {
                pvpModesOnly = !pvpModesOnly;
                GameruleServerboundPacket.setPvpModesOnly(pvpModesOnly);
            },
            I18n.get("commands.reignofnether.gamerule.pvp_modes_only")
        ));
        buttons.add(new GameruleIntegerButton("maxPopulation: " + Math.round(maxPopulation),
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        maxPopulation += 10;
                    else
                        maxPopulation += 1;
                    maxPopulation = Math.min(10000, maxPopulation);
                    GameruleServerboundPacket.setMaxPopulation(maxPopulation);
                },
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        maxPopulation -= 10;
                    else
                        maxPopulation -= 1;
                    maxPopulation = Math.max(0, maxPopulation);
                    GameruleServerboundPacket.setMaxPopulation(maxPopulation);
                },
                List.of(
                        fcs(I18n.get("commands.reignofnether.gamerule.max_population")),
                        fcs(I18n.get("hud.gamerule.reignofnether.click")),
                        fcs(I18n.get("hud.gamerule.reignofnether.shift_click"))
                )
        ));
        buttons.add(new GameruleIntegerButton("groundYLevel: " + Math.round(groundYLevel),
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        groundYLevel += 10;
                    else
                        groundYLevel += 1;
                    groundYLevel = Math.min(320, groundYLevel);
                    GameruleServerboundPacket.setGroundYLevel((long) groundYLevel);
                },
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        groundYLevel -= 10;
                    else
                        groundYLevel -= 1;
                    groundYLevel = Math.max(-320, groundYLevel);
                    GameruleServerboundPacket.setGroundYLevel((long) groundYLevel);
                },
                List.of(
                        fcs(I18n.get("commands.reignofnether.gamerule.ground_y_level")),
                        fcs(I18n.get("hud.gamerule.reignofnether.click")),
                        fcs(I18n.get("hud.gamerule.reignofnether.shift_click"))
                )
        ));
        buttons.add(new GameruleIntegerButton("flyingMaxYLevel: " + Math.round(flyingMaxYLevel),
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        flyingMaxYLevel += 10;
                    else
                        flyingMaxYLevel += 1;
                    flyingMaxYLevel = Math.min(320, flyingMaxYLevel);
                    GameruleServerboundPacket.setFlyingMaxYLevel((long) flyingMaxYLevel);
                },
                () -> {
                    if (Keybindings.shiftMod.isDown())
                        flyingMaxYLevel -= 10;
                    else
                        flyingMaxYLevel -= 1;
                    flyingMaxYLevel = Math.max(-320, flyingMaxYLevel);
                    GameruleServerboundPacket.setFlyingMaxYLevel((long) flyingMaxYLevel);
                },
                List.of(
                        fcs(I18n.get("commands.reignofnether.gamerule.flying_max_y_level")),
                        fcs(I18n.get("hud.gamerule.reignofnether.click")),
                        fcs(I18n.get("hud.gamerule.reignofnether.shift_click"))
                )
        ));

        int height = (buttons.size() * 20) - 5;
        MyRenderer.renderFrameWithBg(poseStack, x, y, width, height, 0xA0000000);

        int i = 0;
        for (Button button : buttons) {
            button.render(poseStack, x + 5, y + 5, mouseX, mouseY);
            y += 18;
        }
        return buttons;
    }
}












