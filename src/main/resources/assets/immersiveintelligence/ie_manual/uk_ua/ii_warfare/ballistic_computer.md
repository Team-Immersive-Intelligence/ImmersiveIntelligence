# meta
Балістичний комп'ютер
Електронно інтегровані номери
# 0
|[multiblock]{mb:"II:BallisticComputer"}|
Балістичний комп’ютер — це складний обчислювальний пристрій, який перетворює вектор позиції та масу кулі на поворот і кут, який використовується в більшості зброї, що керується даними.
# 1
|[data_variable]{type:"integer", direction:"in", letter:"x", name:"X-coordinates", description:"The x-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"y", name:"Y-coordinates", description:"The y-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"z", name:"Z-coordinates", description:"The z-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"m", name:"Shell mass", description:"The ballistic mass"}|

Якщо ввести ці значення, балістичний розрахунок виведе значення «p» (нахил) і «y» (поворот). Дивіться наступну сторінку
# 2
|[data_variable]{type:"float", direction:"out", letter:"p", name:"Pitch value", description:"Pitch value to hit your location. From 0-(-105)"}|
|[data_variable]{type:"float", direction:"out", letter:"y", name:"Yaw value", description:"Yaw value to hit your location. From 0-360"}|