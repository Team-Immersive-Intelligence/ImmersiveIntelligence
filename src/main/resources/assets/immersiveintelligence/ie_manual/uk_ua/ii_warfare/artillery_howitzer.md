# meta
Артилерійська гаубиця
Сестра Берти
# intro
@level_circuits,hammer_electric,upgradable
|[multiblock]{mb:"II:ArtilleryHowitzer"}|
*Пристрій прецизійного бомбардування*, широко відомий як **Артилерійська гаубиця**, — це зброя непрямого наведення, яка чудово підходить для ураження цілей на великих відстанях.
|[text]{mb:"II:ArtilleryHowitzer"}|
# details
Осередок **Гаубиці** складається з бетонного бункера з [гарматною платформою], [механізмом заряджання], командними столами та [сталевими дверима], які можна відкрити за допомогою [сигналу червоного каменю].<br>
[Боеприпаси](bullet_production.md#bullet) мають бути надані до конвеєра [вниз], а сховище для відпрацьованих гільз — до конвеєра [вгору].
Для роботи машини потрібна велика кількість енергії.
# operation
**Гаубиця** працює за допомогою команд, які надходять до неї через [пакети даних](data_main#packetsbasics).<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"What action the howitzer will preform", values:[["load","Loading ammunition to platform"],["unload","Unloading from platform"],["aim","Will aim at given angles, but will not fire"],["fire","Will fire a loaded shell at given angle"]]}|
Крім того, для виконання деяких команд, як-от [вогонь] чи [прицілювання], можуть знадобитися додаткові змінні. Приклад пакету може виглядати так:
|[data_packet]{h:34,data:{c:{Type:"string",Value:"fire"},p:{Type:"float",Value:45},y:{Type:"float",Value:123}}}|
# loading
Перш ніж стріляти з рушниці, її потрібно зарядити деякими [боєприпасами] (bullet_production.md#bullet). [Платформа] гармати має сховище для 4 снарядів для швидкого доступу під час стрільби. Щоб завантажити їх, використовуйте одну з команду [перезаряджання].<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"What action the howitzer will preform", values:[["load","Loads a single shell"],["load_all","Loads up to 4 shells until there is no space in the platform"],["load1","Loads a shell to 1st slot"],["load2","Loads a shell to 2nd slot"],["load3","Loads a shell to 3rd slot"],["load4","Loads a shell to 4th slot"]]}|
# unloading
Після того, як гармата стріляє, вона залишає [порожню гільзу] в [платформному сховищі]. Щоб вилучити гільзу або боєприпас зі сховища, використовуйте одну з команд [вивантажити]. <br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"What action the howitzer will preform", values:[["load","Loads a single shell"],["load_all","Fully empties the platform storage"],["load1","Unloads the shell from 1st slot"],["load2","Unloads the shell from 2nd slot"],["load3","Unloads the shell from 3rd slot"],["load4","Unloads the shell from 4th slot"]]}|
# firing
Щоб віддати команду [стріляти] або [прицілитись], [переданий пакет](data_main#packetsbasics) має містити змінні для [вертикального] та [горизонтального кута], на який буде орієнтовано гармату.<br>
|[data_variable]{type:"integer", direction:"in", letter:"y", name:"Gun yaw", description:"The horizontal angle the weapon points", value:"0:360"}|
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"Gun pitch", description:"Gun vertical angle, up or down", value:"0:-105"}|
<br>
Замість прямої передачі кута можна використовувати [балістичний комп’ютер](ballistic_computer) для інтерпретації [3D-положення] та [сканування](scanning_conveyor) [боєприпасів].
# firing in batch
Привіт
# callback0
Data Callback
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_state", label:"Current State", returns:"idle, loading, unloading, shooting"}|
|[data_callback]{type:"integer", name:"get_state_num", label:"Current State in numbers", returns:"0,1,2,3"}|
|[data_callback]{type:"integer", name:"get_state_progress", label:"State progress in percentage", returns:"1%-100%"}|
# callback1
|[data_callback]{type:"integer", name:"get_yaw", label:"Current yaw", returns:"Current yaw"}|
|[data_callback]{type:"integer", name:"get_pitch", label:"Current pitch", returns:"Current pitch"}|
|[data_callback]{type:"integer", name:"get_planned_yaw", label:"Planned gun yaw", returns:"Planned yaw"}|
|[data_callback]{type:"integer", name:"get_planned_pitch", label:"Planned gun pitch", returns:"Planned pitch"}|
# callback2
|[data_callback]{type:"integer", name:"get_platform_height", label:"Current platform height", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_door_opened", label:"Current state of door", returns:"Opened or closed"}|
|[data_callback]{type:"integer", name:"get_door_opening", label:"Current ongoing of door", returns:"Opening or closing"}|