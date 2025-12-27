# United Pixel Dungeon

The following repository is a set of modifications to the Shattered pixel dungeon game. 

For the moment, this is quite empty.
One issue which I encountered is in the lwjgl-3.3.3, which doesn't check for the
noexec flag in /tmp, which can be an issue on some devices, leading to an error.
Updating to lwjgl-3.3.4 is an option, but libgdx still usees wljgl-3.3.3, as per
[00-Evan](https://github.com/00-Evan/shattered-pixel-dungeon/issues/2254)
According to the [original thread](https://github.com/LWJGL/lwjgl3/issues/987) which diagnosed it, one solution is to let
the lwjgl folder get loaded in /tmp, delete the contents, and set the folder to read-only.
lwjgl will see that it doesn't have write-permissions, and will create a new temporary folder.