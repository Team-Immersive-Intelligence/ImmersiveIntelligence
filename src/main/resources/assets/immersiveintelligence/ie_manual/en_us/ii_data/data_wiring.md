# meta
Data Wiring
Not really powerlines

# intro
Data can be transmitted in two ways: [wired] and [wireless].  
In both cases there is no delay between sending and receiving **Data Packets**, this means the entire system is synchronised all the times and __there is no case where a packet might have arrived too late or too early__.

# wired
The **Wired** data system relies on **Connectors** - the end points of it, **Relays** - which allow connecting multiple wires to form and extend a [Data Network] and finally, **Data Devices** which send and/or receive data.
  
|[scenario]{source:"wired_connection"}|

# data_cable
|[crafting]{source:"%SECTION%"}|  
The **Data Cable** is a transmission medium created from multiple twisted-pair copper wires coated in a white-colored insulating material.  
It allows fast, reliable and relatively long-distance transmission of [Data Packets](data_main.md) without any loss in the information they store.

# connector
|[crafting]{source:"%SECTION%"}|  
The **Data Connector** serves as an endpoint of a **Data Network**. It is placed on a side of a **Data Device** to allow it sending and/or receiving **Packets**.
In a similar manner to its [Electrical Counterparts](basic_wiring), only one wire can be connected to a Data Connector. To extend the network, use a [Data Relay](#relay).


# connector_colors
A **Data Network** can contain multiple **Connectors** of various colors. When a packet is sent through a **Connector**, it will only be received on **Connectors** of the same color.
Using the [Engineer's Hammer] on a **Connector** will change its [color].
This allows multiple isolated **Connectors** to share **Relays**, [without interfering] with each other.

# relay
|[crafting]{source:"%SECTION%"}|  
The **Data Relay** serves as a connection point between multiple [Connectors](#connector).
It does not interact with **Data Devices**.

# duplex_connector
|[crafting]{source:"%SECTION%"}|  
The **Duplex Connector** is a Data Connector with separate colors for [input] and [output].
It is very useful in situations where a **Device** serves as both input and output and has only a single port.
Using the [Engineer's Hammer](introduction#introductionHammer) on it will change the **Connector's** [output color] when sneaking and [input color] when not.

# debugger
|[crafting]{source:"%SECTION%"}|  
The **Data Debugger** is a special type of [Data Relay](#relay), that captures, stores and displays the **Packet** flowing through it.
It operates in 3 modes, that can be switched between by using a [Hammer](tools), with default one being the [Transceiver Mode]. 

# debugger2
In the [Transmitter] mode, the **Debugger** will transmit a test package when provided a redstone signal. In the [Receiver] mode, the  [Transceiver] combines functions of both.

# wireless1
**Wireless** data transfer is an extension over the **Wired** one. 
It uses radio-capable devices, such as the [Radio Station](radio_station), [Radio Backpack](radio_station) and [Radio Explosives](radio_station).  
For a **Packet** to be transmitted **wireless**, the [transmitter] and [receiver] have to use the same [frequency] and the [receiver] has to be in range of the [transmitter] or a [relay] of its signal.

# wireless2
Any radio device of the same frequency will also act as [relays], extending your wireless network.  
When designing radio networks, keep in mind that every device has a [limited maximum transmission range], which changes depending on [weather].  

