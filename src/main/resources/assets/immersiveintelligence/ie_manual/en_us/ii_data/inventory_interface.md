# meta
Inventory Interface
Come, have a look around
# main
|[crafting]{source:"inventory_interface"}|
The **Inventory Interface** is a special [data connector](data_wiring.md#connector) that can read the contents of a block's inventory and send them through the [Data System](data_main.md).
Place it by the block to read from and connect a [Data Cable](data_wiring.md#data_cable).
# uses
The **Inventory Interface** can be used to monitor supplies, detect incoming items, or request more materials when stocks run low.
Only slots accessible from the attached face are read. The interface does not move items.
Like a [Duplex Connector](data_wiring.md#duplex_connector), it has separate input and output colors, set in its interface or with a hammer.
# configuration
Up to [32 rules] can be set in the interface. Each rule has a trigger and a set of output variables.
Triggers can detect inventory or slot changes, item presence, and changes in a selected item's count. They can also check whether the inventory is empty or full, or send reports at regular intervals.
Use the rule's edit button to configure it, then select [Apply] to save.
# comparisons
A rule can compare an inventory value with a number of your choice.
Available values include total items, selected item count, occupied or empty slots, total slots, items in a slot, slot capacity, comparator strength, and fill percentage.
For item conditions, use [Set Item] to select an item. Its [NBT] switch controls whether tags must also match. An empty filter matches all items.
# reporting
Condition rules can send a packet when the condition becomes true, becomes false, changes, or remains true.
Repeated and periodic reports use an interval in ticks. Periodic reports wait one interval after the first sample.
The inventory is checked every [10 ticks]. Changes between checks are not recorded.
For example, a rule can request supplies when the number of Iron Ingots falls below a set amount.
# outputs
Each output selects a variable letter and the value to send. Item counts and stack arrays have their own item filters; slot values use a selected slot.
The rule's item filter does not change its output filters. Filter stack counts are ignored.
Reports are sent to all addresses on the [output color]. Use an [ALM](arithmetic_logic_machine.md) to process the received values.
# fullness
An inventory is [full] when every accessible slot holds its current capacity, and [empty] when all accessible slots are empty.
A full inventory does not necessarily accept items: some slots may be filtered or allow extraction only.
Item filters compare item and metadata, and optionally NBT tags.
ItemStack values report at most [127 items per stack]. Use integer count outputs for larger amounts.
# replies
The interface also supports [Data Callback](data_callback.md). Send requests on its input color and receive replies on its output color.
Set [c] to ["callback"] and put a callback name in another string variable. The reply returns the value under the same letter.
Invalid requests have no result. If the inventory is missing or unloaded, only [is_connected] returns a value: false.
# data_callback
**Data Callback:**
|[data_callback]{type:"integer", name:"get_total_items", label: "Total Items", returns: "Number of items in the inventory"}|
|[data_callback]{type:"integer", name:"get_total_stacks", label: "Occupied Slots", returns: "Number of non-empty slots"}|
|[data_callback]{type:"integer", name:"get_total_slots", label: "Total Slots", returns: "Number of accessible slots"}|
|[data_callback]{type:"integer", name:"get_empty_slots", label: "Empty Slots", returns: "Number of empty slots"}|
|[data_callback]{type:"integer", name:"get_comparator", label: "Comparator Strength", returns: "Inventory comparator value (0–15)"}|
# data_callback_state
**Data Callback:**
|[data_callback]{type:"float", name:"get_fill", label: "Fill Percentage", returns: "Inventory fullness (0–100)"}|
|[data_callback]{type:"boolean", name:"is_empty", label: "Empty Inventory", returns: "True if all accessible slots are empty"}|
|[data_callback]{type:"boolean", name:"is_full", label: "Full Inventory", returns: "True if every slot holds its capacity"}|
|[data_callback]{type:"boolean", name:"is_connected", label: "Inventory Connected", returns: "True if an inventory is available"}|
|[data_callback]{type:"array", name:"get_items", label: "Inventory Contents", returns: "Up to 255 non-empty ItemStacks in slot order"}|
# slot_callbacks
**Data Callback:**
|[data_callback]{type:"integer", name:"get_slot_items", label: "Slot Item Count", returns: "Number of items in the selected slot"}|
|[data_callback]{type:"itemstack", name:"get_slot_stack", label: "Slot Contents", returns:"ItemStack in the selected slot"}|
|[data_callback]{type:"integer", name:"get_slot_limit", label: "Slot Capacity", returns: "Item limit of the selected slot"}|
Slots are numbered from [zero]. Append the slot number, e.g. [get_slot_items:4], or supply it in integer variable [s]. Without either, slot zero is used. Invalid slots have no result.
# item_callback
**Data Callback:**
|[data_callback]{type:"integer", name:"get_item_count", label: "Matching Items", returns: "Total count of the item supplied in variable i"}|
Supply an [ItemStack](data_types.md#itemstack) in variable [i] to select the item to count. Its stack count is ignored.
Optional boolean [n] controls NBT matching and defaults to [true]. Set it too [false] to count items regardless of their tags.
