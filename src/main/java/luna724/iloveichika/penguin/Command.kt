package luna724.iloveichika.penguin

import luna724.iloveichika.penguin.MainMod.Companion.HEADER
import luna724.iloveichika.penguin.MainMod.Companion.config
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText

class Command : CommandBase() {
    override fun getCommandName(): String {
        return "time2penguin"
    }

    override fun getCommandAliases(): List<String> {
        return listOf("t2p", "penguin", "pg")
    }

    override fun getCommandUsage(sender: ICommandSender?): String? {
        return "/pg <toggle/invispenguin/togglepenguin/lowlatency>"
    }

    override fun processCommand(sender: ICommandSender, args: Array<out String>) {
        val subcmd = args.getOrNull(0)
        if (subcmd == null) {
            sender.addChatMessage(
                ChatComponentText("/pg <toggle/invispenguin/togglepenguin/lowlatency>")
            )
            return
        }

        if (subcmd.lowercase() == "toggle") {
            config.isEnable = !config.isEnable
            config.saveConfig()
            sender.addChatMessage(
                ChatComponentText("$HEADER §8Penguin Mode§f: §r§6§l${if (config.isEnable) "ON" else "OFF"}")
            )
            return
        }
        else if (subcmd.lowercase() == "invispenguin") {
            config.hideInvisiblePlayer = !config.hideInvisiblePlayer
            config.saveConfig()
            sender.addChatMessage(
                ChatComponentText("$HEADER §8${if (config.hideInvisiblePlayer) "Hide" else "Show"} §eInvisible Player§f: §r§6§lON")
            )
            return
        }
        else if (subcmd.lowercase() == "lowlatency") {
            config.lowLatencyMode = !config.lowLatencyMode
            config.saveConfig()
            sender.addChatMessage(
                ChatComponentText("$HEADER §5Low-Latency §8Mode§f: §r§6§l${if (config.lowLatencyMode) "ON" else "OFF"}")
            )
            return
        }
        else if (subcmd.lowercase() == "togglepenguin") {
            config.modelName = if (config.modelName == "penguin") "crownpenguin" else "penguin"
            config.saveConfig()
            sender.addChatMessage(
                ChatComponentText("$HEADER §bPenguin Model§f: §r§l${if (config.modelName == "penguin") "§fNormal-Penguin" else "§6Crown-Penguin"}")
            )
            return
        }
        else {
            sender.addChatMessage(
                ChatComponentText("/pg <toggle/invispenguin/togglepenguin/lowlatency>")
            )
            return
        }
        return
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }
}