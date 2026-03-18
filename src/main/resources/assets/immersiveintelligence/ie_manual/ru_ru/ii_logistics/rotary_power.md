# meta
Энергия вращения
Things Start Turning
# torque and speed
Энергия вращения является старомодной альтернотивой электричеству. Вместо проводов и коннекторов используются [колёса] и [ремни]. Система вращения характеризуется 2 параметрами: torque - in [TU] (Torque Unit) и скорость [DPT] (Degrees per Tick). Different machines require different torque and speed to work effectively.
Машины как [лесопилка](Sawmill.md) и [механическая помпа](mechanical_pump.md) используют энергию вращения.
# torque and speed 2
If [TU] or [DPT] values surpass the limit of the machine, the machine **explodes**.
To prevent this, the cunning Engineer may use [Gearboxes](rotary_power.md#gearboxes) to merge, split, and manage rotary devices.
# transmissions
|[crafting]{source:"transmission"}|
[Коробки передач] используются для подключения к механическим устройствам, генерирующим энергию вращения. Их можно использовать для подключения [ветряной мельницы](generator#generator2) или [водяного колеса](generator#generator1) к сети [моторного ремня] или для вывода механической энергии.
# gearboxes
|[crafting]{source:"gearbox"}|
[Gearboxes] are used to link multiple [wheels] together. Their sides can be changed to input, output or nothing. Input torque from all inputs is summed and then divided equally between all outputs. You can use the gearbox to merge torque to make it higher or divide it between two or more wheels.
# wheels
|[crafting]{source:"wheels"}|
The [wheel] is used to transfer mechanical (rotary) power between devices. After placing, it can be connected to another wheel using a [motor belt], the same way [connectors](wiring) are linked with wires.
# belts
|[crafting]{source:"belt"}|
The [motor belts] are the main transmission element of every rotary power network. They differ with the amount of torque and speed they can transfer. If the torque is too high for the belt, it will detach and fall onto the ground.
# belt_materials1
|[item_display]{source:"belt_cloth"}|
<br>**Cloth belts** are fabricated from tough fabric and leather. They have the lowest torque tolerance and shortest length, but are [cheap] and fairly efficient at transferring torque.
# belt_materials2
|[item_display]{source:"belt_steel"}|
<br>**Steel tracks** are fabricated from steel plates. They have the [highest torque tolerance] and can be twice the length of cloth belts, but lose a lot of torque compared to other types.
#belt_materials3
|[item_display]{source:"belt_rubber"}|
<br>**Rubber belts** are fabricated from [vulcanized](vulcanizer.md) rubber. They have a fair torque tolerance and can be twice the length of cloth belts, and are the most efficient at transferring torque. However, the [industrial requirements](vulcanizer.md) for fabrication are much more than other types.
# gears
|[item_display]{source:"gears"}|
Gears can be used to change the ratio for Torque and Speed for [Gearboxes](rotary_power.md#gearboxes).
The Torque/Speed ratio is engraved on the gear at production for the busy Engineer to refer to at any time.






