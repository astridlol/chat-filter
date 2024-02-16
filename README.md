# chat-filter

A port of the chat filter used on Airbrush ( originally created by [apple](https://www.flavored.dev/) )

## Usage

Install the plugin then head over to `/plugins/filter/config.toml`. Here you can manage your filter rules.

`blockMessage` - The message to send to the player<br />
`command` (optional) - The command to run when this rule is triggered (supports the `{player}` placeholder)<br />
`file` - A path to a text file containing a new-line separated list of regex.<br />

### Example
```toml
[[rule]]
blockMessage = "<error>Eww."
file = "plugins/filter/wordlists/test-words.txt"
```

## License
This is licensed under the MIT License. See the [LICENSE](/LICENSE) file for more details.