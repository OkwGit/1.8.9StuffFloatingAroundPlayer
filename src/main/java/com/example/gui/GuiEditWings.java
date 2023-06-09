package com.example.gui;

import com.example.WingSettings;
import com.example.WingsMod;
import com.example.gui.GuiEditWings;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;

public class GuiEditWings extends GuiScreen
{
    private WingSettings settings;
    private GuiSlider sliderScale;
    private GuiSlider sliderHeight;
    private GuiSlider sliderXPosition;
    private GuiSlider sliderYPosition;

    private GuiButton buttonChroma;
    private GuiSlider sliderHue;

    public GuiEditWings(WingsMod mod)
    {
        this.settings = mod.getSettings();
    }

    @Override
    public void initGui()
    {
        // Add a button to the button list
        buttonList.add(new GuiButton(0, width / 2 - 50, height / 2 - 50, 100, 20, "Wings: " + getColoredBool(settings.enabled)));

        // Add a slider to the button list
        buttonList.add(sliderScale = new GuiSlider(1, width / 2 - 50, height / 2 - 25, 100, 20, "Scale: ", "%", 60, 140, settings.scale, false, true));
        buttonList.add(sliderHeight = new GuiSlider(5, width / 2 - 50, height / 2 + 25, 100, 20, "SliderHeight: ", "%", 60, 140, settings.modelHeight, false, true));
        buttonList.add(sliderXPosition = new GuiSlider(6, width / 2 - 50, height / 2 + 25 +25, 100, 20, "XPos: ", "%", -120, 100, settings.xPosition, false, true));
        buttonList.add(sliderYPosition = new GuiSlider(7, width / 2 - 50, height / 2 + 25 +25 +25, 100, 20, "YPos: ", "%", -120, 100, settings.yPosition, false, true));

        // Add a button to the button list
        buttonList.add(new GuiButton(2, width / 2 - 50, height / 2, 100, 20, "Color: " + getColoredBool(settings.colored)));

        // Add a slider to the button list
        buttonList.add(sliderHue = new GuiSlider(3, width / 2 - 50, height / 2 + 25, 100, 20, "Hue: ", "%", 0, 100, settings.hue, false, true));

        // Add a button to the button list
        buttonList.add(buttonChroma = new GuiButton(4, width / 2 - 50, height / 2 + 50, 100, 20, "Chroma: " + getColoredBool(settings.chroma)));

        // Set visibility based on a condition
        sliderHue.visible = settings.colored;
        buttonChroma.visible = settings.colored;

        // Add a slider to the button list
        // (This line is commented out in the provided code)
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        // Draw the default screen background
        drawDefaultBackground();

        // Call the superclass's drawScreen method
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Update the slider value if chroma is enabled, otherwise update the settings value
        if (settings.chroma)
        {
            sliderHue.setValue((System.currentTimeMillis() % 1000) / 1000F * 100);
            sliderHue.updateSlider();
        }
        else
        {
            settings.hue = sliderHue.getValueInt();
        }

        // Update the settings value from the slider
        settings.scale = sliderScale.getValueInt();
    }

    @Override
    public void onGuiClosed()
    {
        // Save the configuration settings
        settings.saveConfig();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        // Handle button clicks
        switch (button.id)
        {
            case 0:
                // Toggle the enabled state and update the button text
                settings.enabled = !settings.enabled;
                button.displayString = "Wings: " + getColoredBool(settings.enabled);
                break;
            case 2:
                // Toggle the colored state and update the button text
                settings.colored = !settings.colored;
                button.displayString = "Color: " + getColoredBool(settings.colored);

                // Set visibility based on the colored state
                sliderHue.visible = settings.colored;
                buttonChroma.visible = settings.colored;
                break;
            case 4:
                // Toggle the chroma state and update the button text
                settings.chroma = !settings.chroma;
                button.displayString = "Chroma: " + getColoredBool(settings.chroma);

                // Update the slider value and appearance based on the chroma state
                sliderHue.setValue(settings.hue);
                sliderHue.updateSlider();
                break;
                //Newly added
            case 5:
                settings.modelHeight = sliderHeight.getValueInt();
                break;

                case 6:
                settings.xPosition = sliderXPosition.getValueInt();
                break;

                case 7:
                settings.yPosition = sliderYPosition.getValueInt();
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        // Specify whether the game should be paused when the GUI is displayed
        return false;
    }

    private String getColoredBool(boolean bool)
    {
        // Get a colored representation of a boolean value
        if (bool)
        {
            return EnumChatFormatting.GREEN + "Enabled";
        }

        return EnumChatFormatting.RED + "Disabled";
    }
}
