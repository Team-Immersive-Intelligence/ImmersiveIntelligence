# meta
Data Types
Results May Vary

# strong_typing
A [Data Packet](data_main.md) can contain multiple **Types** of variables.  
Each **Type** excels in [storing] and [representing] a different kind of information: f.e. a **Number Type**, like [Integer](#integer) specializes in storing numbers, and thus mathematical expressions can be performed on it much more straight-forward than, i.e. on a [String](#string) - a **Text Type**.  

This way of storing information is called **Strong Typing** and gives each **Type** a unique purpose.    

# default_value
Each **Type** has its [default value], which is assigned to it on initialization. 
If a [non-existing] or [incompatible type] variable is referenced and [casted] to a **Type**, it returns a [defaultized] variable of that **Type** instead.
For example, if in a packet:  
|[data_packet]{data:{a:{Type:"string",Value:"Hello, World!"}}}|  
'a' - a [String](#string) is requested as [Integer](#integer), a new [defaultized] (with the type's default value: 0) [Integer](#integer) will be returned instead.

# default_value2
The same would happen in a packet like:  
|[data_packet]{data:{a:{Type:"string",Value:"123"}}}|
Despite that for a human the *text* "123" seems identical to the *number* 123, a data machine sees it very different. A text type [can't be converted] to a number type, thus it too returns a [defaultized Integer], or simply put: 0.

# default_value3
The only case where such conversion would occur properly, is between two **Compatible Types** - two types storing a similar kind of information: f.e. when converting [Float](#float) to [Integer](#integer)  
|[data_packet]{data:{a:{Type:"float",Value:123}}}|  
This mechanism is also one of the core concepts of **Strong Typing**

# data_overflow
Each **Type** is limited by a size or length number.  
This feature is necessary to ensure that there is no [Data Overflow] - a situation when a type would take too much space and corrupt the entire packet.  
This however doesn't fully prevent [Data Overflows] - they can still happen when too many [Compound Types](#types) are nested in each other.
How would a data machine react in such situation? It simply wouldn't pass the broken packet further. 

# types
Types are separated into two groups: 
**Basic Types** - used to store and represent a [simple], [basic], [singular form of information], such as a number or text  
These are: [Null](#null), [Integer](#integer), [Float](#float), [Boolean](#boolean) and [String](#string).

**Compound Types** - consisting of [multiple] **Basic Types** and [joining them into one object], for representing a more sophisticated information.
These include [ItemStack](#itemstack), [Array](#array), [FluidStack](#fluidstack), [Vector](#vector), [Entity](#entity) and [Map](#map).


# null
|[datatype]{type:"%SECTION%",x:52}|
[Null] is a [special] data type, which has [no value].
It is the default type of an [undefined] variable.

# integer
|[datatype]{type:"%SECTION%",x:52}|
[Integer] is a [number] data type. It can store a [number] without its fractional component.
It is mainly used in calculations and number comparisons.

# float
|[datatype]{type:"%SECTION%",x:52}|
[Float] is a [number] data type. It can store a [number] along with its [fractional component].
It is used in high-precision calculations, i.e. computing angles of a long-distance ballistic trajectory.

# string
|[datatype]{type:"%SECTION%",x:52}|
[String] is a [text] data type, which can hold multiple characters. 
It is used by many devices for commands, queries and input/output messages.

# boolean
|[datatype]{type:"%SECTION%",x:52}|
[Boolean] is a [logic] data type, it can hold one of two values - [True] or [False]. 
Its primary use is in logic functions using [Boolean Algebra].

# itemstack
|[datatype]{type:"%SECTION%",x:52}|
[ItemStack] is a [compound] data type which holds information about a specific [stack] of items: its [ID], [amount], [metadata] (damage) and [NBT component].
It is mainly used to set an item filter or mark a specific item for a machine task.

# fluidstack
|[datatype]{type:"%SECTION%",x:52}|
[FluidStack] is a [compound] data type which holds information about a specific [fluid] of fluid: its [name], [amount] and [NBT component].
It is mainly used to set an fluid filter or mark a specific fluid for a machine task.

# vector
|[datatype]{type:"%SECTION%",x:52}|
[Vector] is a [compound] data type storing 3 number values. It can store [integers](#integer), [floats](#float) or [a mix of them].
It is used to represent values in 3-dimensional space, such as position or motion of objects.

# entity
|[datatype]{type:"%SECTION%",x:52}|
[Entity] is a [compound] data type which holds information about a specific in-world [entity]: its [name], [ID], [position], [motion] and [NBT data].
It is mostly used by weaponry to store and receive information of its targets. 

# array
|[datatype]{type:"%SECTION%",x:52}|
[Array] is a [collection] of data types, ordered by their [index] number starting from 0, it can hold any data type.
It is often used when a larger amount of arguments is required, but it has to be passed as a single variable.

# map
|[datatype]{type:"%SECTION%",x:52}|
[Map] is a [collection] of pairs of 2 data types called [entries].
The first element of an entry is called a [key] and second is called a [value].
The first can be used to get the second and in reverse.
An entry can hold any data type both in key and value field.
It is often used to represent a structure with named fields, using [Strings](#string) as [keys] and other types as [values].

# expression
|[datatype]{type:"%SECTION%",x:52}|
[Expression] is an [executable] data type used by the circuits of [Arithmetic-Logic Machine](arithmetic_logic_machine). 
It performs a pre-programmed function on [input parameters] in form of values or their [Accessors](#accessor) and returns a value.

# accessor
|[datatype]{type:"%SECTION%",x:52}|
[Accessor] is a [reference] data type used to get the value of **another** variable in a packet by referencing its [letter]. 
Accessors are mostly used inside [Expressions](#expression) to get a value dynamically from a **Packet**. 
They, too, are an integral part of the [Arithmetic-Logic Machine](arithmetic_logic_machine).