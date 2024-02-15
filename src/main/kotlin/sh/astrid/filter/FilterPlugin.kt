package sh.astrid.filter

import me.aroze.arozeutils.kotlin.reflection.getClassesInPackage
import me.aroze.arozeutils.kotlin.reflection.registerListenersPackage
import me.aroze.arozeutils.minecraft.ArozeUtils
import me.vaperion.blade.Blade
import me.vaperion.blade.bukkit.BladeBukkitPlatform
import org.bukkit.plugin.java.JavaPlugin

class FilterPlugin: JavaPlugin() {
    companion object {
        lateinit var instance: FilterPlugin
    }

    override fun onEnable() {
        instance = this
        ArozeUtils.setPlugin(this)

        registerCommands()
        generateConfig()
        registerListenersPackage("sh.astrid.filter.events")

        FilterManager.initialize()
    }

    override fun onDisable() {}

    private fun registerCommands() {
        val commandHandler = Blade.forPlatform(BladeBukkitPlatform(this)).build()
        for (command in getClassesInPackage("sh.astrid.filter.commands")) {
            if(command.name.toString().contains("$")) continue
            commandHandler.register(command.getField("INSTANCE")[null])
        }
    }

    private fun generateConfig() {
        dataFolder.mkdir()
        saveResource("config.toml", false)
    }
}