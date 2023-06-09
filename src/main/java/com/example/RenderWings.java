package com.example;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

// Here we are defining a public class called "RenderWings",
// which extends ModelBase, meaning it inherits all the methods and variables of ModelBase class.
public class RenderWings extends ModelBase
{
    // These are instance variables that will be used throughout the class.
    private Minecraft mc; // An instance of the Minecraft class, used to get various game data.
    private WingSettings settings; // An instance of the WingSettings class, used to handle the settings for the wings.
    private ResourceLocation location; // The location of the texture for the wings.
    private ModelRenderer wing; // The model for the wing.
//    private ModelRenderer wingTip; // The model for the tip of the wing.
    private boolean playerUsesFullHeight; // Flag to indicate if the player model uses its full height.

    // The constructor of the class, which is called when an instance of the class is created.
    public RenderWings(WingSettings settings)
    {
        this.mc = Minecraft.getMinecraft(); // Get the current Minecraft instance.
        this.settings = settings; // Set the WingSettings instance.
        this.location = new ResourceLocation("wingsmod", "glass.png"); // Set the ResourceLocation instance with the path to the wings texture.
        this.playerUsesFullHeight = Loader.isModLoaded("animations"); // Check if the "animations" mod is loaded to set playerUsesFullHeight.

        // Setting texture offsets.
        setTextureOffset("wing.bone", 0, 0); // Set texture offset for wing bone.
//        setTextureOffset("wing.skin", -10, 8); // Set texture offset for wing skin.
//        setTextureOffset("wingtip.bone", 0, 5); // Set texture offset for wingtip bone.
//        setTextureOffset("wingtip.skin", -10, 18); // Set texture offset for wingtip skin.

        // Creating wing model renderer.
        wing = new ModelRenderer(this, "wing"); // Create a new ModelRenderer instance for the wing.
        wing.setTextureSize(16, 16); // Set the size of the texture for the wing.
        wing.setRotationPoint(0F, 0, 0); // Set the rotation point for the wing.
        wing.addBox("bone", 0F, 0F, 0F, 10, 10, 10); // Add a box to the wing model to represent the bone.
//        wing.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10); // Add a box to the wing model to represent the skin.

        // Creating wing tip model renderer.
//        wingTip = new ModelRenderer(this, "wingtip"); // Create a new ModelRenderer instance for the wing tip.
//        wingTip.setTextureSize(30, 30); // Set the size of the texture for the wing tip.
//        wingTip.setRotationPoint(-10.0F, 0.0F, 0.0F); // Set the rotation point for the wing tip.
//        wingTip.addBox("bone", -10.0F, -0.5F, -0.5F, 10, 1, 1); // Add a box to the wing tip model to represent the bone.
//        wingTip.addBox("skin", -10.0F, 0.0F, 0.5F, 10, 0, 10); // Add a box to the wing tip model to represent the skin.
//        wing.addChild(wingTip); // Make the wingtip a child of the wing so that it rotates around the wing.
    }

    // The onRenderPlayer method, which is triggered after the player has been rendered.
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Post event) //This line declares a method named onRenderPlayer with a parameter of type RenderPlayerEvent.Post named event
    {
        EntityPlayer player = event.entityPlayer; // Get the player entity from the event.

        // Should render wings onto this player?
        if (settings.enabled && player.equals(mc.thePlayer) && !player.isInvisible())
        {
            renderWings(player, event.partialRenderTick); // If the conditions are met, render the wings.
        }
    }

    // The renderWings method, which is responsible for rendering the wings on the player.
    private void renderWings(EntityPlayer player, float partialTicks)
    {
        double scale = settings.scale / 100D; // Get the scale for the wings from the settings.
        double rotate = interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks); // Calculate the rotation for the wings.

        // OpenGL calls to transform the model based on player's position and orientation.
        GL11.glPushMatrix(); // Save the current transformation matrix.
        GL11.glScaled(-scale, -scale, scale); // Scale the model by the calculated scale.
        GL11.glRotated(180 + rotate, 0, 1, 0); // Rotate the model by the calculated rotation.
        GL11.glTranslated(0, -(playerUsesFullHeight ? 1.45 : 1.25) / scale, 0); // Move the wings the correct amount up.
        GL11.glTranslated(0, 0, 0.2 / scale); // Further translation.

        // If the player is sneaking, adjust the position of the wings.
        if (player.isSneaking())
        {
            GL11.glTranslated(0D, 0.125D / scale, 0D); // Translate the model downwards.
        }

        // Get the color settings for the wings.
        float[] colors = settings.getColors();
        GL11.glColor3f(colors[0], colors[1], colors[2]); // Set the color for the wings.
        mc.getTextureManager().bindTexture(location); // Bind the texture for the wings.

        // Loop for rendering both wings.
        for (int j = 0; j < 1; ++j)
        {
            // Enable culling, which decides which faces of the model are visible based on their orientation.
            GL11.glEnable(GL11.GL_CULL_FACE);
//            float f11 = (System.currentTimeMillis() % 1000) / 1000F * (float) Math.PI * 2.0F; // Calculate a repeating time-based value.
            // Calculate the rotation angles for the wing and the wing tip based on the time-based value.
//            this.wing.rotateAngleX = (float) Math.toRadians(-80F) - (float) Math.cos((double)f11) * 0.2F;
//            this.wing.rotateAngleY = (float) Math.toRadians(20F) + (float) Math.sin(f11) * 0.4F;
            this.wing.rotateAngleZ = (float) Math.toRadians(0F);
//            this.wingTip.rotateAngleZ = -((float)(Math.sin((double)(f11 + 2.0F)) + 0.5D)) * 0.75F;
            // Render the wing.
            this.wing.render(0.0625F);
            // Scale the model for the next wing.
            GL11.glScalef(-1.0F, 1.0F, 1.0F);

            // Change the culling face for the second pass.
            if (j == 0)
            {
                GL11.glCullFace(1028);
            }
        }

        // Reset the culling face.
        GL11.glCullFace(1029);
        // Disable culling.
        GL11.glDisable(GL11.GL_CULL_FACE);
        // Reset the color.
        GL11.glColor3f(255F, 255F, 255F);
        // Restore the saved transformation matrix.
        GL11.glPopMatrix();
    }

    // Interpolate function to smoothly transition between the player's yaw offsets for animation purposes.
    private float interpolate(float yaw1, float yaw2, float percent)
    {
        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360; // Linear interpolation.

        // Ensure the interpolated value is between 0 and 360.
        if (f < 0)
        {
            f += 360;
        }

        return f;
    }
}
