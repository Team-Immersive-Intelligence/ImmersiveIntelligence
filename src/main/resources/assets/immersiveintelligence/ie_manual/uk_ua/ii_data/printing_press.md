# meta
Друкарня
Інженерна книга та перо
# 0
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
**Друкарський прес** — це машина, яка може друкувати текст на вимогу, коли отримує пакет даних.
|[text]{mb:"II:PrintingPress"}|
# 1
Друкарський папір — це спеціальний розріджений варіант стандартного, що використовується в книгах, створюється пресуванням паперу в металевому пресі.
Кількість використаного чорнила залежить від кольору надрукованого символу.

Виведені друковані сторінки слід негайно видалити, оскільки прес може зберігати лише 12 із них у кошику.
# data_inputs
|[text]{text:"Data Inputs",bold:1a}|
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"],["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."],["orders","A page with step-by-step orders for a military or logistic unit"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Amount of copies", description:"How many pages will be printed"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Text to print", description:"Text content that will be printed", requirements:{m:"text/code"} }|
# data_inputs_upgrade
[] дозволяє друкувати нові типи
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Output mode", description:"What type of page that will be printed", values:[["text","A page containing text"],["code","A page containing code written in the POL programming language"],["blueprint","A page containing a construction blueprint to be used manually or by a Logistics Drone"],["punchtape","A punchtape with variables of the received packet, except this one printed. Doesn't use any ink."],["orders","A page with step-by-step orders for a military or logistic unit"]]}|
# data_callback
|[text]{text:"Data Callback",bold:1b}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|
# data_callback 2
|[text]{text:"Data Callback",bold:1b}|

|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_paper", label:"Paper amount", returns:"Blank paper pages amount"}|
