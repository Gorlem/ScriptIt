ScriptIt Readme
===============

ScriptIt uses Lua as its scripting language.

It ships with the `table`, `string`, `math` and `coroutine` libraries from Lua.

Additionally a few different libraries were developed to interact with Minecraft.

Scripts which are bound to keybindings are "threaded". That means that they pause their execution after 100 "instructions". And every tick the scripts gets resumed, until it is done.

Event and hud element bindings on the other hand are executed "instantly". They run until they are done, and do not pause. Thath means that you should keep the logic in those scripts pretty short.

The default key for opening the ScriptIt GUI is `K`.

Libraries
---------

### `chat` library

 - `nil chat.send(string message)`
    - Sends a message to the server
 - `nil chat.log(string message)`
    - Adds a chat message (either a plain string or a json text string) to the history
 
### `game` library
 - `string game.fps`
    - Current frames per second
 - `string game.version`
    - Minecraft version

### `json` library
 - `string json.encode(table value)`
    - Converts a lua table to a json string
 - `table json.decode(string json)`
    - Converts a json string to a lua table

### `keyboard` library
 - `boolean keyboard.toggle(string keybinding, [boolean state])`
    - Toggles the state of any keybinding. Returns the new state.
 - `boolean keyboard.once(string keybinding)`
    - Activates any keybinding for one tick.

### `scripts` library
 - `number scripts.stopall()`
    - Stops all running scripts on the next tick (including itself). Returns the amount of scripts stopped.

### `server` library
 - `list<string> server.players()`
 - `string server.name`
 - `string server.label`
 - `string server.address`

### `player` library
 - `number player.x`
 - `number player.y`
 - `number player.z`
 - `number player.health`
 - `number player.hunger`
 - `number player.saturation`
 - `number player.armor`
 - `number player.breath`
 - `string player.gamemode`
 - `number player.yaw`
 - `number player.pitch`
 - `item player.mainhand`
 - `item player.offhand`
 - `item player.armor.helmet`
 - `item player.armor.chestplate`
 - `item player.armor.leggings`
 - `item player.armor.boots`
 - `nil player.look(number yaw, number pitch)`
 - `hit player.hit()`
 - `string player.biome`


Types
-----

#### `item` type
 - `number amount`
 - `number maxamount`
 - `number cooldown`
 - `number damage`
 - `number maxdamage`
 - `number repaircost`
 - `number maxusetime`
 - `number rarity`
 - `list<enchantment> enchantments`
 - `string name`
 - `booelean isenchantable`
 - `booelean isfood`
 - `booelean isdamageable`
 - `booelean isstackable`
 - `string id`
 
 #### `enchantment` type
 - `string name`
 - `number level`
 - `number minlevel`
 - `number maxlevel`
 - `boolean iscursed`
 - `boolea istreasure`

#### `entity` type
 - 	`string name`

#### `hit` type
 - `item item`
 - `entity entity`

Events
------

### Game Join Event
 - Gets fired once you join a server or a singleplayer world

### Chat Mesage Event
 - Fires for every recieved chat message.
 - `string event.json`
    - Contains the chat message as a json string
 - `string event.message`
    - The chat message as a plain string
 - `nil event.filter()`
    - Filters the message, so that it doesn't get displayed
 - `nil event.modify(string message)`
    - Modifies the chat message. Can be either a plain string or a json string.