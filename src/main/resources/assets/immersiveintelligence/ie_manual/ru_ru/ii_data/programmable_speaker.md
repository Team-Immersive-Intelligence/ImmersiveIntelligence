# meta
Программируемая сирена
Звуки счастливого инженера
# intro
|[crafting]{source:"programmable_spkr"}|
Программируемая сирена — это устройство, издающее звук при подаче сигнала красного камня. Красный камень и провод данных можно подключать с любой стороны.
Сигнал данных можно использовать для установки звука, высоты тона, громкости и режима повтора. В качестве альтернативы, сигнал красного камня также позволяет устанавливать громкость.
# data
**Data inputs:**<br>
|[data_variable]{type:"boolean", direction:"out", letter:"o", name:"Play Once", description:"If true, sound is played once, regardless of redstone signal"}|
|[data_variable]{type:"string", direction:"out", letter:"s", name:"Sound ID", description:"Example: immersiveintelligence:siren"}|
|[data_variable]{type:"integer", direction:"out", letter:"v", name:"Volume", description:"Value must be between 0 and 100"}|
|[data_variable]{type:"integer", direction:"out", letter:"t", name:"Tone/Pitch", description:"Value must be between 0 and 200. 100 is normal pitch."}|


