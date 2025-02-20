# meta
Task System
Keep it Simple, Stupid

# intro
The task system is a concept of organising a machine's work schedule through a single iterable task. Tasks are used by many data devices, such as the [inserters](inserters.md), [packers](packer.md) and the [data routers](small_data_devices.md#router). Tasks are divided into two groups:
**Jobs** - tasks which will execute infinitely, unless manually removed.
**Requests** - tasks which are temporary, and will remove themselves after they are finished (i.e. after an inserter picks up a certain amount of items)

# details
By default, tasks are executed from the oldest to the newest. If a task cannot be executed, it will be skipped. After finishing a [Request] __(successfully or **not**)__, it is checked whether it should be removed.