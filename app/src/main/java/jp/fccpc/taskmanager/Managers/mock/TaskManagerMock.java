package jp.fccpc.taskmanager.Managers.mock;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import jp.fccpc.taskmanager.Managers.TaskManager;
import jp.fccpc.taskmanager.Util.Utils;
import jp.fccpc.taskmanager.Values.BoardItem;
import jp.fccpc.taskmanager.Values.Task;
import jp.fccpc.taskmanager.Values.User;

/**
 * Created by tm on 2015/10/28.
 */
public class TaskManagerMock implements TaskManager{

    private static class TaskEntry {
        Task task;
        List<BoardItem> boardItems;
        List<User> doneUsers;

        public TaskEntry(Task task, List<BoardItem> boardItems) {
            this.task = task;
            this.boardItems = boardItems;
            this.doneUsers = Lists.newArrayList();
        }

        public TaskEntry(Task task, List<BoardItem> boardItems, List<User> doneUsers) {
            this.task = task;
            this.boardItems = boardItems;
            this.doneUsers = doneUsers;
        }
    }

    private static final User own;
    private static final List<TaskEntry> entries;

    private Long nextId = 7L;

    // for setting time
    private static Long getTime(String a) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = null;
        try {
            d = s.parse(a);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d.getTime();
    }

    static {
        own = new User(1L, "John Doe", "john@example.com");
        entries = Lists.newArrayList(
                new TaskEntry(
                        new Task(1L, 1L, "dummyTask1", getTime("2015/11/25 12:00:00"), "dummyDetail1\nA unique challenge for SSD storage caching management in a virtual machine (VM) environment is to accomplish the dual objectives: maximizing utilization of shared SSD cache devices and ensuring performance isolation among VMs. In this paper, we present our design and implementation of S-CAVE, a hypervisor-based SSD caching facility, which effectively manages a storage cache in a Multi-VM environment by collecting and exploiting runtime information from both VMs and storage devices. ", "dummyReminderTime1", 0L, 0L, null),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", getTime("2015/11/2 12:00:00"), "Current personal computers exhibit a nonnegligible energy consumption; e.g., a desktop computer for gaming may consume around 500 W. In HPC clusters and data processing centers, a single node may consume 50% more energy. Hence, minimizing energy consumption has become a hot topic in the design of large data centers [1][2].")
                        )
                ),
                new TaskEntry(
                        new Task(2L, 1L, "dummyTask2", getTime("2015/11/1 9:00:00"), "dummyDetail2", "dummyReminderTime2", 0L, 0L, null),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", getTime("2015/11/12 12:00:00"), "2015年5月9日(土)13:00-16:00 研究室を公開します．大学院受験を予定されている方の見学を歓迎します．(2015-03-26)"),
                                new BoardItem(2L, 1L, "John Doe", getTime("2015/11/10 10:13:00"), "fugafuga"),
                                new BoardItem(3L, 2L, "Alice Cartelet", getTime("2015/11/9 23:00:00"), "Virtualization techniques may provide significant energy savings, as they enable a larger resource usage by sharing a given hardware among several users, thus reducing the required amount of instances of that particular device. As a result, virtualization is being increasingly adopted in data centers. In this way, virtualizing GPUs may report power and cost benefits."),
                                new BoardItem(4L, 1L, "John Doe", getTime("2015/11/9 21:00:00"), "hogehoge \n fugafuga")
                        ),
                        Lists.newArrayList(new User(2L, "Alice Cartelet", "alice@example.com"),
                                            new User(3L, "Shinobu Ōmiya", ""),
                                            new User(1L, "John Doe", ""))
                ),
                new TaskEntry(
                        new Task(3L, 2L, "dummyTask3", getTime("2016/1/5 1:23:45"), "随着无线局域网（WLAN）技术的快速发展，越来越高的数据传输率/能源效率、快速的标准演化等趋势对无线网体系结构（MAC层和PHY层）提出了更高的要求。人们希望能够无线系统在实现高性能的同时支持可编程性，方便地对系统进行修改，支持创新的算法协议和跨层次优化（例如对网络上层、MAC层和PHY层进行协同设计以实现更加优化的整体网络系统）。这就要求一个带有可重配置特性的高性能无线局域网物理层与数据链路层硬件体系结构。\n" +
                                "        可重构逻辑可以在生产后动态改变实现的功能，同时具有硬件的性能优势和软件的可编程优势，FPGA为其中的一个典型代表。我们将以可重构逻辑为中心构建带有可重配置特性的高性能无线局域网物理层与数据链路层硬件体系", "dummyReminderTime3", 0L, 0L, null),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", 0L, "hogehoge")
                        )
                ),
                new TaskEntry(
                        new Task(4L, 2L, "dummyTask4", getTime("2015/12/3 4:50:00"), "dummyDetail4", "dummyReminderTime4", 0L, 0L, null),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", 0L, "hogehoge")
                        )
                ),
                new TaskEntry(
                        new Task(5L, 3L, "dummyTask5", getTime("2015/12/8 0:00:00"), "dummyDetail5", "dummyReminderTime5", 0L, 0L, null),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", 0L, "hogehoge")
                        )
                ),
                new TaskEntry( // 終了済み
                        new Task(6L, 3L, "dummyTask6", getTime("2015/10/30 3:00:00"), "dummyDetail6", "dummyReminderTime6", 0L, 0L, 0L),
                        Lists.newArrayList(
                                new BoardItem(1L, 1L, "John Doe", 0L, "hogehoge")
                        ),
                        Lists.newArrayList(own)
                )
        );
    }

    public TaskManagerMock(){}

    public List<Task> getTasks() {
        return Utils.map(entries, new Function<TaskEntry, Task>() {
            @Override
            public Task apply(TaskEntry input) {
                return input.task;
            }
        });
    }

    public List<Task> getGroupTasks(final Long groupId){
        return Utils.map(Utils.findAll(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry task) {
                return task.task.getGroupId() == groupId;
            }
        }), new Function<TaskEntry, Task>() {
            @Override
            public Task apply(TaskEntry input) {
                return input.task;
            }
        });
    }

    public Task get(final Long taskId){
        TaskEntry e = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry task) {
                return task.task.getTaskId() == taskId;
            }
        });
        return e == null ? null : e.task;
    }

    public void create(Task task){
        entries.add(new TaskEntry(task, Lists.<BoardItem>newArrayList()));
    }

    public void update(final Task task){
        TaskEntry o = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry input) {
                return input.task.getTaskId() == task.getTaskId();
            }
        });
        if (o != null) {
            o.task.setDeadline(task.getDeadline());
            o.task.setDetail(task.getDetail());
            o.task.setReminderTime(task.getReminderTime());
            o.task.setTitle(task.getTitle());
        } else {
            throw new NoSuchElementException();
        }
    }

    public void delete(final Long taskId){
        TaskEntry e = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry task) {
                return task.task.getTaskId() == taskId;
            }
        });
        if (e == null) throw new NoSuchElementException();
        entries.remove(e);
    }


    @Override
    public void getList(TaskListCallback callback) {
        callback.callback(getTasks());
    }

    @Override
    public void getList(Long groupId, TaskListCallback callback) {
        callback.callback(getGroupTasks(groupId));
    }

    @Override
    public void get(Long taskId, TaskCallback callback) {
        callback.callback(get(taskId));
    }

    @Override
    public void getBoardItems(final Long taskId, int begin, int end, BoardItemListCallback callback) {
        TaskEntry entry = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry input) {
                return input.task.getTaskId() == taskId;
            }
        });
        if (entry == null) {
            callback.callback(null);
            return;
        }
        callback.callback(entry.boardItems.subList(begin, Math.min(end, entry.boardItems.size())));
    }

    // get latest 'num' items
    public void getBoardItems(final Long taskId, int num, BoardItemListCallback callback) {
        TaskEntry entry = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry input) {
                return input.task.getTaskId() == taskId;
            }
        });
        if (entry == null) {
            callback.callback(null);
            return;
        }
        int end = entry.boardItems.size();
        int begin = (end - num) < 0 ? 0 : (end - num);
        callback.callback(entry.boardItems.subList(begin, end));
    }

    @Override
    public void create(Task task, Callback callback) {
        Task t = new Task(nextId, task.getGroupId(), task.getTitle(), task.getDeadline(), task.getDetail(), task.getReminderTime(), task.getCreatedAt(), task.getUpdatedAt(), task.getDoneAt());
        nextId++;
        create(t);
        callback.callback(true);
    }

    @Override
    public void createBoardItem(final Long taskId, BoardItem boardItem, Callback callback) {
        TaskEntry entry = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry input) {
                return input.task.getTaskId() == taskId;
            }
        });
        if (entry == null) {
            callback.callback(false);
            return;
        }
        Long newBoardNumber = (long)entry.boardItems.size() + 1;
        BoardItem item = new BoardItem(newBoardNumber, boardItem.getUserId(), boardItem.getUserName(), boardItem.getCreatedAt(), boardItem.getContent());
        entry.boardItems.add(item);
    }

    @Override
    public void update(Task task, Callback callback) {
        try {
            update(task);
        } catch (NoSuchElementException e) {
            callback.callback(false);
            return;
        }
        callback.callback(true);
    }

    @Override
    public void delete(Long taskId, Callback callback) {
        try {
            delete(taskId);
        } catch (NoSuchElementException e) {
            callback.callback(false);
            return;
        }
        callback.callback(true);
    }

    @Override
    public void finish(final List<Long> taskIds, Callback callback) {
        final long now = Calendar.getInstance().getTimeInMillis();

        for (final Long taskId : taskIds) {
            TaskEntry entry = Utils.find(entries, new Predicate<TaskEntry>() {
                @Override
                public boolean apply(TaskEntry input) {
                    return input.task.getTaskId() == taskId;
                }
            });
            if (entry == null) {
                callback.callback(false);
                return;
            }
            Task task = entry.task;
            entry.task = new Task(
                    task.getTaskId(),
                    task.getGroupId(),
                    task.getTitle(),
                    task.getDeadline(),
                    task.getDetail(),
                    task.getReminderTime(),
                    task.getCreatedAt(),
                    task.getUpdatedAt(),
                    now
            );
            entry.doneUsers.add(own);
        }
        callback.callback(true);
    }

    @Override
    public void getFinishedUserList(final Long taskId, UserListCallback callback) {
        TaskEntry entry = Utils.find(entries, new Predicate<TaskEntry>() {
            @Override
            public boolean apply(TaskEntry input) {
                return input.task.getTaskId() == taskId;
            }
        });
        if (entry == null) callback.callback(null);
        else {
            callback.callback(entry.doneUsers);
        }
    }
}
