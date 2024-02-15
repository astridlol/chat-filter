package sh.astrid.filter

import cc.ekblad.toml.decode
import cc.ekblad.toml.tomlMapper
import org.bukkit.entity.Player
import sh.astrid.filter.data.FilterAction
import sh.astrid.filter.data.FilterConfig
import sh.astrid.filter.rules.FileDefinedRule
import java.nio.file.Path

object FilterManager {
    private lateinit var filterConfigPath: Path
    private lateinit var filterConfig: FilterConfig

    private val mapper = tomlMapper {
        mapping<FilterConfig> (
            "rule" to "rules"
        )
    }

    private val registeredRules = ArrayList<FilterRule>()

    fun initialize() {
        filterConfigPath = FilterPlugin.instance.dataFolder.resolve("config.toml").toPath()
        loadRules()
    }

    private fun loadRules() {
        filterConfig = mapper.decode(filterConfigPath)
        registerRule(FileDefinedRule(filterConfig))
        FilterPlugin.instance.logger.info("Loaded ${registeredRules.size} filter rules")
    }

    fun reloadRules() {
        registeredRules.clear()
        loadRules()
    }

    fun shouldBlock(player: Player, message: String): Boolean {
        return registeredRules.any { rule -> rule.apply(player, message) >= FilterAction.BLOCK }
    }

    private fun registerRule(rule: FilterRule) {
        if (registeredRules.contains(rule))
            return
        registeredRules += rule
    }
}