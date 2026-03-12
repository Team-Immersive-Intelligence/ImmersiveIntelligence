# meta
Печатный пресс
Книга и перо инженера

# press0
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
**Печатный пресс** — это машина, которая может печатать текст по запросу, полученному из пакета данных.
|[text]{mb:"II:PrintingPress"}|

# press1
|[machine_recipe]{machine:"metal_press", source:"paper_page"}|
[Печатная бумага] — это специальная истонченная версия стандартной бумаги, используемой в книгах. Она изготавливается путем [прессования листа бумаги в металлическом прессе].
Отпечатанные страницы следует как можно скорее извлекать из печатного станка, так как в выходной корзине станка помещается только [12 страниц].

# press_usage
Управление печатным станком осуществляется с помощью [системы данных](data_main); необходимые переменные описаны на следующей странице.
Помимо [Печатной бумаги], для печати текста на странице печатному станку необходимы жидкие чернила. Количество используемых чернил зависит от цвета печатаемого символа.
Чернила создаются в [смесителе](mixer) путем смешивания воды и соответствующего красителя для получения черных, голубых, пурпурных или желтых чернил.
Печатный станок также может быть [модернизирован](#punchtapes_upgrade) для расширения его функциональности.

# data_inputs
**Data Inputs:**
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"],["logi_tag","A logistics tag used to mark containers and items"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Amount of copies", description:"How many pages will be printed"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Text to print", description:"Text content that will be printed", requirements:{m:"text/code"}}|
# punchtapes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/punchtape_processor"}|
Улучшение [обработчик перфоленты] позволяет печатать [перфоленты](punchtapes.md) со всеми переменными полученных пакетов, кроме «a» и «m».
**Вместо пустой страницы предоставьте [пустую перфоленту].**
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"The type of document that will be printed", values:[["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."]]}|

# data_callback
|[text]{text:"Data Callback",bold:1b}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Paper amount", returns:"Blank paper pages amount"}|
