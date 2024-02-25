package Service.InMemoryManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import Service.Interface.HistoryManager;
import Tasks.Task;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> historyTask = new HashMap<>();
    public static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next){
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private int size = 0;

    public void linkLast (Task element){
        final Node oldTail = tail;
        if(oldTail == null){
            final Node newTail = new Node(null, element, null);
            tail = newTail;
            head = newTail;
        } else {
            final Node newTail = new Node(oldTail, element, null);
            oldTail.next = newTail;
            tail = newTail;
        }

        size++;
    }

    public void removeNode(Node node){

        if(node.prev != null && node.next != null){
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node.prev == null){
            node.next.prev = null;
            head = node.next;
        } else if (node.next == null){
            node.prev.next = null;
            tail = node.prev;
        }
    }

    public ArrayList<Task> getTasks(){
        ArrayList<Task> list = new ArrayList<>();

        Node curNode = head;

        while(curNode.next != null){
            list.add(curNode.data);
            curNode = curNode.next;
        }
        list.add(curNode.data);


        return list;
    }

    @Override
    public void add(Task task){
        Task taskForArray = new Task(task.getName(), task.getDescription(), task.getStatus(), task.getId());
        if (historyTask.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(taskForArray);
        historyTask.put(taskForArray.getId(), tail);

    }

    @Override
    public void remove(int id){
        Node task = historyTask.get(id);
        removeNode(task);
        historyTask.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory(){
        return (ArrayList<Task>) getTasks();
    }
}

