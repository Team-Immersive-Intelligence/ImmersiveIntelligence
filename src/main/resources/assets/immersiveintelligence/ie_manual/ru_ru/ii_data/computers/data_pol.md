# meta
POL
Computer Revolution
# intro
@level_computer
POL is a programming language used by [Mainframe]
[Computers] to create sets of executable instructions called **Programs**.
|[wip_notice]|
# code_compilation0
A **POL** program is a sequence of statements, each occupying a single line. Before using any data operations, you must import the required [Circuit](_functional_circuits.md) library with the **USE** statement.
Variables are declared using their type name, followed by the variable letter prefixed with **@**, and an optional initial value.
# code_compilation0_example
|[pol_code]{code:"use BASIC\\n\\ninteger @a = 5\\nfloat @b = 3.14\\nstring @c = \"hello\"\\nboolean @d = 1"}|
# code_compilation1
A **POL** program is a sequence of statements, each occupying a single line. Before using any data operations, you must import the required [Circuit](_functional_circuits.md) library with the **USE** statement.
Variables are declared using their type name, followed by the variable letter prefixed with **@**, and an optional initial value.
# code_compilation0_example
|[pol_code]{code:"use BASIC\\n\\ninteger @a = 5\\nfloat @b = 3.14\\nstring @c = \"hello\"\\nboolean @d = 1"}|
# code_execution0
Programs are executed **one statement per tick** by default. The **WAIT** statement pauses execution for a given number of ticks.
The **IF** statement checks a condition and executes the next statement or code block only when it is met. **ELSE** handles the opposite case.
# code_execution0_example
|[pol_code]{code:"integer @a = 10\\n\\nif > @a 5\\n  type \"a is big\"\\nelse\\n  type \"a is small\"\\n\\nwait 20"}|
# code_execution1
**MARK** and **GOTO** allow creating labeled jump points and loops. **EXEC** jumps to a label and returns afterwards, acting like a function call.
Code blocks created with indentation (compiled as **BEGIN**/**END**) group multiple statements into one.
# code_execution1_example
|[pol_code]{code:"mark loop:\\n  integer @a = + @a 1\\n  type @a\\n  if < @a 10\\n    goto loop:\\n\\nmark greet:\\n  type \"hello!\"\\nend\\n\\nexec greet:"}|
# statements
**Statements** are the building blocks of a **POL** program. Each **Statement** has a distinct name it's called by and performs a different action.
Examples of an action are setting a value of a variable, stopping the program, or running an internal function.
Each **Statement** takes exactly one line, but some, like the if **Statement** require another statement to work properly.
The following pages list all the **Statements**, their usage, and exemplary use cases.
# statements_use
The **USE** statement is used to include a library of functions from a [Circuit](_functional_circuits.md) to be used by the program.
# statements_begin_end
The **BEGIN** and **END** statements are used to mark a fragment of a program, called a *code block*.
A *code block* is treated as a single statement, but if consisting of more than one statements, it will have a longer execution time, equal to the sum of all the statements within it.
# statements_begin_end2
Multiple *code blocks* can be nested within each other, but each **BEGIN** statement must have a corresponding **END** statement.
Although both exist as keywords, the recommended way to use them is through adding indentation to the code.
# statements_wait
The **WAIT** statement is used to pause the program for a specified amount of ticks.
During this time, the program will not execute any other statements.
# statements_sign
The **SIGN** statement is used to switch one of the 16 colored signal lamps on a Computer Terminal to a lit or unlit state.
The states are persistent and will remain until changed by another **SIGN** statement or power is cut off from the terminal.
# statements_mark
The **MARK** statement is used to set a label in the program that can be jumped to by the [GOTO](#statements_goto) statement.
A **MARK** label cannot contain spaces or names reserved by operations, and must be unique within the program.
# statements_goto
The **GOTO** statement is used to jump to a label set by the [MARK](#statements_mark) statement.
This can be useful for creating loops or conditional jumps in the program.
# statements_exec
The **EXEC** statement is used to jump to a label set by the [MARK](#statements_mark) statement and then return to the original position in the program.
This can be useful for defining callable *functions* in the program.
# statements_ext
The **EXT** statement is used call another POL program from the current one.
The program will share the same memory, but will not share imported functions and [External Device] links from the one calling it.
# statements_if
The **IF** statement is used to execute a block of code if a condition is met.
If the condition is not met, the program will skip the next instruction or block of code.
# statements_else
The **ELSE** statement is used to execute a block of code if the condition of the preceding [IF](#statements_if) statement is not met.
# statements_page
The **PAGE** statement is used to switch the [memory page](data_main.md#packets_basics) of the computer to a different one.
This can be useful for setting separate memory for different routines of the program.
# statements_wipe
The **WIPE** statement is used to clear all the current memory page of the computer.
# statements_copy
The **COPY** statement is used to copy the value of one variable to another.
The copied variable will have the same value as the original, but changes to one will not affect the other.
# statements_move
The **MOVE** statement is used to move the value of one variable to another, leaving the original variable empty.
# statements_swap
The **SWAP** statement is used to exchange the values of two variables in memory.

