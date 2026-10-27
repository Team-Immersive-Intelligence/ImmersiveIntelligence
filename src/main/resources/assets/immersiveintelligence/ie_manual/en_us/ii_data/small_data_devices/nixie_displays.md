# meta
Nixie Tube Displays
# displays
|[crafting]{source:"crafting"}|
[Nixie Tube Displays] are used to display [numbers](../data_types.md#integer) and [text](../data_types.md#string) provided through [data](../data_main.md).
Data Packets are accepted through the bottom or left side. *Characters* from set variables are displayed on the [tubes], then the remaining text and the rest of the packet is passed to the right side, which allows *display chaining*.
# settings
The **machine** operates in two modes set in its interface: the [Digit mode] or [Number/Text mode].
<br>
In [Digit mode], select one variable for each tube. Each tube takes the first character of its variable. For example, a=7 and b="K" show [7K] on a Double display.
<br>
In [Number/Text mode], select one variable. Its characters fill the tubes from left to right.
# chaining
**Displays** can be chained together by placing them in a row.
For example, when three **displays** are placed in a row and the first one receives a packet:
|[data_packet]{data:{a:{Type:"integer",Value:123456}}}|
They will show [12], [34] and [56].