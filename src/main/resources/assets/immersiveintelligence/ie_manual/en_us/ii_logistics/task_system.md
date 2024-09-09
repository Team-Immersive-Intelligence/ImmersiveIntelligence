# meta
Task System
Keep it Simple, Stupid

# intro
The task system is a concept of organising a machine's work schedule through a single iterable task. Tasks are used by many data devices, such as the inserters, packers and the data routers. Tasks are divided into two groups:
Jobs - tasks which will execute infinitely, unless manually removed.
Requests - tasks which are temporary, and will remove themselves after they are finished (i.e. after an inserter picks up a certain amount of items)

# details
By default, tasks are executed from the oldest fo the newest. If a task cannot be executed, it will be skipped. After finishing a Request (successfully or not), it is checked whether it should be removed.