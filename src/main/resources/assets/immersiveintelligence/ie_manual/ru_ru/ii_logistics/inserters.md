# meta
Манипуляторы
Никакого желтого цвета!
# intro
|[item_display]{source:"inserters_full"}|
<br>The Precision Insertion Device, also known as the "Inserter," is a device used to transfer items from one inventory to another. Item Inserters consists of an arm and motors for rotation and handling the item pickup. Fluid Inserters use pipes and automatic valves. Inserters are able to take and output from and to any horizontal side desired.
# intro_2
The Inserter's sides can be configured with a [hammer](introduction#introductionHammer). Right click to set an input. The device will turn to face that направление. Sneak to set an output. The output can be any of the non-input sides.<br>
Для работы манипуляторы требуется электричество и [данные](data_main.md), указывающие им, что делать.
# basic_inserter
|[crafting]{source:"inserter_basic"}|
Манипулятор используется для простого, но контролируемого перемещения предметов. Он состоит из базовых схем и механических компонентов и представляет собой дешевый и простой способ установить, количество предметов которое нужно перенести с одной стороны на другую .
# basic_commands_1
|[data_variable]{тип:"integer", направление:"вход", переменная:"e", имя:"Истекает после", описание:"Опционально. После какого количества операций задание должен завершиться."}|
|[data_variable]{тип:"integer", направление:"вход", переменная:"t", имя:"Кол-во предметов", описание:"Опционально. Максимальное количество предметов, которые манипулятор заберет за поворот."}|
|[data_variable]{тип:"string", направление:"вход", переменная:"i", имя:"Направление загрузки", описание:"Опционально значение: south, west, north, east, или Integer 0-3 (respectively)."}|
# basic_commands_2
|[data_variable]{тип:"string", направление:"вход", переменная:"o", имя:"Направление выгрузки", описание:"Опционально значение: south, west, north, east, или Integer 0-3 (respectively)."}|
|[data_variable]{тип:"integer", направление:"вход", переменная:"1", имя:"Дистанция загрузки", описание:"Опционально. Integer 1-2."}|
|[data_variable]{тип:"integer", направление:"вход", переменная:"0", имя:"Дистанция выгрузки", описание:"Опционально. Integer 1-2."}|
# advanced_inserter
|[crafting]{source:"inserter_advanced"}|
Продвинутый манипулятор - улучшенная версия манипулятора, которая позволяет фильтровать и выбирать определенные предметы, а также позволяет брать\ставить ящики и бочки с\на вагонетки. Продвинутый манипуляторы работают намного быстрее, но требуют больше энергии.
# advanced_commands
**Input Variables:**<br>
|[data_variable]{тип:"string", направление:"вход", переменная:"c", имя:"Задание", описание:"Возмонные значение: add, remove, and clear."}|
|[data_variable]{тип:"string", направление:"вход", переменная:"a", имя:"Тип задания", описание:"Возмонные значение: item, block_place, form_minecart, into_minecart."}|
|[data_variable]{тип:"itemstack", направление:"вход", переменная:"s", имя:"Стак", описание:"Опционально. Itemstack или OreDict String, c которым предметы должны совпадать"}|
# fluid_inserter
|[crafting]{source:"inserter_fluid"}|
Жидкостный манипулятор является точным ограничителем перемещения жидкости. В некоторых ситуациях требуется ввести только определенное количество жидкости. Это устройство выполняет именно эту функцию.
# fluid_inserter_commands
|[data_variable]{тип:"string", направление:"вход", переменная:"m", имя:"Режим взятия жидкости", описание:"Возмонные значение: set, add"}|
|[data_variable]{тип:"integer", направление:"вход", переменная:"c", имя:"Кол-во жидкости", описание:"значение в миллибакетах (mB)"}|
