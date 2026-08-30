# meta
Баллистический компьютер
Числа интегрированные электронами
# intro
|[multiblock]{mb:"II:BallisticComputer"}|
Балистический компьютер - сложный калькулятор преобразующий относительные координаты цели в направление выстрела в виде [данных](data_main.md) для [орудий](artillery_howitzer.md).
# guide
При расчете тангажа и рыскания [баллистический компьютер] рассчитывает *относительные* координаты.
Это означает, что для компьютера координаты [0,0,0] является центром управляемой машины. Убедитесь, что вы правильно рассчитали координаты, чтобы ваши снаряды попали в цель.
[Цель] может быть передана с использованием [vector](../../ii_data/data_types.md#vector), для чего требуется [усовершенствованная МВД](../../ii_data/data_input_machine.md#advanced_data_upgrade) или [xyz координаты](../../ii_data/data_types.md#float).
# target
|[data_variable]{type:"vector", direction:"вход", letter:"v", name:"Вектор цели", description:"Относительное положение цели в виде вектора"}|
|[data_variable]{type:"integer", direction:"вход", letter:"x", name:"X-координата", description:"Относительная X-координата"}|
|[data_variable]{type:"integer", direction:"вход", letter:"y", name:"Y-координата", description:"Относительная Y-координата"}|
|[data_variable]{type:"integer", direction:"вход", letter:"z", name:"Z-координата", description:"Относительная Z-координата"}|
# projectile
|[data_variable]{type:"itemstack", direction:"вход", letter:"s", name:"Ammunition stack", description:"Ammunition item used to read both mass and standard velocity"}|
|[data_variable]{type:"float", direction:"вход", letter:"m", name:"Масса снаряда", description:"Баллистическая масса"}| 
|[data_variable]{type:"float", direction:"вход", letter:"f", name:"Скорость снаряда", description:"Настраиваемая скорость снаряда. По умолчанию используется скорость снаряда тяжёлой артиллерии"}|
|[data_variable]{type:"string", direction:"вход", letter:"t", name:"Тип снаряда", description:"Стандартныее типы снарядов", values:[["artillery_8bCal","Тяжёлой артиллерии"], ["artillery_6bCal_long","Средней артиллерии"], ["artillery_6bCal","Лёгкой артиллерии"], ["mortar_6bCal","Миномётнной артиллерии"]]}|
# projectile2
|[data_variable]{type:"boolean", direction:"вход", letter:"d", name:"Прямой наводкой", description:"Используйте стрельбу прямой наводкой вместо стандартной баллистической траектории"}|

При указании [Ammunition stack](bullet_production.md) задаются значения [массы] и [скорости].
В противном случае входной пакет должен содержать значения [массы] и [скорости]. Скорость может быть передана в виде [числа](../../ii_data/data_types.md#float) или путем указания [типа боеприпаса].
# output
Ввод правильных данных о цели и снаряде приведет к получению значений [рыскания], [тангажа] и [времени полета снаряда].
Все допустимые входные переменные, перечисленные на предыдущих страницах, удаляются из выходного пакета.
|[data_variable]{type:"float", direction:"выход", letter:"p", name:"Тангаж", description:"Тангаж цели, от 0 до -105"}|
|[data_variable]{type:"float", direction:"выход", letter:"y", name:"Рыскание", description:"Рыскание цели, от 0 до 360"}|
|[data_variable]{type:"float", direction:"выход", letter:"t", name:"Время полета", description:"Расчетное время, за которое снаряд достигнет цели, в тиках"}|
# passthrough
В случае, если углы рыскания и тангажа ** введены** в [Баллистический компьютер], он передаст их на **вывод** без выполнения каких-либо вычислений.
<br>
|[data_variable]{type:"float", direction:"выход", letter:"p", name:"Тангаж", description:"Тангаж цели, от 0 до -105"}|
|[data_variable]{type:"float", direction:"выход", letter:"y", name:"Рыскание", description:"Рыскание цели, от 0 до 360"}|