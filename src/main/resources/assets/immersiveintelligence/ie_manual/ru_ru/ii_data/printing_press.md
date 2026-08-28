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
|[data_variable]{type:"string", direction:"вход", letter:"m", name:"Тип документа", description:"Тип документа, который будет напечатан", values:[["text","Страница, содержащая текст"],["code","Страница, содержащая код, написанный на языке программирования POL"],["blueprint","Страница, содержащая план строительства, который будет использоваться вручную или с помощью логистического дрона"],["logi_tag","Логистическая метка, используемая для маркировки контейнеров и товаров"]]}|
|[data_variable]{type:"integer", direction:"вход", letter:"a", name:"Кол-во копий", description:"Сколько страниц будет напечатано"}|
|[data_variable]{type:"string", direction:"вход", letter:"t", name:"Text to print", description:"Текстовое содержимое, которое будет напечатано", requirements:{m:"text/code"}}|
Печать [логистического манифеста](../ii_logistics/task_system.md#logitags) требует другого набора переменных и описана на [специальной странице](../ii_logistics/task_system.md#logitag_printing).

# punchtapes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/punchtapes"}|
Улучшение [обработчик перфоленты] позволяет печатать [перфоленты](punchtapes.md) со всеми переменными полученных пакетов, кроме «a», «m» и «t».
**Вместо пустой страницы предоставьте [пустую перфоленту].**
|[data_variable]{type:"string", direction:"вход", letter:"m", name:"Тип документа", description:"Тип документа, который будет напечатан", values:[["punchtape","Перфолента с напечатанными параметрами полученного пакета, за исключением "a", "m" и "t". Не затрачивает чернил."]]}|
# enveloper
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/enveloper"}|
Сворачиватель позволяет печатать [Письма в конвертах](envelopes.md), которые будут адресованы и отправлены другим игрокам.
|[wip_notice]|
# batching
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/batching"}|
Улучшение [Механизм дозирования] позволяет печатать несколько страниц текста одновременно, что позволяет печатному станку создавать газеты и книги.
|[wip_notice]|
# data_callback
**Data Callback:**
|[data_callback]{type:"integer", name:"get_ink", label:"Чернила (Ч)", returns:"Кол-во чёрных чернил (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Чернила (Б)", returns:"Кол-во бирюзовых чернил (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Чернила (Ж)", returns:"Кол-во жёлтых чернил (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Чернила (П)", returns:"Кол-во пурпурных чернил (mB)"}|
|[data_callback]{type:"integer", name:"get_energy", label:"Запасённая энергия", returns:"Кол-во запасённой энергии (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Бумага", returns:"Кол-во листов бумаги"}|