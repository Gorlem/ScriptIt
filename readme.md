ScriptIt Readme
===============

ScriptIt uses Lua as its scripting language.

It ships with the `table`, `string`, `math` and `coroutine` libraries from Lua.

Additionally a few different libraries were developed to interact with Minecraft.

Scripts which are bound to keybindings are "threaded". That means that they pause their execution after 100 "instructions". And every tick the scripts gets resumed, until it is done.

Event and hud element bindings on the other hand are executed "instantly". They run until they are done, and do not pause. Thath means that you should keep the logic in those scripts pretty short.

The default key for opening the ScriptIt GUI is `K`.

Documentation is available on the [github wiki](https://github.com/Gorlem/ScriptIt/wiki).