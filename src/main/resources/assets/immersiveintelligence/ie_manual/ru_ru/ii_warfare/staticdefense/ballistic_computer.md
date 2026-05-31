# meta
Баллистический компьютер
Числа интегрированные электронами
# intro
|[multiblock]{mb:"II:BallisticComputer"}|
Балистический компьютер - сложный калькулятор преобразующий относительные координаты цели в направление выстрела в виде [данных](data_main.md) для [орудий](artillery_howitzer.md).
# data1
|[data_variable]{type:"integer", direction:"вход", letter:"x", name:"X-координата", description:"Относительная X-координата"}|
|[data_variable]{type:"integer", direction:"вход", letter:"y", name:"Y-координата", description:"Относительная Y-координата"}|
|[data_variable]{type:"integer", direction:"вход", letter:"z", name:"Z-координата", description:"Относительная Z-координата"}|
|[data_variable]{type:"float", direction:"вход", letter:"m", name:"Масса снаряда", description:"Баллистическая масса"}|

Ввод этих данных в балистический компьютер выдаст ["p" (тангаж)] и ["y" (рыскание)].
# data2
|[data_variable]{type:"float", direction:"выход", letter:"p", name:"Тангаж", description:"Тангаж цели, от 0 до -105"}|
|[data_variable]{type:"float", direction:"выход", letter:"y", name:"Рыскание", description:"Рыскание цели, от 0 до 360"}|