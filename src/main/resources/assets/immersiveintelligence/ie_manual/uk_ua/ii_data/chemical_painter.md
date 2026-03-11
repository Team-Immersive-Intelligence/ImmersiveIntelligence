# meta
Художник-хімік
Коштує тисячу пензлів
# intro
@hammer;level_advanced_industrial
|[multiblock]{mb:"II:ChemicalPainter"}|
**Хімічний маляр** — це машина, яка використовується для фарбування предметів у точно встановлений колір за допомогою чорнила.
|[text]{mb:"II:ChemicalPainter"}|
# details
Фарбувати предмети вручну – справа нудна й не дуже точна, багато фарби можна витратити просто на те, щоб отримати потрібний колір.
[Хімічний маляр] усуває цю проблему.

Блакитне, пурпурове, жовте та чорне чорнило потрібно подати до чотирьох вхідних отворів на задній панелі пристрою.
Електрика повинна бути подана до верхнього вхідного порту. Предмети можна вставляти вручну або через конвеєр, спрямований усередину.
# interface
Для встановлення кольору використовуйте інтерфейс або систему даних. В інтерфейсі є 3 кнопки, які дозволяють перемикати колірну модель:
[R] - [RGB]  
[C] - [CMYK]  
[H] - [HSV]

Хімічний маляр можна використовувати для нанесення маркування на кулі, щоб їх було легше візуально ідентифікувати.
Більшість із цих предметів можна згодом очистити за допомогою [хімічної ванни](chemical_bath), щоб відновити свій початковий колір.
# data_output
Вихідні змінні
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"Paint Color", description:"Число, що позначає колір фарби RGB. У шістнадцятковій формі його цифри мають бути у формі RRGGBB, де R — червоний, G — зелений, а B — синій"}|
|[data_variable]{type:"string", direction:"in", letter:"p", name:"Paint Color", description:"Шістнадцятковий код рядка для кольору без будь-яких додаткових символів на початку або в кінці"}|
# data_callback
Змінні зворотного виклику
|[data_callback]{type:"integer", name:"get_color", label:"Color", returns:"Currently used color as a RGB integer"}|
|[data_callback]{type:"integer", name:"get_color_hex", label:"Color (hexadecimal)", returns:"Returns currently used color as a hex string"}|

|[data_callback]{type:"integer", name:"get_ink", label:"Ink Level", returns:"Black ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"Cyan Ink Level", returns:"Cyan ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"Yellow Ink Level", returns:"Yellow ink amount (mB)"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"Magenta Ink Level", returns:"Magenta ink amount (mB)"}|

|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
