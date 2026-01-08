# meta
Скануючий конвеєр
Бери камеру!
# intro
@hammer;level_circuits
|[multiblock]{mb:"II:ScanningConveyor"}|
**Скануючий конвеєр** – це пристрій, який використовується для точного [виявлення елементів](data_types.md#itemstack) шляхом надсилання [пакетів даних](data_main.md#packetsbasics).
|[text]{mb:"II:ScanningConveyor"}|
# details
Коли елемент проходить через конвеєрний сканер, його властивості зчитуються та негайно надсилаються у вигляді пакета даних із використанням типу даних **ItemStack**.
Так само, як і звичайний [Конвеєр] (conveyor), використовуючи сигнал редстоуну, вимкніть його.

Для подальшої обробки отриманого пакету можна використовувати "ALM" із [відповідною схемою](functional_circuits.md#itemstack).
Найчастіше він використовується як частина лічильника предметів, машини для сортування або як тригер, що активується певним предметом.
# scanner_variables
|[data_variable]{type:"itemstack", direction:"out", letter:"s", name:"Scanned item", description:"Recently scanned item (with its id, durability and NBT)"}|