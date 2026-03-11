# meta
Skycrate System
Same Day Delivery
# intro
The Skycrate system is a different approach on delivering items. Its speed surpasses any conveyor, and the usage of rope makes it cheaper and more space-efficient. There are also drawbacks however; it requires stations on both sides and a number of posts depending on the length of the system. Skycrates are a better option than conveyors on longer distances, and is not encouraged to use them for short (0-30m) distances.
# warning
**Letter of notice from the Engineering Department:**<br>
The Engineering Department has spent countless hours and funds attempting to implement advanced techniques into the aging Skycrate system, but tragically, have not been fruitful.
<br>All Engineers wishing to develop a Skycrate system shall ensure that **all chunks between start and end stations are loaded**.
# warning2
The Engineering Department is not responsible for loss of goods along unloaded ziplines. Sign below to confirm you have [read and understood this warning.]<br>
X
------------------------------
# skycrate
|[multiblock]{mb:"II:SkycrateStation"}|
The Skycrate station is both the starting and stopping point of the delivery system. To form it, use a [hammer](introduction#introductionHammer) on the scaffolding in the middle.
To function, it requires rotary power to be inputted to its side.
# details_crate
The amount of gears increase its efficiency, similar how it works with [Gearboxes](rotary_power.md#gearboxes).
The station uses [mounts](#frames) to put crates into the delivery system. The mounts are taken from a [user-supplied crate] placed behind the inserter on the rear of the machine. Transport crates are inserted and outputted on the conveyor. A crate can be forced out of the station by applying a redstone signal.
# skycart
|[multiblock]{mb:"II:SkycartStation"}|
The Sky*cart* station is similar to the Sky*crate* station, using minecarts and rails instead of crates and conveyors. To form it, use a [hammer](introduction#introductionHammer) on the scaffolding in the middle.
# details_cart
Like its Skycrate counterpart, the station uses [mounts](#frames) to put the contents of the carts into the delivery system. Minecarts can only enter the station when the railway barrier is up. A minecart can be forced out of the station by applying a redstone signal. The station will automatically expel minecarts after a loading/unloading operation.
# skycrate_post
|[multiblock]{mb:"II:SkycratePost"}|
The Skycrate Post is the relay between two points in the skycrate network. Only two wires can be attached to the pylon at the same time. To form it, use a [hammer](introduction#introductionHammer) on the fence.
# minecarts
|[item_display]{source:"minecart_general"}|
Crate carts are a safe way to transport items via rail. They can be crafted by putting a crate into a minecart. Items inside said crates will not be lost. When disassembled, the minecart will split into the cart and the crate
# frames
|[crafting]{source:"mount"}|
The Skycrate mounts are used to transport crates via the zipline. There are two models: The basic ones, which are slow, and the fast electric ones, which use energy provided on the way via nearby [Tesla Coils](teslaCoil) to rapidly traverse the line. 