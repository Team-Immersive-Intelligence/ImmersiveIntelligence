# meta
Система задач
Будь проще, глупец

# intro
Система задач - это возможность организации работы по расписанию с помощью одной повторяющейся задачи. Задачи используются многими устройствами передачи данных, такими как [манипуляторы](inserters.md), [упаковщики](packer.md) и [маршрутизаторы данных](small_data_devices.md#router). Задачи делятся на две группы:
**Задачи** - задания, которые будут выполняться бесконечно, если их не удалить вручную.
**Задания** - задания, которые являются временными и удаляются сами по себе после завершения (т.е. после того, как пользователь получит определенное количество элементов).
# details
По умолчанию задания выполняются от самого старого к самому новому. Если задание не может быть выполнено, оно будет пропущено. После завершения [задания] (успешно или нет) проверяется, следует ли ее удалять.
# logitags
|[item_display]{source:"logitag_item"}|
**Логистический манифест** или **LogiTags** для краткости, это информационные метки, используемые логистическими машинами II, как [упаковщики](packer.md) и [манипуляторы](inserters.md).
They contain information about name of the cargo, its name, text description, [owner identity], origin, destination, color marker, and batch number and can be applied to containers, such as [Crates](engineers_crates.md) through crafting.
# logitag_printing
To create a **Logistic Manifest**, a [Printing Press](../ii_data/printing_press.md) loaded with [Blank Pages] is required.
The easiest way of passing information about the Manifest to a machine is to use a single [LogiTag](../ii_data/data_types.md#logitag) type variable.<br>
|[data_variable]{type:"logitag", direction:"in", letter:"l", name:"Logistic Tag", description:"Logistic tag that will be printed", requirements:{m:"logi_tag"}}|
Because this type is considered an [advanced one], it requires an upgraded [Data Input Machine](../ii_data/data_input_machine.md).
# logitag_printing2
An alternative way to pass information about a **LogiTag** is to use an [ItemStack](../ii_data/data_types.md#itemstack) of an existing Logistic Manifest item.<br>
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"Logistic Tag", description:"Logistic tag that will be printed", requirements:{m:"logi_tag"}}|
It is also possible to do it using a set of packet variables:<br><br>
|[data_variable]{type:"string", direction:"in", letter:"n", name:"Name", description:"Name of the cargo"}|
|[data_variable]{type:"string", direction:"in", letter:"d", name:"Description", description:"Optional longer text description"}|
# logitag_printing3
|[data_variable]{type:"integer", direction:"in", letter:"b", name:"Batch Number", description:"Number for cargo in series with the same name"}|
|[data_variable]{type:"string", direction:"in", letter:"f", name:"From", description:"Sender's name"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"To", description:"Recipient's name"}|
|[data_variable]{type:"string", direction:"in", letter:"o", name:"Owner", description:"Identity of this cargo's owner."}|
|[data_variable]{type:"string", direction:"in", letter:"p", name:"Color", description:"Color assigned to this cargo in hex format."}|
# logitag_printing4
This last way is **not recommended**, but it remains a viable option, when you're not concerned about taking space in the [Packet].
The [Owner Identity](../ii_warfare/terrain_control/owner_identity.md) in a [LogiTag] refers to what's commonly called *the faction system*.