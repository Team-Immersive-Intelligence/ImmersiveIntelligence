# meta
Манипуляторы
Никакого желтого цвета!
# intro
|[item_display]{source:"inserters_full"}|
"Манипулятор,"  это устройство, используемое для перемещения предметов из одного инвентаря в другой. Он состоят из рычага и двигателей для вращения и перемещения устройства для сбора предметов. Для ввода жидкостей используются трубы и клапаны. Манипуляторы могут брать и ложить в любое заданное горизонтальное направление.
# intro_2
Манипулятор настраевается [молотком](introduction#introductionHammer). ПКМ для входа и  Shiht+ПКМ для выхода.<br>
Для работы манипуляторы требуется электричество и [данные](data_main.md), указывающие им, что делать.
# basic_inserter
|[crafting]{source:"inserter_basic"}|
Манипулятор используется для простого, но контролируемого перемещения предметов. Он состоит из базовых схем и механических компонентов и представляет собой дешевый и простой способ установить, количество предметов которое нужно перенести с одной стороны на другую .
# basic_commands_1
|[data_variable]{type:"integer", direction:"вход", letter:"e", name:"Истекает после", description:"Опционально. После какого количества операций задание должен завершиться."}|
|[data_variable]{type:"integer", direction:"вход", letter:"t", name:"Кол-во предметов", description:"Опционально. Максимальное количество предметов, которые манипулятор заберет за поворот."}|
|[data_variable]{type:"string", direction:"вход", letter:"i", name:"Направление загрузки", description:"Опционально значение: south, west, north, east, или Integer 0-3 (respectively)."}|
# basic_commands_2
|[data_variable]{type:"string", direction:"вход", letter:"o", name:"Направление выгрузки", description:"Опционально значение: south, west, north, east, или Integer 0-3 (respectively)."}|
|[data_variable]{type:"integer", direction:"вход", letter:"1", name:"Дистанция загрузки", description:"Опционально. Integer 1-2."}|
|[data_variable]{type:"integer", direction:"вход", letter:"0", name:"Дистанция выгрузки", description:"Опционально. Integer 1-2."}|
# advanced_inserter
|[crafting]{source:"inserter_advanced"}|
Продвинутый манипулятор - улучшенная версия манипулятора, которая позволяет фильтровать и выбирать определенные предметы, а также позволяет брать\ставить ящики и бочки с\на вагонетки. Продвинутый манипуляторы работают намного быстрее, но требуют больше энергии.
# advanced_commands
**Input Variables:**<br>
|[data_variable]{type:"string", direction:"вход", letter:"c", name:"Задание", description:"Возмонные значение: add, remove, and clear."}|
|[data_variable]{type:"string", direction:"вход", letter:"a", name:"Тип задания", description:"Возмонные значение: item, block_place, form_minecart, into_minecart."}|
|[data_variable]{type:"itemstack", direction:"вход", letter:"s", name:"Стак", description:"Опционально. Itemstack или OreDict String, c которым предметы должны совпадать"}|
# fluid_inserter
|[crafting]{source:"inserter_fluid"}|
Жидкостный манипулятор является точным ограничителем перемещения жидкости. В некоторых ситуациях требуется ввести только определенное количество жидкости. Это устройство выполняет именно эту функцию.
# fluid_inserter_commands
|[data_variable]{type:"string", direction:"вход", letter:"m", name:"Режим взятия жидкости", description:"Возмонные значение: set, add"}|
|[data_variable]{type:"integer", direction:"вход", letter:"c", name:"Кол-во жидкости", description:"значение в миллибакетах (mB)"}|
