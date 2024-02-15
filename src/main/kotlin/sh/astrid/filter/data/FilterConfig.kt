package sh.astrid.filter.data

data class FilterConfig(
    val root: Root,
    val rules: List<RuleDefinition>?
) {
    data class Root(val blockMessage: String)
    data class RuleDefinition(
        val blockMessage: String?,
        val pattern: String?,
        val file: String?,
        val command: String?,
        val action: FilterAction?)
}
