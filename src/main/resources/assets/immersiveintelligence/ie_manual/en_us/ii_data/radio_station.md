# meta
Radio Station
The Wireless Wire™

# intro
@hammer_electric;level_circuits
|[multiblock]{mb:"II:RadioStation"}|
The **Radio Station** is a device for sending, receiving and relaying [Data Packets](data_main.md#packetsbasics) wirelessly.
|[text]{mb:"II:RadioStation"}|

# details
A **Radio Station** can send [Packets](data_main.md#packetsbasics) to compatible radio devices up to <config;i;radio_station_range> blocks distance.
Poor weather conditions such as [rain] or [snow] will [decrease this range], so while planning construction of a radio network, an engineer should use ranges negating this effect.
|[scenario]{}|

# tuner
The **Radio Station**'s frequency can be set by using a **Radio Tuner**.
|[crafting]{source:"tuner"}|
There are 2 versions of the **Tuner** with different [ranges of frequencies] avaliable.
To [change] the current frequency of the tuner, *sneak and scroll up or down*.
To [check] the frequency a device is set, *sneak-right click* it with the Tuner.

Keep in mind that [some Data Devices](explosives_mines#radio_satchel0) use a limited [Basic Frequency Range], which makes them unable to be configured with frequencies above [<config;i;radio_station_range>].
