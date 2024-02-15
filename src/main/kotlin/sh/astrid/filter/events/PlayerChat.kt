package sh.astrid.filter.events

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import sh.astrid.filter.FilterManager
import sh.astrid.filter.FilterPlugin

object PlayerChat : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun chatEvent(event: AsyncPlayerChatEvent) {
        val player = event.player
        val msg = event.message

        if(!FilterManager.shouldBlock(player, msg)) return

        event.isCancelled = true
        FilterPlugin.instance.logger.info("${player.name} said \"$msg\".")
    }
}