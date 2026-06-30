# meta
Артиллерийская Установка
Сестра Берты
# intro
@level_circuits,hammer_electric,upgradable
|[multiblock]{mb:"II:ArtilleryHowitzer"}|
*Устройство Точечной Бомбардировки*, так же известное как **Артиллерийская Установка** это оружие непрямой наводки, прекрасно подходящее для атаки далеко расположенных целей.
# forming_block
Она собирается нажатием [молотом инженера](introduction#introductionHammer) по бетону с противоположной стороны от краснокаменного инженерного блока.
# details
**Артиллерийская Установка** состоит из бетонного бункера, [орудийной платформы], [заряжающего механизма], столов управления и [стального люка], который можно открыть, подав [сигнал редстоуна].<br>
[Снаряды](bullet_production.md#bullet) подаются в [спускающий] конвейер, а отработанные гильзы выбрасываются из [восходящего] конвейера.
Машина требует **большого количества энергии** для работы.
# operation
**Установка** управляется командами с помощью [пакетов данных](data_main#packetsbasics).<br>
|[data_variable]{type:"string", direction:"вход", letter:"c", name:"Приказ", description:"Какие приказы можно отдать", значения:[["load","Заряжает платформу"],["unload","Разряжает платформу"],["aim","Will aim at given angles, but will not fire"],["fire","Огонь"]]}|
Также, некоторые команды вроде [огонь] и [наводка] могут требовать дополнительных переменных для их работы, пример пакета может выглядеть так:
|[data_packet]{h:34,data:{c:{Type:"string",Value:"fire"},p:{Type:"float",Value:45},y:{Type:"float",Value:123}}}|
# loading
Перед выстрелом, необходимо снарядить батарею [снарядами](bullet_production.md#bullet). Артиллерийская [платформа] имеет хранилище на 4 снаряда для быстрого доступа во время ведения огня. Чтобы загрузить их, используйте одну их команд [загрузки].<br>
|[data_variable]{type:"string", direction:"вход", letter:"c", name:"Команда загрузки", description:"Команда загрузки снарядов в хранилище платформы", значения:[["load","загружает 1 снаряд"],["load_all","загружает все (4) снаряда"],["loadn","загружает снаряд из n слота (значения n:1-4)"]]}|
# unloading
После выстрела пушка выгружает [пустую гильзу] в [хранилище платформы]. Чтобы убрать убрать гильзу или снаряд из хранилища, используйте одну из команд [разгрузки]. <br>
|[data_variable]{type:"string", direction:"вход", letter:"c", name:"Команда разгрузки", description:"Команда разгрузки снарядов из хранилище платформы", значения:[["unload","разгружает 1 снаряд"],["unload_all","разгружает все (4) снаряда"],["unloadn","разгружает снаряд из n слота (значения n:1-4)"]]}|
# firing
Для выдачи команды [огня] или [наводки], [пакет данных] должен содержать переменные для [вертикального (Тангаж)] и [горизонтального угла (Рысканье)], на которые пушка будет наклонена.
|[data_variable]{type:"float", direction:"вход", letter:"y", name:"Рысканье", description:"Горизантальный угол", значения:"0 to 360"}|
|[data_variable]{type:"float", direction:"вход", letter:"p", name:"Тангаж", description:"Вертикальный уровень", значения:"0 to -105"}|
Вместо задачи угла напрямую, [баллистический компьютер](ballistic_computer) может использоваться для конвертации [координатов цели в углы наклона], а [сканирующий конвейер](scanning_conveyor) для считывания веса [снаряда].
# callback0
**Обратные данные**
|[data_callback]{type:"integer", name:"get_energy", label:"Накопленная энергия", returns:"Кол-во запасённой энергии (IF)"}|
|[data_callback]{type:"string", name:"get_state", label:"Current State", returns:"idle, loading, unloading, shooting"}|
|[data_callback]{type:"integer", name:"get_state_num", label:"Current State in numbers", returns:"0,1,2,3"}|
|[data_callback]{type:"float", name:"get_state_progress", label:"State progress in percentage", returns:"Progress value 0:1"}|
|[data_callback]{type:"float", name:"get_yaw", label:"Текущие рысканье", returns:"Горизантальный угол при текущем повороте батареи"}|
|[data_callback]{type:"float", name:"get_pitch", label:"Текущий тангаж", returns:"Вертикальный угол при текущем повороте батареи"}|
|[data_callback]{type:"float", name:"get_planned_yaw", label:"Планируемые рысканье", returns:"Горизантальный угол по окончанию поворота батареи"}|
|[data_callback]{type:"float", name:"get_planned_pitch", label:"Планируемый тангаж", returns:"Вертикальный угол по окончанию поворота батареи"}|
# callback2
|[data_callback]{type:"float", name:"get_platform_height", label:"Current platform elevation", returns:"Progress value 0:1"}|
|[data_callback]{type:"string", name:"get_door_opened", label:"Текущее состояние двери", returns:"Opened(открытое) или closed(закрытое)"}|
|[data_callback]{type:"string", name:"get_door_opening", label:"Current ongoing of door", returns:"Opening or closing"}|