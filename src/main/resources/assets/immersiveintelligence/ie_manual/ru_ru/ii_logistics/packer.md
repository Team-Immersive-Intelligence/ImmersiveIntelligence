# meta
Упаковщик
Еще больше труб и туннелей!
# intro
|[multiblock]{mb:"II:Packer"}|
Упаковщик - это машина, используемая для загрузки предметов в контейнеры, такие как ящики или магазины.
Для сборки упаковщика используйте [молоток](introduction#introductionHammer) на [вертикальном конвейере].
# details
По умолчанию в упаковщике 54 ячейки для хранения предметов. Предметы можно загрузить с помощью конвейера в задней части машины.
Для работы машины требуется электричество и наличие контейнера на конвейере длиной в 3 блока.
Машина использует [Систему задач](task_system.md) для работы. Задачи можно добавлять, удалять и настраивать через ее интерфейс или [систему данных](data_main.md).
# details_2
Задача ["Pack"] (упаковать) используется для загрузки предметов в контейнер. По умолчанию упаковщик может взять любой предмет в максимально возможном количестве.
Это можно изменить, выбрав другой режим выбора. Вы можете выбрать между определенным количеством слотов инвенторя или количеством предметов. Количество можно указать в интерфейсе, в поле ввода ниже.
# details_20
Упаковщик также может ["Unpack"] (распаковывать) предметы из заполненных контейнеров. Переключите режим с помощью кнопки в верхней части интерфейса. Предметы будут распакованы в 54 выходных слота для хранения и выведены в отмеченный слот на боковой стороне машины.
# details_3
Переключатель ["Use OreDict"] определяет, будут ли сопоставляться элементы со словарём руд. Переключатель ["NBT Sensitive"] определяет, должен ли элемент иметь тот же тег NBT, что и предоставленный. По умолчанию эти параметры отключены.
# details_4
Задачи ["Unpack"], ["Fill"], ["Unfill"], ["Charge"] и ["Discharge"] задаются аналогично ["Pack"]. Для жидкостей ["Ячейки"] - это резервуары, а объем - это количество жидкости в mb. Для энергии учитывается только количество.
# data_1
|[data_variable]{type:"string", direction:"вход", letter:"c", name:"Команду", description:"Возможные значения: add(добавить), remove(удалить), clear(очистить)."}|
|[data_variable]{type:"string", direction:"вход", letter:"a", name:"Тип", description:"Возможные значения: item(предмет), fluid(жидкость), energy(энергия)."}|
|[data_variable]{type:"itemstack", direction:"вход", letter:"s", name:"Стак", description:"Опционально: ItemStack или OreDict, которой должны соответствовать элементу."}|
# data_2
|[data_variable]{type:"string", direction:"вход", letter:"m", name:"Режим выбора", description:"Возможные значения: amount(кол-во), slot(слоты), all_possible(все возможные). Опционально. По умолчанию последние."}|
|[data_variable]{type:"integer", direction:"вход", letter:"e", name:"Expire After", description:"Необязательный. кол-во циклов для завершения задания"}|
|[data_variable]{type:"boolean", direction:"вход", letter:"r", name:"Repeat", description:"Optional. Whether the task should be repeated multiple times in one cycle."}|
# upgrades_fluid
|[upgrade_display]{upgrade:"immersiveintelligence:packer_fluid"}|
The [Fluid Loader] transforms the Packer's item storage into [96 buckets] of fluid storage and changes the loading mechanism to fill fluid containers like barrels. A souped-up [Bottling Machine](bottlingMachine)!
# upgrades_energy
|[upgrade_display]{upgrade:"immersiveintelligence:packer_energy"}|
The [Energy Loader] converts the Packer's item storage into [16 million] IF of energy storage. The loading mechanism is replaced with a set of electrical terminals to rapidly charge capacitors or equipment.
# upgrades_railway
|[upgrade_display]{upgrade:"immersiveintelligence:packer_railway"}|
The [Railway Upgrade] transforms the Packer's loading conveyor into a set of rails. This allows the Packer to fill [storage Minecarts](skycrate_system.md#minecarts). This upgrade can be combined with other upgrades.
|[wip_notice]|
# upgrades_labeler
|[upgrade_display]{upgrade:"immersiveintelligence:packer_naming"}|
The [Naming Stamp] adds a label maker to the Packer and allows it to set the name of the packed item. This upgrade can be combined with other upgrades.
# data_labeling_1
|[data_variable]{type:"itemstack", direction:"вход", letter:"f", name:"Label Filter", description:"Optional. Container filter for labeling. ItemStack or OreDict String."}|
|[data_variable]{type:"integer", direction:"вход", letter:"b", name:"Serial Batch Start", description:"Optional. Starting serial number for labeling; also sets current serial to this value."}|
|[data_variable]{type:"logistic_tag", direction:"вход", letter:"i", name:"LogiTag Input", description:"Optional. Only label containers matching this incoming tag."}|
|[data_variable]{type:"logistic_tag", direction:"вход", letter:"o", name:"LogiTag Output", description:"Tag applied to labeled containers."}|
# data_labeling_2
|[data_variable]{type:"itemstack", direction:"вход", letter:"t", name:"LogiTag In (as ItemStack)", description:"Optional alternative to 'i'. Provide an ItemStack carrying a Logistic Tag."}|
|[data_variable]{type:"itemstack", direction:"вход", letter:"T", name:"LogiTag Out (as ItemStack)", description:"Optional alternative to 'o'. Provide an ItemStack carrying a Logistic Tag."}|
|[data_variable]{type:"integer", direction:"вход", letter:"x", name:"Индекс", description:"Optional. Index for remove/remove_label."}|