# meta
Химический маляр
Стоит тысячи кистей
# intro
@hammer;level_advanced_industrial
|[multiblock]{mb:"II:ChemicalPainter"}|
**Химический маляр** - машина, используемая для окрашивания предметов в точно заданный цвет с помощью чернил.
|[text]{mb:"II:ChemicalPainter"}|
# details
Раскрашивать предметы вручную утомительно и не очень аккуратно, можно потратить много краски впустую, просто пытаясь добиться нужного цвета.
[Химический маляр] устраняет эту проблему.

Бирюзовые, пурпурные, желтые и черные чернила подаются отверстие для подачи жидкости в задней части маляра.
К верхнему коннектору должно быть подведено электричество. Предметы покраски можно вставлять вручную или по конвееру.
# uses
Помимо окрашивания таких предметов, как шерсть или ковер, с помощью химического маляра можно наносить маркировку на снаряды, чтобы их было легче идентифицировать визуально.
Большинство окрашенных предметов впоследствии можно обесцветить с помощью [химической ванны](chemical_bath), чтобы вернуть им первоначальный цвет.
# interface
Цвет задается через интерфейс или с помощью системы обработки данных. В интерфейсе есть 3 кнопки для переключения цветовой модели:
[R] - [RGB]  
[C] - [CMYK]  
[H] - [HSV]

Если требуется управление данными, следующая страница содержит описания необходимых переменных.
# data_output
**Data input:**<br>
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"Paint Color", description:"A number representing the RGB paint color. In its hexadecimal form its digits should be in RRGGBB form, where R is red, G is green and B is blue"}|
|[data_variable]{type:"string", direction:"in", letter:"p", name:"Paint Color", description:"A string hex code for the color without any additional leading or trailing characters"}|
# data_callback
Callback Variables
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_progress", label:"Production Progress", returns:"Progress value 0:1"}|
|[data_callback]{type:"integer", name:"get_color", label:"Color", returns:"Currently used color as a RGB integer"}|
|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
