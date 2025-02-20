package luna724.iloveichika.penguin

import net.minecraft.client.Minecraft
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

@Mod(modid = "time2penguin", name = "LC-AutoMove", version = "2.6")
class MainMod {
    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        val configFile = File(event.modConfigurationDirectory, "time2penguin.cfg")
        config = Config(configFile)

        if (!configFile.exists()) {
            configFile.writeText("{}")
        }
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(Command())
        MinecraftForge.EVENT_BUS.register(Penguin())
    }

    companion object {
        lateinit var config: Config
        const val HEADER: String = "§7[§5P§de§bn§9g§au§ei§cn§7]§f:§r "
    }
}