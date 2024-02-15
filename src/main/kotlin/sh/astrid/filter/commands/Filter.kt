package sh.astrid.filter.commands

import me.vaperion.blade.annotation.argument.Name
import me.vaperion.blade.annotation.argument.Sender
import me.vaperion.blade.annotation.argument.Text
import me.vaperion.blade.annotation.command.Command
import me.vaperion.blade.annotation.command.Permission
import org.bukkit.entity.Player
import sh.astrid.filter.FilterManager
import sh.astrid.filter.send

@Suppress("unused")
object Filter {
    @Command("filter reload")
    @Permission("op")
    fun test(@Sender player: Player) {
        FilterManager.reloadRules()
        player.send("<success> Successfully reloaded filter rules.")
    }
}