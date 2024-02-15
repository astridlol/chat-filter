package sh.astrid.filter

import org.bukkit.entity.Player
import sh.astrid.filter.data.FilterAction

interface FilterRule {
    /**
     * Apply a filter rule.
     * @return Return true if the message should be blocked. Otherwise, return false.
     */
    fun apply(player: Player, message: String): FilterAction
}