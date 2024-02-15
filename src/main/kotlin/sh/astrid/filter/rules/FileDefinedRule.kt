package sh.astrid.filter.rules

import me.aroze.arozeutils.minecraft.generic.sync
import org.bukkit.Bukkit
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import sh.astrid.filter.FilterPlugin
import sh.astrid.filter.data.FilterAction
import sh.astrid.filter.data.FilterConfig
import sh.astrid.filter.FilterRule
import sh.astrid.filter.mm
import java.nio.file.Path
import kotlin.io.path.notExists
import kotlin.io.path.readLines

class FileDefinedRule(private val filterConfig: FilterConfig) : FilterRule {
    private val parsedPatternLists = HashMap<FilterConfig.RuleDefinition, PatternList>()

    private var validRules: List<FilterConfig.RuleDefinition> = filterConfig.rules
        .orEmpty()
        .filter { rule ->
            if (rule.file == null && rule.pattern == null) {
                FilterPlugin.instance.logger.warning("Rule defined in config.toml does not contain 'pattern' or 'file'")
                return@filter false
            }
            if (rule.file != null && rule.pattern != null) {
                FilterPlugin.instance.logger.warning("Rule defined in config.toml has both 'pattern' and 'file'")
                return@filter false
            }
            if (rule.file != null && Path.of(rule.file).notExists()) {
                FilterPlugin.instance.logger.warning("Rule specifies 'file' that does not exist")
                return@filter false
            }
            true
        }
        .sortedBy { rule -> rule.action }

    init {
        validRules
            .filter { rule -> rule.file != null }
            .forEach { rule -> parsedPatternLists[rule] = parseFile(Path.of(rule.file!!)) }
        FilterPlugin.instance.logger.info("Loaded ${validRules.size} user-defined filter rules (out of ${filterConfig.rules.orEmpty().size})")
    }

    override fun apply(player: Player, message: String): FilterAction {
        val matchedRules = validRules.filter { rule -> hasMatch(rule, message) }

        if (matchedRules.isEmpty())
            return FilterAction.ALLOW

        val first = validRules.first()
        val blockMessage = first.blockMessage ?: filterConfig.root.blockMessage
        val command = first.command

        if(command !== null) {
            val cmd = command.replace("{player}", player.name)
            sync {
                Bukkit.dispatchCommand(Bukkit.getServer().consoleSender, cmd)
            }
        }

        player.sendMessage(blockMessage.mm())
        return first.action ?: FilterAction.BLOCK
    }

    private fun hasMatch(rule: FilterConfig.RuleDefinition, message: String): Boolean {
        return when {
            rule.pattern != null -> {
                val regex = Regex(rule.pattern)
                regex.containsMatchIn(message)
            }
            rule.file != null -> {
                val list = parsedPatternLists[rule]?.patterns
                list.orEmpty().any { pattern -> pattern.containsMatchIn(message) }
            }
            else -> false
        }
    }

    private fun parseFile(filePath: Path): PatternList {
        val list = filePath.readLines().map { line -> Regex(line) }
        return PatternList(list)
    }

    private data class PatternList(val patterns: List<Regex>)
}