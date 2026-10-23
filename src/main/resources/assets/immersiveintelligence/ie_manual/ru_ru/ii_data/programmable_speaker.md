# meta
Программируемая сирена
Звуки счастливого инженера
# intro
|[crafting]{source:"programmable_spkr"}|
Программируемая сирена — это устройство, издающее звук при подаче сигнала красного камня. Красный камень и провод данных можно подключать с любой стороны.
Сигнал данных можно использовать для установки звука, высоты тона, громкости и режима повтора.
# intro1
В качестве альтернативы, сигнал красного камня также позволяет устанавливать громкость.
# data
**Входы данных:**<br>
|[data_variable]{type:"boolean", direction:"in", letter:"o", name:"Воспроизвести 1 раз", description:"Если true, звук воспроизводится один раз, независимо от сигнала редстоуна."}|
|[data_variable]{type:"string", direction:"in", letter:"s", name:"ID Звука", description:"По умолчанию: immersiveintelligence:siren"}|
|[data_variable]{type:"integer", direction:"in", letter:"v", name:"Громкость", description:"Значение между 0 и 100"}|
|[data_variable]{type:"integer", direction:"in", letter:"t", name:"Высота тона", description:"Значение между 0 и 200. 100 - нормальная высота."}|


