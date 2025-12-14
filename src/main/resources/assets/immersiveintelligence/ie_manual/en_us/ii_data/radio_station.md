# meta
Radio Station
The Wireless Wire(TM)
# intro
@hammer_electric;level_circuits
|[multiblock]{mb:"II:RadioStation"}|
The **Radio Station** is a device for sending, receiving and relaying [Data Packets](data_main.md#packetsbasics) wirelessly. Since it is an advanced structure, it requires the [Electric Hammer](electric_tools.md) to be used on the [Advanced Electronic Engineering Block].
# details
A **Radio Station** can send [Packets](data_main.md#packetsbasics) to compatible radio devices up to [<config;i;radio_station_range>] blocks distance.
Poor weather conditions such as [rain] or [snow] will [decrease this range], so while planning construction of a radio network, an engineer should use ranges negating this effect.
|[scenario]{source:"wireless_connection"}|
# tuner
The **Radio Station**'s frequency can be set by using a **Radio Tuner**.
There are 2 versions of the **Radio Tuner** with different [frequency ranges] avaliable. The aspiring engineer will typically begin with the basic radio tuner:
|[crafting]{source:"basic_radio_tuner"}|
# advtuner
For sufficiently advanced engineers, the **Advanced Radio Tuner** may be used to access a wider range of frequencies.
|[crafting]{source:"advanced_radio_tuner"}|
# usage
To [change] the current frequency of the tuner, *sneak and scroll up or down*.
To [check] the frequency a device is set, *sneak-right click* it with the Tuner.<br>
Keep in mind that [some Data Devices](explosives_mines#radio_satchel0) use the limited [Basic Frequency Range], which makes them unable to be configured with frequencies above <config;i;radio_station_range>.
