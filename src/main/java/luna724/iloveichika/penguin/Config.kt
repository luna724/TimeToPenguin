package luna724.iloveichika.penguin

import net.minecraftforge.common.config.Configuration
import java.io.File

class Config(configFile: File?) {
    var isEnable: Boolean = false
    var hideInvisiblePlayer: Boolean = true
    var modelName: String = "penguin" // or crownpenguin
    var lowLatencyMode: Boolean = false

    private val config = Configuration(configFile)

    init {
        loadConfig()
    }

    fun loadConfig() {
        config.load()

        isEnable =
            config.getBoolean("isEnable", CATEGORY_GENERAL, false, "🐧")
        hideInvisiblePlayer =
            config.getBoolean("hideInvisiblePlayer", CATEGORY_GENERAL, true, "透明化状態のプレイヤーを🐧にしない")
        modelName =
            config.getString("modelName", CATEGORY_GENERAL, "penguin", "🐧名", arrayOf("penguin", "crownpenguin"))
        lowLatencyMode =
            config.getBoolean("lowLatencyMode", CATEGORY_GENERAL, false, "最初にプレイヤーを消すか、変換後に消すか")
    }

    fun saveConfig() {
        config[CATEGORY_GENERAL, "isEnable", isEnable].set(isEnable)
        config[CATEGORY_GENERAL, "hideInvisiblePlayer", hideInvisiblePlayer].set(hideInvisiblePlayer)
        config[CATEGORY_GENERAL, "modelName", modelName].set(modelName)
        config[CATEGORY_GENERAL, "lowLatencyMode", lowLatencyMode].set(lowLatencyMode)

        println("[Penguin]: Saving config..")
        config.save()
    }

    companion object {
        private const val CATEGORY_GENERAL = "general"
    }
}